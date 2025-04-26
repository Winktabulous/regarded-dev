package com.near_reality.scripts.interfaces.user

import it.unimi.dsi.fastutil.objects.ObjectArrayList

/**
 * @author Jire
 */
class UserInterfaceHandler(
    val interfaceID: Int,
    val parent: UserInterfaceHandler? = null
) {

    private var orHandler: UserInterfaceHandler? = null

    @PublishedApi
    internal val children: MutableList<UserInterfaceHandler> = ObjectArrayList()

    val component = UserInterfaceIntField(this)
    val slot = UserInterfaceIntField(this)
    val item = UserInterfaceIntField(this)
    val optionID = UserInterfaceIntField(this)
    val option = UserInterfaceStringField(this)

    private var handler: (UserInterfaceClickEvent.() -> Unit)? = null

    fun handle(handler: UserInterfaceClickEvent.() -> Unit) {
        this.handler = handler
    }

    fun child(build: UserInterfaceHandler.() -> Unit) {
        val child = UserInterfaceHandler(interfaceID, this).apply(build)
        children.add(child)
    }

    internal fun handle(event: UserInterfaceClickEvent): Boolean {
        val handler = this.handler
        if (handler != null
            && this.component matches event.componentID
            && this.slot matches event.slotID
            && this.item matches event.itemID
            && this.optionID matches event.optionID
            && this.option matches event.option
        ) {
            event.handler()
            return true
        }

        for (i in 0..children.lastIndex) {
            val child = children[i]
            if (child.handle(event)) {
                return true
            }
        }
        return false
    }

}