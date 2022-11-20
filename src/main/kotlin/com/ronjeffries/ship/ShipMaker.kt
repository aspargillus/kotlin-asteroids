package com.ronjeffries.ship

class ShipMaker(val ship: SolidObject) : BaseObject() {
    var safeToEmerge = true
    var asteroidTally = 0

    override val interactions: InteractionStrategy = EagerInteractor(
        this::beforeInteractions,
        this::afterInteractions,
        this::interact
    )

    fun beforeInteractions() {
        safeToEmerge = true
        asteroidTally = 0
    }

    fun interact(other: SpaceObject): List<SpaceObject> {
        if (other is SolidObject && other.isAsteroid) asteroidTally += 1
        if (tooClose(other)) safeToEmerge = false
        return emptyList()
    }

    fun interactWithOther(other: SpaceObject): List<SpaceObject> = interactions.interactWith(other)

    private fun tooClose(other: SpaceObject): Boolean {
        return if (other !is SolidObject) false
        else (ship.position.distanceTo(other.position) < U.SAFE_SHIP_DISTANCE)
    }

    fun afterInteractions(): Transaction {
        return if (elapsedTime > U.MAKER_DELAY && safeToEmerge) {
            replaceTheShip()
        } else {
            Transaction()
        }
    }

    private fun replaceTheShip(): Transaction {
        return Transaction().also {
            it.add(ship)
            it.add(ShipChecker(ship))
            it.remove(this)
            it.accumulate(Transaction.hyperspaceEmergence(ship, asteroidTally))
        }
    }
}
