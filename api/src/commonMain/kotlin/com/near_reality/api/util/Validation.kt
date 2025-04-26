package com.near_reality.api.util

import kotlinx.serialization.Serializable

const val validNameCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789- "
const val validPasswordCharacters =
    "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"ï¿½$%^&*()-_=+[{]};:'@#~,<.>/?\\| "

private val emailRegex = ("^(.+)@(.+)\$").toRegex()

val usernameValidator: (String) -> UsernameValidationResult = {
    when {
        it.length !in 1..12 ->
            UsernameValidationResult.InvalidLength

        it.startsWith(" ") || it.endsWith(" ") ->
            UsernameValidationResult.TrailingWhitespace

        it.any { char -> !validNameCharacters.contains(char) } ->
            UsernameValidationResult.InvalidCharacter

        else ->
            UsernameValidationResult.Valid
    }
}

val usernameSanitizer: (String) -> String = {
    it.trim().filter(validNameCharacters::contains)
}

val passwordValidator: (String) -> PasswordValidationResult = {
    when {
        it.length !in 5..12 ->
            PasswordValidationResult.InvalidLength

        it.any { char -> !validPasswordCharacters.contains(char) } ->
            PasswordValidationResult.InvalidCharacter

        else ->
            PasswordValidationResult.Valid
    }
}

val emailValidator: (String) -> EmailValidationResult = {
    if (emailRegex.matches(it))
        EmailValidationResult.Valid
    else
        EmailValidationResult.Invalid
}

@Serializable
sealed class UsernameValidationResult(val message: String) {
    @Serializable
    data object TrailingWhitespace : UsernameValidationResult("Username cannot start or end with a space.")
    @Serializable
    data object InvalidLength : UsernameValidationResult("Username must be between 1 and 12 characters long.")
    @Serializable
    data object InvalidCharacter : UsernameValidationResult("Username can only contain letters, numbers, hyphens and spaces.")
    @Serializable
    data object Valid : UsernameValidationResult("Username is valid.")
}

@Serializable
sealed class PasswordValidationResult(val message: String) {
    @Serializable
    data object InvalidLength : PasswordValidationResult("Password must be between 5 and 12 characters long.")
    @Serializable
    data object InvalidCharacter : PasswordValidationResult("Password can only contain letters, numbers and special characters.")
    @Serializable
    data object PasswordsAreEqual : PasswordValidationResult("Old password matches new password.")
    @Serializable
    data object Valid : PasswordValidationResult("Password is valid.")
}

@Serializable
sealed class EmailValidationResult(val message: String) {
    @Serializable
    data object Invalid : EmailValidationResult("Email is invalid.")
    @Serializable
    data object Valid : EmailValidationResult("Email is valid.")
}
