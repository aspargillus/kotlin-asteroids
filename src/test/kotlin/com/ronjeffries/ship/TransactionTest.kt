package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TransactionTest {

    private val transaction = Transaction()

    @Test
    fun `transaction adds sorts types`() {
        val asteroid = Asteroid(U.randomPoint())
        val ship = Ship(U.randomPoint())
        val splat = Splat(ship)
        val missile = Missile(ship)
        transaction.add(asteroid)
        transaction.add(splat)
        transaction.add(missile)
        assertThat(transaction.adds.asteroids).containsExactly(asteroid)
        assertThat(transaction.adds.splats).containsExactly(splat)
        assertThat(transaction.adds.missiles).containsExactly(missile)
    }

    @Test
    fun `transaction removes sorts types`() {
        val asteroid = Asteroid(U.randomPoint())
        val ship = Ship(U.randomPoint())
        val splat = Splat(ship)
        val missile = Missile(ship)
        transaction.remove(asteroid)
        transaction.remove(splat)
        transaction.remove(missile)
        assertThat(transaction.removes.asteroids).containsExactly(asteroid)
        assertThat(transaction.removes.splats).containsExactly(splat)
        assertThat(transaction.removes.missiles).containsExactly(missile)
    }
}