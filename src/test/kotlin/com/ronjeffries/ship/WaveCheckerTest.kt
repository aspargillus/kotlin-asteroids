package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class WaveCheckerTest {
    @Test
    fun `checker returns nothing when elapsed lt 1`() {
        val ck = WaveChecker()
        ck.update(0.5)
        ck.beginInteraction()
        val trans = ck.finishInteraction()
        assertThat(trans.adds).isEmpty()
        assertThat(trans.removes).isEmpty()
        assertThat(ck.elapsedTime).isEqualTo(0.5)
        val toCreate = ck.update(0.1)
        assertThat(toCreate).isEmpty() // always is, don't check again
    }

    @Test
    fun `returns WaveMaker when elapsed gt 1 and no asteroid scanned`() {
        val ck = WaveChecker()
        ck.update(1.1)
        ck.beginInteraction()
        val trans = ck.finishInteraction()
        assertThat(trans.adds.toList()[0]).isInstanceOf(WaveMaker::class.java)
        assertThat(ck.elapsedTime).isEqualTo(-5.0)
    }

    @Test
    fun `returns empty when elapsed gt 1 and an asteroid IS scanned`() {
        val a = SolidObject.asteroid(U.randomPoint(), U.randomVelocity(1000.0))
        val ck = WaveChecker()
        ck.update(1.1)
        ck.beginInteraction()
        ck.interactWith(a)
        val trans = ck.finishInteraction()
        assertThat(trans.adds).isEmpty()
        assertThat(trans.removes).isEmpty()
        assertThat(ck.elapsedTime).isEqualTo(0.0)
    }
}