package com.ronjeffries.ship

import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated

class Game {
    val knownObjects = SpaceObjectCollection()
    val addsFromUpdates = mutableListOf<ISpaceObject>()
    private var lastTime = 0.0

    fun add(newObject: ISpaceObject) = knownObjects.add(newObject)

    fun colliders() = knownObjects.collectFromPairs { f1, f2 -> f1.interactWith(f2) }

    fun createContents(controls: Controls) {
        val ship = newShip(controls)
        add(ship)
        add(ShipMonitor(ship))
        add(ScoreKeeper())
        add(LifetimeClock())
        add(WaveChecker())
    }

    private fun newShip(controls: Controls): SolidObject {
        return  SolidObject.ship(U.CENTER_OF_UNIVERSE, controls)
    }

    fun cycle(drawer: Drawer, seconds: Double) {
        val deltaTime = seconds - lastTime
        lastTime = seconds
        update(deltaTime)
        processInteractions()
        draw(drawer)
    }

    private fun draw(drawer: Drawer) = knownObjects.forEach {drawer.isolated { it.draw(drawer) } }

    fun processInteractions() {
        val toBeRemoved = colliders()
        if ( toBeRemoved.size > 0 ) {
            knownObjects.removeAll(toBeRemoved)
        }
        for (removedObject in toBeRemoved) {
            val addedByFinalize = removedObject.finalize()
            knownObjects.addAll(addedByFinalize)
        }
    }

    fun update(deltaTime: Double) {
        knownObjects.addAll(addsFromUpdates)
        addsFromUpdates.clear()
        knownObjects.forEach { addsFromUpdates.addAll(it.update(deltaTime)) }
    }
}
