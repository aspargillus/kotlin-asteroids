package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class InteractionsTest {
    @Test
    fun `beginInteractions should notify all objects`() {
        val object1 = object : ISpaceObject {
            var beforeInteractionsCalled: Boolean = false

            override fun update(deltaTime: Double, trans: Transaction) {
                TODO("Do not call this!")
            }

            override val subscriptions: Subscriptions = Subscriptions(
                beforeInteractions = { beforeInteractionsCalled = true }
            )

            override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
                TODO("Not yet implemented")
            }
        }
        val object2 = object : ISpaceObject {
            var beforeInteractionsCalled: Boolean = false

            override fun update(deltaTime: Double, trans: Transaction) {
                TODO("Do not call this!")
            }

            override val subscriptions: Subscriptions = Subscriptions(
                beforeInteractions = { beforeInteractionsCalled = true }
            )

            override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
                TODO("Not yet implemented")
            }
        }
        val knownObjects = SpaceObjectCollection().also { it.addAll(listOf(object1, object2)) }

        val interactions = Interactions(knownObjects)
        interactions.beginInteractions()

        assertThat(object1.beforeInteractionsCalled).isTrue
        assertThat(object2.beforeInteractionsCalled).isTrue
    }
}

class Interactions(private val knownObjects: SpaceObjectCollection) {
    fun beginInteractions() {
        knownObjects.forEach { it.subscriptions.beforeInteractions() }
    }
}
