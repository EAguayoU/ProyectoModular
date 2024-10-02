package com.imbitbox.recolectora.classes.Signature

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.imbitbox.recolectora.config.Parcelize

@Parcelize
data class clEvent(
    val timestamp: Long,
    val action: Int,
    val x: Float,
    val y: Float,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readInt(),
        parcel.readFloat(),
        parcel.readFloat()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(timestamp)
        parcel.writeInt(action)
        parcel.writeFloat(x)
        parcel.writeFloat(y)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<clEvent> {
        override fun createFromParcel(parcel: Parcel): clEvent {
            return clEvent(parcel)
        }

        override fun newArray(size: Int): Array<clEvent?> {
            return arrayOfNulls(size)
        }
    }
}