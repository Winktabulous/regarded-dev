package com.near_reality.api.dao

import com.near_reality.api.model.ApiGameMode
import com.near_reality.api.model.ApiMemberRank
import com.near_reality.api.model.ApiPrivilege
import com.near_reality.api.model.Sanction
import com.near_reality.api.model.User
import com.near_reality.api.util.currentTime
import com.near_reality.api.util.formatUsername
import com.near_reality.api.util.minus
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.selectAll
import kotlin.time.Duration


/**
 * Represents a table for user-information of near-reality players.
 *
 * @author Stan van der Bend
 */
object Users : LongIdTable(name = "users") {
    val username = varchar("username", 12).uniqueIndex()
    val name get() = username
    val password = varchar("password", 128)
    val passwordAtRisk = bool("password_at_risk")
    val email =  email("email")
    val privilege = enumeration<ApiPrivilege>("privilege").default(ApiPrivilege.PLAYER)
    val storeCredits = integer("tokens").default(0)
    val totalVotes = integer("votes").default(0)
    val totalSpent = float("total_spent").default(0F)
    val lastLogin = datetime("last_login").nullable().default(null)
    val joinDate = datetime("join_date").clientDefault { currentTime }
    val twoFactorSecret = varchar("two_factor_secret", 50).uniqueIndex().nullable().default(null)
    val twoFactorEnabled = bool("two_factor_activated").default(false)
    /**
     * TODO: currently the id is ordinal + 1, but should just be ordinal, have to refactor the database
     */
    val gameMode = enumeration<ApiGameMode>("game_mode").default(ApiGameMode.REGULAR)
}

class UserEntity(id: EntityID<Long>) : ModelEntity<User>(id) {
    companion object : LongEntityClass<UserEntity>(Users) {
        fun findByUsername(username: String) =
            find { Users.username.lowerCase() eq username.formatUsername() }
                .firstOrNull()
        fun findByUsernameAndEmail(username: String, email: String) =
            find { (Users.username.lowerCase() eq username.formatUsername()) and (Users.email.lowerCase() eq email.lowercase()) }
                .firstOrNull()
        fun findByUsernameOrEmail(username: String, email: String) =
            find { (Users.username.lowerCase() eq username.formatUsername()) or (Users.email.lowerCase() eq email.lowercase()) }
                .firstOrNull()
        fun edit(id: Int, user: User): UserEntity? = findById(id.toLong())?.apply {
            setFrom(user)
            println("boo")
        }
    }

    var username by Users.username
    var email by Users.email
    var password by Users.password
    var passwordAtRisk by Users.passwordAtRisk
    var storeCredits by Users.storeCredits
    var totalSpent by Users.totalSpent
    var totalVotes by Users.totalVotes
    val memberRank get() = ApiMemberRank.findForUserWithAmountSpent(username, totalSpent.toInt())
    var privilege by Users.privilege
    var lastLogin by Users.lastLogin
    var joinDate by Users.joinDate
    var twoFactorSecret by Users.twoFactorSecret
    var twoFactorActivated by Users.twoFactorEnabled
    var gameMode by Users.gameMode

    val ips: Set<String> get() = UserIPs.selectAll().where { (UserIPs.user eq id) and (UserIPs.ip neq "") }.map { it[UserIPs.ip] }.toSet()

    val sanctions: List<Sanction> get() {
        val accountSanctions = AccountSanctionEntity.findFor(this)
        val ipSanctions = IPSanctionEntity.findFor(ips)
        val sanctions = (accountSanctions + ipSanctions)
        val sanctionsByExpired = sanctions.groupBy { it.isExpired() }
        sanctionsByExpired[true]?.forEach { it.delete() }
        return sanctionsByExpired[false]?.map { it.toModel() }?: emptyList()
    }

    fun timeSinceLastVote(site: VoteSiteEntity) =
        Votes
            .selectAll()
            .where { (Votes.site eq site.id) and (Votes.user eq id) }
            .maxOfOrNull { it[Votes.voteTime] }
            ?.let { previous ->
                val current = currentTime
                val since = current - previous
                since.absoluteValue
            }
            ?:Duration.INFINITE

    fun setFrom(user: User) {
        username = user.name
        email = user.email
        user.passwordHash?.apply { password = this}
        storeCredits = user.storeCredits
        totalSpent = user.totalSpent.toFloat()
        totalVotes = user.totalVotes
        privilege = user.privilege
        joinDate = user.joinDate
        twoFactorActivated = user.twoFactorEnabled
        user.twoFactorSecret?.apply { twoFactorSecret = this }
        gameMode = user.gameMode
    }

    override fun toModel(): User = toApiModel()

    fun toApiModel(includeCredentials: Boolean = false) = User(
        id.value,
        username,
        if (includeCredentials) password else null,
        passwordAtRisk,
        email,
        gameMode,
        memberRank,
        privilege,
        twoFactorActivated,
        if (includeCredentials) twoFactorSecret else null,
        totalSpent.toInt(),
        storeCredits,
        totalVotes,
        joinDate,
        sanctions
    )
}


object UserIPs : Table(name = "user_ips") {
    val user = reference("user", Users).index()
    val ip = ip("ip").index()
    override val primaryKey: PrimaryKey = PrimaryKey(user, ip)
}
