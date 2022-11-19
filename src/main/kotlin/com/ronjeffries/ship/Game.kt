package com.ronjeffries.ship

import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated

class Game {
    val knownObjects = SpaceObjectCollection()
    val addsFromUpdates = mutableListOf<SpaceObject>()
    private var lastTime = 0.0

    fun add(newObject: SpaceObject) = knownObjects.add(newObject)

    fun colliders() = knownObjects.collectFromPairs { f1, f2 -> f1.interactWith(f2) }

    fun createContents(controlFlags: ControlFlags) {
        val ship = newShip(controlFlags)
        add(ship)
        add(ShipChecker(ship))
        add(ScoreKeeper())
        add(LifetimeClock())
        add(WaveChecker())
    }

    private fun newShip(controlFlags: ControlFlags): SolidObject {
        val controls = Controls(controlFlags)
        return Ship(U.CENTER_OF_UNIVERSE, controls)
    }

    fun cycle(drawer: Drawer, seconds: Double) {
        val deltaTime = seconds - lastTime
        lastTime = seconds
        tick(deltaTime)
        beginInteractions()
        processInteractions()
        finishInteractions()
        draw(drawer)
    }

    private fun beginInteractions() {
        knownObjects.forEach { it.beginInteraction() }
    }

    private fun finishInteractions() {
        val buffer = Transaction()
        knownObjects.forEach {
            val result: Transaction = it.finishInteraction()
            buffer.accumulate(result)
        }
        knownObjects.applyChanges(buffer)
    }

    private fun draw(drawer: Drawer) = knownObjects.drawables.forEach { drawer.isolated { it.draw(drawer) } }

    fun processInteractions() {
        val toBeRemoved = colliders()
        if (toBeRemoved.size > 0) {
            knownObjects.removeAll(toBeRemoved)
        }
        for (removedObject in toBeRemoved) {
            val addedByFinalize = removedObject.finalize()
            knownObjects.addAll(addedByFinalize)
        }
    }

    fun tick(deltaTime: Double) {
        knownObjects.addAll(addsFromUpdates)
        addsFromUpdates.clear()
        knownObjects.forEach { addsFromUpdates.addAll(it.tick(deltaTime)) }
    }
}
