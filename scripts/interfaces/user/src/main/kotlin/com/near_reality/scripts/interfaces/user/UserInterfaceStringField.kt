package com.near_reality.scripts.interfaces.user

/**
 * @author Jire
 */
class UserInterfaceStringField(
    override val handler: UserInterfaceHandler,
    private val default: String = "$**DEFAULT**$"
) : UserInterfaceField {

    private var value = default

    internal infix fun matches(value: String?) = this.value == default || this.value == value

    operator fun invoke(value: String) {
        check(default != value) { "Value (\"$value\") must not be default (\"$default\")" }

        this@UserInterfaceStringField.value = value
    }

    operator fun invoke(
        value: String,
        build: UserInterfaceHandler.() -> Unit
    ) {
        invoke(value)

        handler.child(build)
    }

    operator fun set(
        value: String,
        handle: UserInterfaceClickEvent.() -> Unit
    ) {
        invoke(value)
        handler.handle(handle)
    }

}