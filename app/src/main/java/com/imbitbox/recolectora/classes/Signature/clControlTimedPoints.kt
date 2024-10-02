package com.imbitbox.recolectora.classes.Signature

internal data class clControlTimedPoints(
    var c1: clTimedPoint,
    var c2: clTimedPoint
) {
    fun set(c1: clTimedPoint, c2: clTimedPoint): clControlTimedPoints {
        this.c1 = c1
        this.c2 = c2
        return this
    }
}