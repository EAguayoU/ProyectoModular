package com.imbitbox.recolectora.classes.Signature

import kotlin.math.sqrt

internal data class clBezier(
    var startPoint: clTimedPoint,
    var control1: clTimedPoint,
    var control2: clTimedPoint,
    var endPoint: clTimedPoint,
) {

    operator fun set(
        startPoint: clTimedPoint,
        control1: clTimedPoint,
        control2: clTimedPoint,
        endPoint: clTimedPoint
    ): clBezier {
        this.startPoint = startPoint
        this.control1 = control1
        this.control2 = control2
        this.endPoint = endPoint
        return this
    }

    @SuppressWarnings("MagicNumber")
    fun length(): Float {
        val steps = 10
        var length = 0f
        var cx: Double
        var cy: Double
        var px = 0.0
        var py = 0.0
        var xDiff: Double
        var yDiff: Double
        for (i in 0..steps) {
            val t = i.toFloat() / steps
            cx = point(
                t, startPoint.x, control1.x,
                control2.x, endPoint.x
            )
            cy = point(
                t, startPoint.y, control1.y,
                control2.y, endPoint.y
            )
            if (i > 0) {
                xDiff = cx - px
                yDiff = cy - py
                length += sqrt(xDiff * xDiff + yDiff * yDiff).toFloat()
            }
            px = cx
            py = cy
        }
        return length
    }

    @SuppressWarnings("MagicNumber")
    fun point(t: Float, start: Float, c1: Float, c2: Float, end: Float): Double {
        return start * (1.0 - t) * (1.0 - t) * (1.0 - t) + 3.0 * c1 * (1.0 - t) * (1.0 - t) * t +
                3.0 * c2 * (1.0 - t) * t * t + end * t * t * t
    }
}
