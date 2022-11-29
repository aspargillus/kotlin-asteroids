package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import kotlin.random.Random

private val SaucerPoints = listOf(
    Point(-2.0, 1.0),
    Point(2.0, 1.0),
    Point(5.0, -1.0),
    Point(-5.0, -1.0),
    Point(-2.0, -3.0),
    Point(2.0, -3.0),
    Point(5.0, -1.0),
    Point(2.0, 1.0),
    Point(1.0, 3.0),
    Point(-1.0, 3.0),
    Point(-2.0, 1.0),
    Point(-5.0, -1.0),
    Point(-2.0, 1.0)
)

private val Directions = listOf(
    Velocity(1.0, 0.0),
    Velocity(0.7071, 0.7071),
    Velocity(0.7071, -0.7071)
)

class Saucer : ISpaceObject, InteractingSpaceObject, Collider {
    override lateinit var position: Point
    override val killRadius = 100.0

    private var direction: Double
    lateinit var velocity: Velocity
    private val speed = 1500.0
    private var elapsedTime = 0.0

    init {
        direction = -1.0
        wakeUp()
    }

    private fun wakeUp() {
        direction = -direction
        position = Point(0.0, Random.nextDouble(U.UNIVERSE_SIZE))
        velocity = Velocity(direction, 0.0) * speed
    }

    override val subscriptions = Subscriptions(
        draw = this::draw,
        interactWithAsteroid = { asteroid, trans ->
            checkCollision(asteroid, trans)
        },
        interactWithShip = { ship, trans ->
            checkCollision(ship, trans)
        },
        interactWithMissile = { missile, trans ->
            checkCollision(missile, trans)
        },
    )

    private fun checkCollision(asteroid: Collider, trans: Transaction) {
        if (Collision(asteroid).hit(this)) trans.remove(this)
    }

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        other.subscriptions.interactWithSaucer(this, trans)
    }

    override fun update(deltaTime: Double, trans: Transaction) {
        elapsedTime += deltaTime
        if (elapsedTime > 1.5) {
            elapsedTime = 0.0
            zigZag()
        }
        position = (position + velocity * deltaTime).cap()
    }

    fun zigZag() {
        velocity = newDirection(Random.nextInt(3)) * speed * direction
    }

    override fun finalize(): List<ISpaceObject> {
        wakeUp()
        return emptyList()
    }

    fun newDirection(direction: Int): Velocity {
        return when (direction) {
            0, 1, 2 -> Directions[direction]
            else -> Directions[0]
        }
    }

    fun draw(drawer: Drawer) {
        drawer.translate(position)
        drawer.stroke = ColorRGBa.GREEN
        drawer.fill = ColorRGBa.GREEN
        val sc = 45.0
        drawer.scale(sc, -sc)
        drawer.strokeWeight = 8.0 / sc
        drawer.lineStrip(SaucerPoints)
    }
}
