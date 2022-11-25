package com.ronjeffries.ship

class SpaceObjectCollection {
    val spaceObjects = mutableListOf<ISpaceObject>()

    fun add(spaceObject: ISpaceObject) {
        spaceObjects.add(spaceObject)
    }

    fun addAll(newbies: Collection<ISpaceObject>) {
        spaceObjects.addAll(newbies)
    }

    fun applyChanges(transaction: Transaction) {
        transaction.applyChanges(this)
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

    fun removeAndFinalizeAll(moribund: Set<ISpaceObject>) {
        moribund.forEach { spaceObjects += it.finalize() }
        spaceObjects.removeAll(moribund)
    }

    val size get() = spaceObjects.size
}
