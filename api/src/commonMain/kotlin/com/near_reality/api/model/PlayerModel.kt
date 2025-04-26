package com.near_reality.api.model

import com.near_reality.api.model.ItemContainer.Policy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

var PlayerData.exchangePoints: Int by attribute("exchangePoints", 0)
val PlayerData.totalVoteCredits: Int by attribute("vote_points", 0)
val PlayerData.registered: Boolean by attribute<PlayerData, Int>("registered", 0).asBoolean()

@Serializable
data class PlayerData(
    val playerInformation: MetaInformation,
    val appearance: Appearance = Appearance.DEFAULT_MALE,
    val skills: Skills = Skills(),
    val equipment: ItemContainerWrapper = ItemContainerWrapper(Policy.ALWAYS_STACK),
    val inventory: ItemContainerWrapper = ItemContainerWrapper(Policy.NORMAL),
    val seedVault: ItemContainerWrapper = ItemContainerWrapper(Policy.ALWAYS_STACK),
    val runePouch: ItemContainerWrapper = ItemContainerWrapper(Policy.ALWAYS_STACK),
    val seedBox: ItemContainerWrapper = ItemContainerWrapper(Policy.NORMAL),
    val lootingBag: ItemContainerWrapper = ItemContainerWrapper(Policy.NORMAL),
    val herbSack: ItemContainerWrapper = ItemContainerWrapper(Policy.ALWAYS_STACK),
    val bonePouch: ItemContainerWrapper = ItemContainerWrapper(Policy.ALWAYS_STACK),
    val dragonhidePouch: ItemContainerWrapper = ItemContainerWrapper(Policy.ALWAYS_STACK),
    val gemBag: ItemContainerWrapper = ItemContainerWrapper(Policy.ALWAYS_STACK),
    val retrievalService: ItemContainerWrapper = ItemContainerWrapper(Policy.NORMAL),
    val privateStorage: ItemContainerWrapper = ItemContainerWrapper(Policy.NORMAL),
    val bank: ItemContainerWrapper = ItemContainerWrapper(Policy.ALWAYS_STACK),
    val gravestone: ItemContainerWrapper = ItemContainerWrapper(Policy.ALWAYS_STACK),
    override val attributes: Attributes = Attributes()
) : AttributeHolder {

    val username get() = playerInformation.username
    val containerWrapperList get() = listOf(equipment, inventory, seedVault, runePouch, seedBox, lootingBag, herbSack, bonePouch, dragonhidePouch, gemBag, retrievalService, privateStorage, bank, gravestone)

    @Serializable
    data class MetaInformation(
        val username: String
    )

    @Serializable
    data class Skills(
        @SerialName("level")
        val levels: List<Int> = Skill.entries.map { it.defaultLevel },
        @SerialName("experience")
        val experiences: List<Double> = Skill.entries.map { it.defaultExperience.toDouble() },
    )


    @Serializable
    data class Appearance(
        @SerialName("appearance")
        val kitIds: List<Int>,
        @SerialName("colours")
        val colours: List<Int>,
        val male: Boolean,
        val headIcon: Int = -1,
    ) {

        companion object {
            val DEFAULT_MALE = Appearance(
                kitIds = listOf(0, 10, 18, 26, 33, 36, 42),
                colours = listOf(0, 0, 0, 0, 0),
                male = true,
            )
            val DEFAULT_FEMALE = Appearance(
                kitIds = listOf(45, 1000, 56, 61, 68, 70, 79),
                colours = listOf(0, 0, 0, 0, 0),
                male = false
            )
        }
    }
}
