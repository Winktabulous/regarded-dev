package com.near_reality.api.dao

import com.near_reality.api.model.CreditPackage
import com.near_reality.api.model.CreditPackageOrder
import com.near_reality.api.model.CreditStoreCategory
import com.near_reality.api.model.CreditStoreProduct
import com.near_reality.api.model.CreditStoreType
import com.near_reality.api.model.PaypalDispute
import com.near_reality.api.util.defaultTimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.json.jsonb
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object CreditPackages : IntIdTable("credit_packages") {
    val title = varchar("title", 255)
    val price = float("price")
    val imageUrl = varchar("image_url", 255)
    val credits = integer("credits")
    val bonusCredits = integer("bonus_credits")
}

class CreditPackageEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CreditPackageEntity>(CreditPackages)
    var title by CreditPackages.title
    var price by CreditPackages.price
    var imageUrl by CreditPackages.imageUrl
    var credits by CreditPackages.credits
    var bonusCredits by CreditPackages.bonusCredits
    val totalCredits get() = credits + bonusCredits
    fun toApiModel() = CreditPackage(
        id = id.value,
        title = title,
        price = price,
        imageUrl = imageUrl,
        credits = credits,
        bonusCredits = bonusCredits
    )
}

object CreditPackageOrders : IntIdTable("credit_package_orders") {
    val user = reference("user", Users).index()
    val product = reference("product", CreditPackages).index()
    val productAmount = integer("product_amount")
    val time = timestamp("time")
    val status = enumeration<CreditPackageOrder.Status>("status")
    val paymentMethod = enumeration<CreditPackageOrder.PaymentMethod>("payment_method")
    val transactionId = varchar("transaction_id", 200).nullable()
}

class CreditPackageOrderEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object: IntEntityClass<CreditPackageOrderEntity>(CreditPackageOrders)
    var user by UserEntity referencedOn CreditPackageOrders.user
    var product by CreditPackageEntity referencedOn CreditPackageOrders.product
    var productAmount by CreditPackageOrders.productAmount
    var time by CreditPackageOrders.time
    var status by CreditPackageOrders.status
    var paymentMethod by CreditPackageOrders.paymentMethod
    var transactionId by CreditPackageOrders.transactionId

    val cost get() = product.price * productAmount
    val creditsReward get() = product.totalCredits * productAmount

    fun toApiModel(includeCredentials: Boolean = false) = CreditPackageOrder(
        id = id.value,
        time = time.toLocalDateTime(defaultTimeZone),
        creditPackage = product.toApiModel(),
        amount = productAmount,
        user = user.toApiModel(includeCredentials),
        status = status,
        paymentMethod = paymentMethod,
        transactionId = transactionId
    )
}



object CreditStoreProducts : LongIdTable("credit_store_products") {
    val itemId = integer("item_id")
    val itemAmount = integer("item_amount").default(1)
    val price = integer("price")
    val priceDiscount = integer("price_discount").nullable()
    val stock = integer("stock").nullable() // null means infinite stock
    val categories = jsonb<Set<CreditStoreCategory>>("categories", Json)
    val priority = integer("priority").default(1)
    val types = jsonb<Set<CreditStoreType>>("types", Json)
}

class CreditStoreProductEntity(id: EntityID<Long>) : ModelEntity<CreditStoreProduct>(id) {
    companion object : LongEntityClass<CreditStoreProductEntity>(CreditStoreProducts) {
        fun new(product: CreditStoreProduct) = new {
            setFrom(product)
        }
        fun edit(id: Int, product: CreditStoreProduct) = findById(id.toLong())?.apply {
            setFrom(product)
        }
    }

    var itemId by CreditStoreProducts.itemId
    var itemAmount by CreditStoreProducts.itemAmount
    var price by CreditStoreProducts.price
    var priceDiscount by CreditStoreProducts.priceDiscount
    var stock by CreditStoreProducts.stock
    var categories by CreditStoreProducts.categories
    var types by CreditStoreProducts.types
    var priority by CreditStoreProducts.priority

    val priceWithDiscount get() = price - (priceDiscount ?: 0)
    fun setFrom(product: CreditStoreProduct) {
        itemId = product.itemId
        itemAmount = product.itemAmount
        price = product.price
        priceDiscount = product.priceDiscount
        stock = product.stock
        categories = product.categories
        types = product.types
    }
    override fun toModel(): CreditStoreProduct = CreditStoreProduct(
        id = id.value.toInt(),
        itemId = itemId,
        itemAmount = itemAmount,
        price = price,
        priceDiscount = priceDiscount,
        stock = stock,
        priority = priority,
        categories = categories,
        types = types,
    )
}

object PaypalDisputes : LongIdTable("disputes_paypal") {
    val disputeId = varchar("dispute_id", 255)
    val createTime = datetime("create_time")
    val updateTime = datetime("update_time")
    val order = reference("order", CreditPackageOrders).uniqueIndex()
    val user = reference("user", Users).index()
    val reason = text("reason")
    val status = varchar("status", 255)
    val disputeAmount = varchar("dispute_amount", 255)
    val disputeOutcome = varchar("dispute_outcome", 255).nullable()
    val messages = jsonb<List<PaypalDispute.Message>>("messages", Json)
    val links = jsonb<List<PaypalDispute.Link>>("links", Json)
}

class PaypalDisputeEntity(id: EntityID<Long>) : ModelEntity<PaypalDispute>(id) {

    companion object : LongEntityClass<PaypalDisputeEntity>(PaypalDisputes) {
        fun new(dispute: PaypalDispute) = new {
            setFrom(dispute)
        }
        fun edit(id: Int, dispute: PaypalDispute) = findById(id.toLong())?.apply {
            setFrom(dispute)
        }
    }

    var disputeId by PaypalDisputes.disputeId
    var createTime by PaypalDisputes.createTime
    var updateTime by PaypalDisputes.updateTime
    var order by CreditPackageOrderEntity referencedOn PaypalDisputes.order
    var user by UserEntity referencedOn PaypalDisputes.user
    var reason by PaypalDisputes.reason
    var status by PaypalDisputes.status
    var disputeAmount by PaypalDisputes.disputeAmount
    var disputeOutcome by PaypalDisputes.disputeOutcome
    var messages by PaypalDisputes.messages
    var links by PaypalDisputes.links


    override fun toModel(): PaypalDispute =
        PaypalDispute(
            disputeId = disputeId,
            createTime = createTime,
            updateTime = updateTime,
            order = order.toApiModel(),
            user = user.toApiModel(),
            reason = reason,
            status = status,
            amount = disputeAmount,
            outcome = disputeOutcome ?: "",
            messages = messages,
            links = links
        )

    fun setFrom(dispute: PaypalDispute) {
        disputeId = dispute.disputeId
        createTime = dispute.createTime
        updateTime = dispute.updateTime
        order = CreditPackageOrderEntity[dispute.order.id]
        user = UserEntity[dispute.user.id]
        reason = dispute.reason
        status = dispute.status
        disputeAmount = dispute.amount
        disputeOutcome = dispute.outcome
        messages = dispute.messages
        links = dispute.links
    }
}
