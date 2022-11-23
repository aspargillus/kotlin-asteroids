package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer

class ScoreKeeper: ISpaceObject {
    var totalScore = 0
    override val lifetime
        get() = Double.MAX_VALUE

    override fun finalize(): List<ISpaceObject> { return emptyList() }

    fun formatted(): String {
        return ("00000" + totalScore.toShort()).takeLast(5)
    }

    override fun interactWith(other: ISpaceObject): List<ISpaceObject> {
        return getScore(other)
    }

    private fun getScore(other: ISpaceObject): List<ISpaceObject> {
        if (other is Score) {
            totalScore += other.score
            return listOf(other)
        }
        return emptyList()
    }

    override fun draw(drawer: Drawer) {
        drawer.translate(100.0, 500.0)
        drawer.stroke = ColorRGBa.GREEN
        drawer.fill = ColorRGBa.GREEN
        drawer.text(formatted(), Point(0.0, 0.0))
    }

    override fun beforeInteractions() {}
    override fun afterInteractions(trans: Transaction) {}
    override fun update(deltaTime: Double, trans: Transaction) {}
}
