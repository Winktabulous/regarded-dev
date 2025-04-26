package com.near_reality.api.model

import com.near_reality.api.model.CreditPackageOrder.PaymentMethod
import com.near_reality.api.model.CreditPackageOrder.Status
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder

/**
 * Represents an order of a [StoreCreditPackage].
 *
 * @author Stan van der Bend
 *
 * @param id                a unique id associated with this order.
 * @param productId         the [StoreCreditPackage.id] of the product being purchased.
 * @param accountName         the [GameAccount.id] of the account purchasing the product.
 * @param status            the [Status] of this order.
 * @param paymentMethod     the [PaymentMethod] used to fulfill this order.
 */
@Serializable
data class CreditPackageOrder(
    val id: Int,
    val time: LocalDateTime,
    val user: User,
    val creditPackage: CreditPackage,
    val amount: Int,
    val status: Status,
    val paymentMethod: PaymentMethod,
    val transactionId: String? = null,
) {

    @Serializable
    data class Pending(
        val order: CreditPackageOrder,
        val payUrl: String,
    )

    /**
     * The status of a [CreditPackageOrder].
     */
    @Serializable
    enum class Status {
        CREATED,
        PENDING,
        CANCELED,
        DISPUTED,
        PAID
    }

    /**
     * The supported payment methods to fulfill an [CreditPackageOrder] with.
     */
    @Serializable
    enum class PaymentMethod {
        PAYPAL,
        COINBASE
    }
}

@Serializable
data class PaypalDispute(
    val disputeId: String,
    val createTime: LocalDateTime,
    val updateTime: LocalDateTime,
    val order : CreditPackageOrder,
    val user: User,
    val reason: String,
    val status: String,
    val amount: String,
    val outcome: String?,
    val messages: List<Message>,
    val links: List<Link>
)  {
    @Serializable
    data class Message(
        @SerialName("posted_by") val postedBy: String,
        @SerialName("time_posted") val timePosted: String,
        val content: String,
        val documents: List<Document> = emptyList()
    ) {
        @Serializable
        data class Document(
            val name: String,
            val url: String
        )
    }
    @Serializable
    data class Link(
        val href: String,
        val rel: String,
        val method: String
    )

}

@Serializable
data class CreditPackage(
    val id: Int,
    val title: String,
    val price: Float,
    val imageUrl: String,
    val credits: Int,
    val bonusCredits: Int
) {
    val totalCredits get() = credits + bonusCredits
}

@Serializable
data class CreditStoreCartItem(
    val product: CreditStoreProduct,
    val amount: Int
) {
    val cost get() = product.priceWithOptionalDiscount * amount
}

@Serializable
data class CreditStoreProduct(
    val id: Int? = null,
    val itemId: Int,
    val itemAmount: Int = 1,
    val price: Int,
    val priceDiscount: Int? = null,
    val stock: Int? = null,
    val priority: Int = 1,
    @Serializable(with = CreditStoreCategorySetSerializer::class)
    val categories: Set<CreditStoreCategory> = emptySet(),
    @Serializable(with = CreditStoreTypeSetSerializer::class)
    val types: Set<CreditStoreType> = emptySet()
) {
    val quantity get() = (stock)?:Int.MAX_VALUE
    val limited get() = categories.contains(CreditStoreCategory.LimitedTime)
    val discounted get() = priceDiscount != null && priceDiscount > 0
    val sortPriority get() = priority
    val priceWithOptionalDiscount get() = price - (priceDiscount?:0)

    val trueCategories : Set<CreditStoreCategory>
        get() = categories.apply {
            if(quantity != Int.MAX_VALUE && !this.contains(CreditStoreCategory.LimitedTime))
                this.plus(CreditStoreCategory.LimitedTime)
        }

}

@Serializable
enum class CreditStoreType {
    Regular,
    Ironman
}

@Serializable
enum class CreditStoreCategory {
    BestSellers,
    LimitedTime,
    Pins,
    Weapons,
    Armory,
    Supplies,
    Boosts,
    Pets,
    Cosmetic,
    Miscellaneous;

    companion object {
        operator fun get(ordinal: Int) = entries[ordinal]
    }
}

object CreditStoreCategorySetSerializer : KSerializer<Set<CreditStoreCategory>> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("CreditStoreCategorySet", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Set<CreditStoreCategory> {
        val string = decoder.decodeString()
        return string.split(",").map { CreditStoreCategory.valueOf(it) }.toSet()
    }

    override fun serialize(encoder: kotlinx.serialization.encoding.Encoder, value: Set<CreditStoreCategory>) {
        encoder.encodeString(value.joinToString(","))
    }
}

object CreditStoreTypeSetSerializer : KSerializer<Set<CreditStoreType>> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("CreditStoreTypeSet", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Set<CreditStoreType> {
        val string = decoder.decodeString()
        return string.split(",").map { CreditStoreType.valueOf(it) }.toSet()
    }

    override fun serialize(encoder: kotlinx.serialization.encoding.Encoder, value: Set<CreditStoreType>) {
        encoder.encodeString(value.joinToString(","))
    }
}
