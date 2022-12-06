package com.ronjeffries.ship

import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated

class Game(val knownObjects:SpaceObjectCollection = SpaceObjectCollection()) {
    private var lastTime = 0.0

    fun add(newObject: ISpaceObject) = knownObjects.add(newObject)

    fun changesDueToInteractions(): Transaction {
        val trans = Transaction()
        knownObjects.pairsToCheck().forEach { p ->
            p.first.callOther(p.second, trans)
            p.second.callOther(p.first, trans)
        }
        return trans
    }

    fun createContents(controls: Controls) {
        val ship = newShip(controls)
        val scoreKeeper = ScoreKeeper(8)
        add(ShipChecker(ship, scoreKeeper))
        add(scoreKeeper)
        add(WaveMaker(1))
        add(SaucerMaker())
    }

    private fun newShip(controls: Controls): Ship =
        Ship(U.CENTER_OF_UNIVERSE, controls)

    fun cycle(elapsedSeconds: Double, drawer: Drawer? = null) {
        val deltaTime = elapsedSeconds - lastTime
        lastTime = elapsedSeconds
        tick(deltaTime)
        beginInteractions()
        processInteractions()
        finishInteractions()
        drawer?.let {draw(drawer)}
    }

    private fun beginInteractions()
        = knownObjects.forEach { it.subscriptions.beforeInteractions() }

    private fun finishInteractions() {
        val buffer = Transaction()
        knownObjects.forEach { it.subscriptions.afterInteractions(buffer) }
        knownObjects.applyChanges(buffer)
    }

    private fun draw(drawer: Drawer)
        = knownObjects.forEach {drawer.isolated { it.subscriptions.draw(drawer) } }

    fun processInteractions() = knownObjects.applyChanges(changesDueToInteractions())

    fun tick(deltaTime: Double) {
        val trans = Transaction()
        knownObjects.forEach { it.update(deltaTime, trans) }
        knownObjects.applyChanges(trans)
    }
}
