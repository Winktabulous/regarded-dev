package com.near_reality.tools.discord.staff.model

import com.near_reality.tools.discord.DiscordServer
import com.near_reality.tools.discord.staff.DiscordStaffBot
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.rest.builder.interaction.attachment
import dev.kord.rest.builder.interaction.integer
import io.ktor.client.request.forms.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mgi.types.config.draw.toBufferedImage
import mgi.types.config.items.ItemDefinitions
import mgi.types.config.items.loadSpritePixels
import mgi.types.config.items.toModel
import mgi.types.draw.Rasterizer3D
import mgi.types.draw.model.ModelData
import java.io.ByteArrayOutputStream
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import javax.imageio.ImageIO

private var modelData by atomic<ModelData?>(null)

private const val genInvModel = "gen_inv_model"

internal fun DiscordStaffBot.configureModelTestCommand() {
    Rasterizer3D.Rasterizer3D_setBrightness(0.5)
    launchGated {
        registerChatInputCommand(genInvModel, "Generate inventory model",
            builder = {
                attachment("file", "some .dat file") {
                    required = true
                }
                integer("id", "item id") {

                    required = true
                }
            },
            handler = {
                if (channelId != DiscordServer.Staff.modelChannelId)
                    return@registerChatInputCommand


                if (!isManager(user)) {
                    respondPublic { content = "You do not have sufficient permissions to do an economy search." }
                    return@registerChatInputCommand
                }

                val commandName = command.rootName
                if (commandName != genInvModel)
                    return@registerChatInputCommand

                val attachment = command.attachments["file"]!!
                val modelBytes = withContext(Dispatchers.IO) {
                    URL(attachment.data.url).openStream().readAllBytes()
                }
                modelData = ModelData(modelBytes)

                when (commandName) {
                    genInvModel -> {
                        val id = command.integers["id"]!!.toInt()
                        val itemDefinition = ItemDefinitions.get(id)!!
                        val model = itemDefinition.toModel(modelData!!)
                        val pixels = itemDefinition.loadSpritePixels(scale = 2, model = model)
                        val image = pixels.toBufferedImage()
                        val imageBytes = withContext(Dispatchers.IO) {
                            val out = ByteArrayOutputStream()
                            ImageIO.write(image, "png", out)
                            out.toByteArray()
                        }
                        withContext(Dispatchers.IO) {
                            Files.write(Paths.get("bla.png"), imageBytes)
                        }
                        respondPublic {
                            addFile(
                                "inventory_${itemDefinition.id}.png",
                                ChannelProvider(imageBytes.size.toLong()) {
                                    imageBytes.inputStream().toByteReadChannel()
                                })
                        }
                    }
                }
            }
        )
    }
}
