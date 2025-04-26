package com.near_reality.tools.cloudflare

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.datetime.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Simple wrapper around CloudFlare API for uploading images.
 *
 * @author Stan van der Bend
 */
@ExperimentalSerializationApi
object Cloudflare {

    private const val accountId = "801d6d1c69b3214b215b8070c07cb953"

    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json()
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        uploadItemSprites(Paths.get("/item/sprites"))

//        val time = kotlinx.datetime.Clock.System.now().minus(5.days)
//        val gate = Semaphore(5)
//        runBlocking {
//            coroutineScope {
//                for (page in 0..100) {
//                    listImage(1, 100).result?.images?.forEach {
//
//                        if (it.id.startsWith("item/") && it.uploaded > time) {
//                            launch {
//                                gate.withPermit {
//                                    deleteImage(it)
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
    }

    private fun uploadItemSprites(folder: Path) {

        // restrict only 3 coroutines to actively perform requests, due to rate-limiting
        val gate = Semaphore(10)

        // read successfully uploaded ones, so we don't upload these twice
        val previousUploadResults = File("responses.pb").let {
            if (it.exists())
                ProtoBuf.decodeFromByteArray<List<Response.UploadImage>>(it.readBytes())
            else
                emptyList()
        }.toMutableList()

        runBlocking {
            coroutineScope {
                folder.toFile().listFiles()!!
                    .forEach {
                        val itemId = it.nameWithoutExtension.toInt()
                        val id = "item/$itemId"
                        if (previousUploadResults.any { image -> image.result?.id == id }) {
                            println("skipping $itemId")
                            return@forEach
                        }
                        launch {
                            gate.withPermit {
                                try {
                                    val response = uploadImage(it, "item/$itemId")
                                    synchronized(previousUploadResults) {
                                        println(response)
                                        previousUploadResults += response
                                        File("responses.pb").writeBytes(ProtoBuf.encodeToByteArray(previousUploadResults))
                                    }
                                } catch (e :Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
            }
        }
    }

    @Suppress("unused")
    suspend fun listImage(page: Int, perPage: Int): Response.ListImages {
        val httpResponse =
            client.get("https://api.cloudflare.com/client/v4/accounts/$accountId/images/v1?page=$page&per_page=$perPage") {
                header("Authorization", "Bearer o2YH8GDLSokfmsg548i9ZG7jb6BR9jZYmCkSfoxc")
            }
        return httpResponse.body()
    }

    private suspend fun deleteImage(image: Response.Image) {
        val httpResponse = client.delete("https://api.cloudflare.com/client/v4/accounts/$accountId/images/v1/${image.id}") {
            header("Authorization", "Bearer o2YH8GDLSokfmsg548i9ZG7jb6BR9jZYmCkSfoxc")
        }
        println("deleted $image -> "+httpResponse.body<String>())
    }

    private suspend fun uploadImage(file: File, id: String): Response.UploadImage {

        require(file.exists())

        val httpResponse = client.submitFormWithBinaryData(
            url = "https://api.cloudflare.com/client/v4/accounts/$accountId/images/v1",
            formData = formData {
                append("id", id)
                append("file", file.readBytes(), Headers.build {
                    append(HttpHeaders.ContentDisposition, "filename=${file.name}")
                })
            }
        ) {
            header("Authorization", "Bearer o2YH8GDLSokfmsg548i9ZG7jb6BR9jZYmCkSfoxc")
        }
        println("$file -> "+httpResponse.body<String>())
        return httpResponse.body()
    }

    @Serializable
    sealed interface Response {

        val success: Boolean
        val errors: List<Error>
        val messages: List<String>

        @Serializable
        data class UploadImage(
            override val success: Boolean,
            override val errors: List<Error> = emptyList(),
            override val messages: List<String> = emptyList(),
            val result: Image?
        ): Response

        @Serializable
        data class ListImages(
            override val success: Boolean,
            override val errors: List<Error> = emptyList(),
            override val messages: List<String> = emptyList(),
            val result: Result?
        ) : Response {

            @Serializable
            data class Result(val images: List<Image>)
        }

        @Serializable
        data class Image(
            val id: String,
            val filename: String?,
            val metadata: Map<String, String> = emptyMap(),
            val requireSignedURLs: Boolean,
            val variants: List<String> = emptyList(),
            val uploaded: Instant
        )

        @Serializable
        data class Error(
            val code: Int,
            val message: String
        )
    }
}
