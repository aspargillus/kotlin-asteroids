package com.ronjeffries.ship

class GameState {
    val spaceObjects get() = typedObjects.all.toList()
    val typedObjects = TypedObjects()
    var totalScore = 0

    fun applyChanges(transaction: Transaction) {
        transaction.typedRemoves.missiles.forEach { typedObjects.remove(it) }
        transaction.typedRemoves.asteroids.forEach { typedObjects.remove(it) }
        transaction.typedRemoves.splats.forEach { typedObjects.remove(it) }
        transaction.typedRemoves.others.forEach { typedObjects.remove(it) }

        transaction.typedAdds.missiles.forEach { typedObjects.add(it) }
        transaction.typedAdds.asteroids.forEach { typedObjects.add(it) }
        transaction.typedAdds.splats.forEach { typedObjects.add(it) }
        transaction.typedAdds.others.forEach { typedObjects.add(it) }
        totalScore += transaction.score
    }

    fun forEach(spaceObject: (ISpaceObject) -> Unit) = spaceObjects.forEach(spaceObject)

    fun pairsToCheck(): List<Pair<ISpaceObject, ISpaceObject>> {
        val pairs = mutableListOf<Pair<ISpaceObject, ISpaceObject>>()
        spaceObjects.indices.forEach { i ->
            spaceObjects.indices.minus(0..i).forEach { j ->
                pairs.add(spaceObjects[i] to spaceObjects[j])
            }
        }
        return pairs
    }

    val size get() = spaceObjects.size
}