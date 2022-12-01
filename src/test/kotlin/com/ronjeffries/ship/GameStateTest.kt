package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class GameStateTest {

    val adds = Transaction()
    val removes = Transaction()
    val state = GameState()

    @Test
    fun `applyChanges with asteroid`() {
        val asteroid = Asteroid(U.randomPoint())
        adds.add(asteroid)
        state.applyChanges(adds)
        assertThat(state.objects.asteroids).containsExactly(asteroid)
        removes.remove(asteroid)
        state.applyChanges(removes)
        assertThat(state.objects.asteroids).isEmpty()
    }

    @Test
    fun `applyChanges with missile`() {
        val ship = Ship(Point.ZERO)
        val missile = Missile(ship)
        adds.add(missile)
        state.applyChanges(adds)
        assertThat(state.objects.missiles).containsExactly(missile)
        removes.remove(missile)
        state.applyChanges(removes)
        assertThat(state.objects.missiles).isEmpty()
    }

    @Test
    fun `applyChanges with splat`() {
        val ship = Ship(Point.ZERO)
        val splat = Splat(ship)
        adds.add(splat)
        state.applyChanges(adds)
        assertThat(state.objects.splats).containsExactly(splat)
        removes.remove(splat)
        state.applyChanges(removes)
        assertThat(state.objects.splats).isEmpty()
    }

    @Test
    fun `applyChanges with score`() {
        adds.score = 100
        state.applyChanges(adds)
        assertThat(state.totalScore).isEqualTo(100)
    }

}