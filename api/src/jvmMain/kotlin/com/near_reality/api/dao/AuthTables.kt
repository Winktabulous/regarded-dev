package com.near_reality.api.dao

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.or

object RefreshTokens : LongIdTable(name = "auth_refresh_tokens") {
    val token = varchar("token", 128).uniqueIndex()
    val user = reference("user", Users)
    val expires = datetime("expires")
}

class RefreshTokenEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<RefreshTokenEntity>(RefreshTokens)

    var token by RefreshTokens.token
    var user by UserEntity referencedOn RefreshTokens.user
    var expiresAt by RefreshTokens.expires
}

object AccessTokens : LongIdTable(name = "auth_access_tokens") {
    val token = varchar("token", 128).uniqueIndex()
    val user = reference("user", Users)
    val expires = datetime("expires")
}

class AccessTokenEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<AccessTokenEntity>(AccessTokens)

    var token by AccessTokens.token
    var user by UserEntity referencedOn AccessTokens.user
    var expires by AccessTokens.expires
}

object PasswordResetTokens : LongIdTable(name = "auth_password_reset_tokens") {
    val token = varchar("token", 128).uniqueIndex()
    val user = reference("user", Users)
    val expires = datetime("expires")
}

class PasswordResetTokenEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<PasswordResetTokenEntity>(PasswordResetTokens)

    var token by PasswordResetTokens.token
    var user by UserEntity referencedOn PasswordResetTokens.user
    var expires by PasswordResetTokens.expires
}

object RegistrationTokens : LongIdTable(name = "auth_registration_tokens") {
    val username = username("username").uniqueIndex()
    val email = email("email").uniqueIndex()
    val password = varchar("password", 128)
    val token = varchar("token", 128).uniqueIndex()
    val expires = datetime("expires")
}

class RegistrationTokenEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<RegistrationTokenEntity>(RegistrationTokens) {
        fun findByUsername(username: String) = find { RegistrationTokens.username.lowerCase() eq username.lowercase() }.firstOrNull()
        fun findByEmail(email: String) = find { RegistrationTokens.email.lowerCase() eq email.lowercase() }.firstOrNull()
        fun findByUsernameOrEmail(username: String, email: String) = find {
            (RegistrationTokens.username.lowerCase() eq username.lowercase()) or
                    (RegistrationTokens.email.lowerCase() eq email.lowercase())
        }.firstOrNull()
    }
    var username by RegistrationTokens.username
    var email by RegistrationTokens.email
    var password by RegistrationTokens.password
    var token by RegistrationTokens.token
    var expires by RegistrationTokens.expires
}
