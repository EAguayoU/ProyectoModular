package com.imbitbox.recolectora.classes.Signature

import android.os.Parcel
import android.os.Parcelable
import com.imbitbox.recolectora.config.Parcelize

@Parcelize
data class clSignature(
    val versionCode: Int,
    val events: List<clEvent>,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.createTypedArrayList(clEvent)!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(versionCode)
        parcel.writeTypedList(events)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<clSignature> {
        override fun createFromParcel(parcel: Parcel): clSignature {
            return clSignature(parcel)
        }

        override fun newArray(size: Int): Array<clSignature?> {
            return arrayOfNulls(size)
        }
    }
}

