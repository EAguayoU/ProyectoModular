package com.imbitbox.recolectora.classes.Signature

import kotlin.math.pow
import kotlin.math.sqrt

internal class clTimedPoint {
    var x = 0f
    var y = 0f
    var timestamp: Long = 0

    operator fun set(x: Float, y: Float, timestamp: Long): clTimedPoint {
        this.x = x
        this.y = y
        this.timestamp = timestamp
        return this
    }

    fun velocityFrom(start: clTimedPoint): Float {
        var diff = timestamp - start.timestamp
        if (diff <= 0) {
            diff = 1
        }
        var velocity = distanceTo(start) / diff
        if (velocity.isInfinite() || velocity.isNaN()) {
            velocity = 0f
        }
        return velocity
    }

    private fun distanceTo(point: clTimedPoint): Float {
        return sqrt(
            (point.x - x).toDouble().pow(2.0) + (point.y - y).toDouble().pow(2.0)
        ).toFloat()
    }
}