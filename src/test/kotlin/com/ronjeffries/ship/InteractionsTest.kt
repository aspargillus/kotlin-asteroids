package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass

typealias Interaction = (object1: ISpaceObject, object2: ISpaceObject, transaction: Transaction) -> Unit

class InteractionsTest {
    @Test
    fun `beginInteractions should notify all objects`() {
        val object1 = InteractionTestObject()
        val object2 = InteractionTestObject()
        val knownObjects = SpaceObjectCollection().also { it.addAll(listOf(object1, object2)) }

        val interactions = Interactions(knownObjects)
        interactions.beginInteractions()

        assertThat(object1.beforeInteractionsCalled).isTrue
        assertThat(object2.beforeInteractionsCalled).isTrue
    }

    @Test
    fun `interaction lookup yields null by default`() {
        val object1 = InteractionTestObject()
        val object2 = InteractionTestObject()
        val knownObjects = SpaceObjectCollection().also { it.addAll(listOf(object1, object2)) }

        val interactions = Interactions(knownObjects)

        assertThat(interactions.findInteraction(object1, object2)).isNull()
    }

    @Test
    fun `interaction lookup yields previously registered lambda`() {
        val object1 = InteractionTestObject()
        val object2 = InteractionTestObject()
        val knownObjects = SpaceObjectCollection().also { it.addAll(listOf(object1, object2)) }

        val interactions = Interactions(knownObjects)
        val interaction = { _: ISpaceObject, _: ISpaceObject, _: Transaction -> }
        interactions.register(InteractionTestObject::class, InteractionTestObject::class, interaction)

        assertThat(interactions.findInteraction(object1, object2)).isSameAs(interaction)
    }

    @Test
    fun `executing interactions can use transactions`() {
        val object1 = InteractionTestObject()
        val object2 = InteractionTestObject()
        val object3 = InteractionTestObject()
        val knownObjects = SpaceObjectCollection().also { it.addAll(listOf(object1, object2)) }

        val interactions = Interactions(knownObjects)
        interactions.register(InteractionTestObject::class, InteractionTestObject::class) { o1, o2, transaction ->
            transaction.remove(o1)
            transaction.remove(o2)
            transaction.add(object3)
        }
        val transaction = Transaction()
        interactions.executeInteraction(object1, object2, transaction)

        assertThat(transaction.adds).containsExactly(object3)
        assertThat(transaction.removes).containsExactlyInAnyOrder(object1, object2)
    }
}

class InteractionTestObject : ISpaceObject {
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

class Interactions(private val knownObjects: SpaceObjectCollection) {
    private val registeredInteractions =
        mutableMapOf<Pair<KClass<out ISpaceObject>, KClass<out ISpaceObject>>, Interaction>()

    fun beginInteractions() {
        knownObjects.forEach { it.subscriptions.beforeInteractions() }
    }

    fun executeInteraction(object1: ISpaceObject, object2: ISpaceObject, transaction: Transaction) {
        val interaction = findInteraction(object1, object2)
        interaction?.let { it(object1, object2, transaction) }
    }

    fun findInteraction(object1: ISpaceObject,object2: ISpaceObject): (Interaction)? {
        return registeredInteractions[Pair(object1::class, object2::class)]
    }

    fun register(class1: KClass<out ISpaceObject>, class2: KClass<out ISpaceObject>, interaction: Interaction) {
        registeredInteractions[Pair(class1, class2)] = interaction
    }
}
