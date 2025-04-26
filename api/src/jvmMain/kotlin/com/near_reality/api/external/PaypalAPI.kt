package com.near_reality.api.external

import com.near_reality.api.model.CreditPackageOrder
import com.near_reality.api.model.PaypalDispute.Link
import com.near_reality.api.model.PaypalDispute.Message
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import java.time.format.DateTimeFormatter

object PaypalAPI {

    private lateinit var clientID: String
    private lateinit var secret: String
    private lateinit var client: HttpClient
    private lateinit var accessToken: String
    private val logger = LoggerFactory.getLogger(PaypalAPI::class.java)
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    fun init(
        clientID: String,
        secret: String,
        sandboxMode: Boolean = false,
    ) {
        PaypalAPI.clientID = clientID
        PaypalAPI.secret = secret
        client = createHttpClient(sandboxMode)
    }

    suspend fun createOrder(order: CreditPackageOrder, uid: String): PaypalOrder.CreateResponse {
        val rawResponse = client.post("/v2/checkout/orders") {
            contentType(ContentType.Application.Json)
            header("PayPal-Request-Id", order.id.toString())
            setBody(
                PaypalOrder(
                    purchaseUnits = listOf(
                        PaypalPurchaUnit(
                            description = order.creditPackage.title,
                            customId = order.id.toString(),
                            items = listOf(
                                PaypalPurchaUnit.Item(
                                    name = order.creditPackage.title,
                                    quantity = "1",
                                    description = "Credits can be spend in our in-game store for various items.",
                                    category = PaypalPurchaUnit.Category.DIGITAL_GOODS,
                                    imageUrl = order.creditPackage.imageUrl,
                                    unitAmount = PaypalAmount(
                                        currencyCode = "USD",
                                        value = order.creditPackage.price.toString()
                                    )
                                )
                            ),
                            amount = PaypalCost(
                                currencyCode = "USD",
                                value = order.creditPackage.price.toString(),
                                breakdown = Breakdown(
                                    itemTotal = PaypalAmount(
                                        currencyCode = "USD",
                                        value = order.creditPackage.price.toString()
                                    )
                                )
                            )
                        )
                    ),
                    paymentSource = PaymentSource(
                        paypal = Paypal(
                            experienceContext = ExperienceContext(
                                returnUrl = "https://api.near-reality.com/store/paypal/capture?order-id=${order.id}&uid=$uid",
                                cancelUrl = "https://api.near-reality.com/store/paypal/cancel?order-id=${order.id}&uid=$uid",
                                shippingPreference = ShippingPreference.NO_SHIPPING
                            )
                        )
                    )
                )
            )
        }
        if (rawResponse.status == HttpStatusCode.Unauthorized) {
            requestNewToken()
            return createOrder(order, uid)
        }
        logger.trace("Received response {} for attempted creation of {}", rawResponse, order)
        return rawResponse.body()
    }

    suspend fun captureOrder(order: CreditPackageOrder): PaypalCaptureResponse {
        val transactionId = order.transactionId?: error("Order has no transaction id")
        val captureHttpResponse = client.post("/v2/checkout/orders/$transactionId/capture") {
            contentType(ContentType.Application.Json)
            setBody("{}")
        }
        if (captureHttpResponse.status == HttpStatusCode.Unauthorized) {
            requestNewToken()
            return captureOrder(order)
        }
        logger.trace("Received response {} for attempted capture of {}", captureHttpResponse, order)
        return captureHttpResponse.body()
    }

    suspend fun showOrderDetails(id: String): PaypalOrderDetails {
        val response = client.get("/v2/checkout/orders/$id")
        if (response.status == HttpStatusCode.Unauthorized) {
            requestNewToken()
            return showOrderDetails(id)
        }
        logger.trace("Received response {} for attempted details of {}", response, id)
        val details = response.body<PaypalOrderDetails>()
        return details
    }

    suspend fun listDisputesAfter(
        time: LocalDateTime? = null,
        pageSize: Int = 50,
        disputeStates: Set<DisputeState> = emptySet(),
        nextPageToken: String? = null
    ): DisputesResponse {

        val response = client.get("/v1/customer/disputes") {
            if (time != null)
                parameter("start_time", formatDateTime(time))
            parameter("page_size", pageSize)
            if (nextPageToken != null)
                parameter("next_page_token", nextPageToken)
            if (disputeStates.isNotEmpty())
                parameter("dispute_state", disputeStates.joinToString(","))
        }
        if (response.status == HttpStatusCode.Unauthorized) {
            requestNewToken()
            return listDisputesAfter(time, pageSize, disputeStates, nextPageToken)
        }
        logger.trace("Received response {} for attempted disputes after {}", response, time)
        return response.body()
    }

    suspend fun showDisputeDetails(id: String): DisputeDetails {
        val response = client.get("/v1/customer/disputes/$id")
        if (response.status == HttpStatusCode.Unauthorized) {
            requestNewToken()
            return showDisputeDetails(id)
        }
        logger.trace("Received response {} for attempted details of {}", response, id)
        return response.body()
    }

    private fun formatDateTime(dateTime: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        return formatter.format(dateTime.toJavaLocalDateTime())
    }

    private fun createHttpClient(sandboxMode: Boolean) = HttpClient {
        install(ContentNegotiation) {
            json(json)
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(DefaultRequest) {
            url(if (sandboxMode)
                "https://api-m.sandbox.paypal.com"
            else
                "https://api-m.paypal.com")
            headers {
                if (this@PaypalAPI::accessToken.isInitialized)
                    bearerAuth(accessToken)
            }
        }
    }

    private suspend fun requestNewToken(): PaypalTokenResponse =
        client.post("/v1/oauth2/token") {
            basicAuth(clientID, secret)
            contentType(ContentType.Application.FormUrlEncoded)
            parameter("grant_type", "client_credentials")
        }.body<PaypalTokenResponse>().apply {
            logger.info("Received new PayPal token {}", access_token)
            this@PaypalAPI.accessToken = access_token
        }
}

@Serializable
data class PaypalCaptureResponse(
    val id: String,
    val status: PaypalOrder.Status,
)

@Serializable
data class PaypalOrder(
    val intent: Intent = Intent.CAPTURE,
    @SerialName("purchase_units")
    val purchaseUnits: List<PaypalPurchaUnit>,
    @SerialName("payment_source")
    val paymentSource: PaymentSource,
) {
    @Serializable
    data class CreateResponse(
        val id: String,
        val status: Status,
        val links: List<PaypalLink>,
    ) {
        val payerActionLinkOrNull get() = links.find { it.rel == "payer-action" }?.href
    }

    @Serializable
    enum class Status {
        CREATED,
        SAVED,
        APPROVED,
        VOIDED,
        COMPLETED,
        PAYER_ACTION_REQUIRED
    }
}

@Serializable
data class PaymentSource(
    val paypal: Paypal,
)

@Serializable
data class Paypal(
    @SerialName("experience_context")
    val experienceContext: ExperienceContext,
)

@Serializable
data class PaypalPurchaUnit(
    val description: String,
    @SerialName("custom_id")
    val customId: String,
    val amount: PaypalCost,
    val items: List<Item>
) {

    @Serializable
    data class Item(
        val name: String,
        val quantity: String,
        val description: String,
        val category: Category,
        @SerialName("image_url")
        val imageUrl: String,
        @SerialName("unit_amount")
        val unitAmount: PaypalAmount
    )

    @Serializable
    enum class Category {
        DIGITAL_GOODS,
        PHYSICAL_GOODS,
        DONATION
    }
}

@Serializable
data class PaypalTokenResponse(
    val scope: String,
    val access_token: String,
    val token_type: String,
    val app_id: String,
    val expires_in: Int,
)


@Serializable
data class PaypalOrderDetails(
    val id: String,
    val status: PaypalOrder.Status,
    @SerialName("purchase_units")
    val purchaseUnits: List<PaypalPurchaUnit>,
    @SerialName("payment_source")
    val paymentSource: PaymentSource,
    @SerialName("create_time")
    val createTime: String,
    val payer: Payer,
)

@Serializable
data class Payer(
    val name: Name,
    @SerialName("email_address")
    val emailAddress: String,
    @SerialName("payer_id")
    val payerId: String,
)

@Serializable
data class Name(
    @SerialName("given_name")
    val givenName: String,
    val surname: String,
)

fun PaypalOrder.Status.toOrderStatus() = when (this) {
    PaypalOrder.Status.CREATED -> CreditPackageOrder.Status.CREATED
    PaypalOrder.Status.SAVED -> CreditPackageOrder.Status.CREATED
    PaypalOrder.Status.APPROVED -> CreditPackageOrder.Status.PENDING
    PaypalOrder.Status.VOIDED -> CreditPackageOrder.Status.CANCELED
    PaypalOrder.Status.COMPLETED -> CreditPackageOrder.Status.PAID
    PaypalOrder.Status.PAYER_ACTION_REQUIRED -> CreditPackageOrder.Status.PENDING
}

@Serializable
data class PaypalLink(
    val href: String,
    val rel: String,
    val method: String? = null,
)

@Serializable
data class ExperienceContext(
    @SerialName("return_url")
    val returnUrl: String,
    @SerialName("cancel_url")
    val cancelUrl: String,
    @SerialName("shipping_preference")
    val shippingPreference: ShippingPreference,
)

@Serializable
enum class ShippingPreference {
    NO_SHIPPING,
    GET_FROM_FILE,
    SET_PROVIDED_ADDRESS
}

@Serializable
enum class Intent {
    CAPTURE,
    AUTHORIZE
}


@Serializable
data class PaypalCost(
    @SerialName("currency_code")
    val currencyCode: String,
    val value: String,
    val breakdown: Breakdown
)

@Serializable
data class Breakdown(
    @SerialName("item_total")
    val itemTotal: PaypalAmount
)


@Serializable
data class PaypalAmount(
    @SerialName("currency_code")
    val currencyCode: String ,
    val value: String,
)
@Serializable
data class Dispute(
    @SerialName("dispute_id") val disputeId: String,
    @SerialName("create_time") val createTime: String,
    @SerialName("update_time") val updateTime: String,
    val status: String,
    val reason: String,
    @SerialName("dispute_state") val disputeState: DisputeState? = null,
    @SerialName("dispute_amount") val disputeAmount: DisputeAmount? = null,
    @SerialName("dispute_asset") val disputeAsset: DisputeAsset? = null,
    val links: List<Link>
)

@Serializable
data class DisputeAmount(
    @SerialName("currency_code") val currencyCode: String,
    val value: String
)

@Serializable
data class DisputeAsset(
    @SerialName("asset_symbol") val assetSymbol: String,
    val quantity: String
)

@Serializable
data class DisputesResponse(
    val items: List<Dispute>,
    val links: List<Link>
)

@Serializable
enum class DisputeState {
    RESOLVED,
    REQUIRED_ACTION,
    REQUIRED_OTHER_PARTY_ACTION,
    UNDER_PAYPAL_REVIEW
}

@Serializable
data class DisputeDetails(
    @SerialName("dispute_id") val disputeId: String,

    @SerialName("create_time")
    @Serializable(with = LocalDateTimePaypalSerializer::class)
    val createTime: LocalDateTime,
    @SerialName("update_time")
    @Serializable(with = LocalDateTimePaypalSerializer::class)
    val updateTime: LocalDateTime,
    @SerialName("disputed_transactions") val disputedTransactions: List<DisputedTransaction>,
    val reason: String,
    val status: String,
    @SerialName("dispute_amount") val disputeAmount: DisputeAmount,
    @SerialName("dispute_outcome") val disputeOutcome: DisputeOutcome? = null,
    @SerialName("dispute_life_cycle_stage") val disputeLifeCycleStage: String,
    @SerialName("dispute_channel") val disputeChannel: String,
    val messages: List<Message> = emptyList(),
    val extensions: Extensions,
    val offer: Offer,
    val links: List<Link>
)

@Serializable
data class DisputedTransaction(
    @SerialName("seller_transaction_id") val sellerTransactionId: String,
    @SerialName("create_time") val createTime: String,
    @SerialName("transaction_status") val transactionStatus: String,
    @SerialName("gross_amount") val grossAmount: GrossAmount,
    @SerialName("custom")
    val orderId: String? = null,
    val buyer: Buyer,
    val seller: Seller
)

@Serializable
data class GrossAmount(
    @SerialName("currency_code") val currencyCode: String,
    val value: String
)

@Serializable
data class Buyer(
    val name: String
)

@Serializable
data class Seller(
    val email: String,
    @SerialName("merchant_id") val merchantId: String,
    val name: String
)

@Serializable
data class DisputeOutcome(
    @SerialName("outcome_code") val outcomeCode: String,
    @SerialName("amount_refunded") val amountRefunded: AmountRefunded? = null
)

@Serializable
data class AmountRefunded(
    @SerialName("currency_code") val currencyCode: String,
    val value: String
)


@Serializable
data class Extensions(
    @SerialName("merchandize_dispute_properties") val merchandizeDisputeProperties: MerchandizeDisputeProperties? = null
)

@Serializable
data class MerchandizeDisputeProperties(
    @SerialName("issue_type") val issueType: String,
    @SerialName("service_details") val serviceDetails: ServiceDetails
)

@Serializable
data class ServiceDetails(
    @SerialName("sub_reasons") val subReasons: List<String>,
    @SerialName("purchase_url") val purchaseUrl: String? = null
)

@Serializable
data class Offer(
    @SerialName("buyer_requested_amount") val buyerRequestedAmount: DisputeAmount,
    @SerialName("offer_type") val offerType: String? = null,
    val history: OfferHistory? = null
)

@Serializable
data class OfferHistory(
    @SerialName("offer_time") val offerTime: String,
    val actor: String,
    @SerialName("event_type") val eventType: String,
    @SerialName("offer_amount") val offerAmount: DisputeAmount,
    val notes: String,
    @SerialName("dispute_life_cycle_stage") val disputeLifeCycleStage: String
)

public object LocalDateTimePaypalSerializer: KSerializer<LocalDateTime> {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(formatter.format(value.toJavaLocalDateTime()))
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return java.time.LocalDateTime.parse(decoder.decodeString(), formatter).toKotlinLocalDateTime()
    }
}
