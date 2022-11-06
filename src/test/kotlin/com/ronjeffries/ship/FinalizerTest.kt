package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class FinalizerTest {
    @Test
    fun `asteroid finalizer`() {
        val fin = AsteroidFinalizer()
        val asteroid = Flyer.asteroid(Point.ZERO, Velocity.ZERO)
        val splits = fin.finalize(asteroid)
        assertThat(splits.size).isEqualTo(3) // split guys and a score
    }
}