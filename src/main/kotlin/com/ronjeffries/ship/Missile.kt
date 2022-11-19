package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE

class Missile(ship: Ship) : Drawable, SolidObject(
    position = ship.position + Point(2 * (ship.killRadius + 10.0), 0.0).rotate(ship.heading),
    velocity = ship.velocity + Velocity(U.SPEED_OF_LIGHT / 3.0, 0.0).rotate(ship.heading),
    killRadius = 10.0,
    lifetime = 3.0,
    view = MissileView(10.0),
    finalizer = MissileFinalizer()
) {
    override fun draw(drawer: Drawer) {
        drawer.fill = ColorRGBa.MEDIUM_SLATE_BLUE
        drawer.translate(position)
        view.draw(drawer, heading, elapsedTime)
    }

}