package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2

class UniverseTest {

    @Test
    fun `collision calculation`() {
        val ship = SolidObject(
            Vector2.ZERO,
            Vector2.ZERO,
            100.0
        )
        val asteroid = SolidObject(
            position = Vector2.ZERO,
            velocity = Vector2.ZERO,
            killRadius = 1000.0
        )
        assertThat(ship.interactWith(asteroid).size).describedAs("on top").isEqualTo(2)
        val tooFar = Vector2(ship.killRadius + asteroid.killRadius + 1, 0.0)
        var rotated = tooFar.rotate(37.0)
        ship.position = rotated
        assertThat(ship.interactWith(asteroid).size).describedAs("too far").isEqualTo(0)
        val closeEnough = Vector2(ship.killRadius + asteroid.killRadius - 1, 0.0)
        rotated = closeEnough.rotate(37.0)
        ship.position = rotated
        assertThat(ship.interactWith(asteroid).size).describedAs("too close").isEqualTo(2)
    }
}