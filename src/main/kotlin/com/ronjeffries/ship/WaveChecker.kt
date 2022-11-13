package com.ronjeffries.ship

class WaveChecker: ISpaceObject {
    var sawAsteroid = false
    override var elapsedTime = 0.0

    override fun beginInteraction() {
        sawAsteroid = false
    }

    override fun interactWith(other: ISpaceObject): List<ISpaceObject> {
        if (other is SolidObject && other.isAsteroid)
            sawAsteroid = true
        return emptyList()
    }

    override fun interactWithOther(other: ISpaceObject): List<ISpaceObject> {
        return this.interactWith(other)
    }

    override fun finishInteraction(): Pair<List<ISpaceObject>, Set<ISpaceObject>> {
        if ( elapsedTime > 1.0  ) {
            elapsedTime = 0.0
            if (!sawAsteroid) {
                elapsedTime = -5.0
                return Pair(listOf(WaveMaker(1)), emptySet())
            }
        }
        return Pair(emptyList(), emptySet())
    }

    override fun update(deltaTime: Double): List<ISpaceObject> {
        elapsedTime += deltaTime
        return emptyList()
    }
}
