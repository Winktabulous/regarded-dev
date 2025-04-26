package com.near_reality.tools.collections

import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.superclasses

/**
 * Represents a tree data structure that can infer hierarchy irrespective of the order that nodes area added to it.
 *
 * @author Stan van der Bend
 */
class Tree<T : Any>(private val rootSuperClass: KClass<T>) {

    private val nodeMap = mutableMapOf<KClass<out T>, Node<T>>()
    private val pendingNodes = mutableMapOf<KClass<*>, MutableList<Node<T>>>()

    fun add(clazz: KClass<out T>) {

        if (clazz.isAbstract || clazz.isSealed)
            error("Class must not be abstract or sealed ($clazz)")

        if (!clazz.isSubclassOf(rootSuperClass))
            error("Class must be subclass of $rootSuperClass")

        val node = Node(clazz)

        nodeMap[clazz] = node

        var nonRootSuperClass = clazz.findSuperClass()
        while (nonRootSuperClass != rootSuperClass && (nonRootSuperClass.isAbstract || nonRootSuperClass.isSealed))
            nonRootSuperClass = nonRootSuperClass.findSuperClass()

        if (nonRootSuperClass != rootSuperClass) {
            val parent = nodeMap[nonRootSuperClass]
            if (parent != null)
                parent.addChild(node)
            else
                pendingNodes
                    .getOrPut(nonRootSuperClass) { mutableListOf() }
                    .add(node)
        }
    }

    private fun KClass<*>.findSuperClass() =
        superclasses.first { it.isSubclassOf(rootSuperClass) }

    fun resolvePendingNodes() {
        for ((superClass, nodes) in pendingNodes) {
            val parent = nodeMap[superClass] ?: error("Missing node for super $superClass")
            for (child in nodes)
                parent.addChild(child)
        }
        pendingNodes.clear()
    }

    fun getRootNodes(): List<Node<T>> {
        val values = nodeMap.values.toMutableList()
        values.removeIf { it.parent != null }
        return values
    }

    class Node<T : Any>(var value: KClass<out T>) {

        var parent: Node<T>? = null

        var children: MutableList<Node<T>> = mutableListOf()

        fun addChild(node: Node<T>) {
            children.add(node)
            node.parent = this
        }

        override fun toString(): String {
            val buffer = StringBuilder(50)
            print(buffer, "", "")
            return buffer.toString()
        }

        private fun print(buffer: StringBuilder, prefix: String, childrenPrefix: String) {
            buffer.append(prefix)
            buffer.append(value.simpleName)
            buffer.append('\n')
            val it = children.iterator()
            while (it.hasNext()) {
                val next = it.next()
                if (it.hasNext()) {
                    next.print(buffer, "$childrenPrefix├── ", "$childrenPrefix│   ")
                } else {
                    next.print(buffer, "$childrenPrefix└── ", "$childrenPrefix    ")
                }
            }
        }
    }
}
