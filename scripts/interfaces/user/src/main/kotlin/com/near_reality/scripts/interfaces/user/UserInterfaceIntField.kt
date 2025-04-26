package com.near_reality.scripts.interfaces.user

/**
 * @author Jire
 */
class UserInterfaceIntField(
    override val handler: UserInterfaceHandler,
    private val default: Int = Int.MIN_VALUE
) : UserInterfaceField {

    private var value = default

    internal infix fun matches(value: Int) = this.value == default || this.value == value

    operator fun invoke(value: Int) {
        check(default != value) { "Value ($value) must not be default ($default)" }

        this@UserInterfaceIntField.value = value
    }

    operator fun invoke(
        value: Int,
        build: UserInterfaceHandler.() -> Unit
    ) {
        invoke(value)

        handler.child(build)
    }

    operator fun set(
        value: Int,
        handle: UserInterfaceClickEvent.() -> Unit
    ) {
        invoke(value)
        handler.handle(handle)
    }

}