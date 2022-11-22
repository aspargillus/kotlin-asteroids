package com.ronjeffries.ship

import org.openrndr.draw.Drawer

class ShipChecker(val ship: SolidObject) : SpaceObject() {
    private var missingShip = true
    override val lifetime
        get() = Double.MAX_VALUE

    override fun finalize(): List<SpaceObject> { return emptyList() }

    override fun beginInteraction() {
        missingShip = true
    }

    override fun interactWith(other: SpaceObject): List<SpaceObject> {
        if ( other == ship ) missingShip = false
        return emptyList()
    }

    override fun finishInteraction(trans: Transaction) {
        if ( missingShip ) {
            trans.add(ShipMaker(ship))
            trans.remove(this)
        }
    }

    override fun draw(drawer: Drawer) {}
}
