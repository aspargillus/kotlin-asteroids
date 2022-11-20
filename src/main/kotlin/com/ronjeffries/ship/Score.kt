package com.ronjeffries.ship

class Score(override val score: Int) : BaseObject() {
    override val interactions: InteractionStrategy = ShyInteractor()

    override fun interactWith(other: SpaceObject): List<SpaceObject> {
        return emptyList()
    }

}
