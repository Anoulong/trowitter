
package com.anou.prototype.core.db.about

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

import com.google.gson.annotations.SerializedName


@Entity(tableName = "About")
class AboutEntity(
        @SerializedName("_id")
        var id: String? = null,

        @PrimaryKey
        var eid: String,

        @SerializedName("custom_module_eid")
        var customModuleEid: String? = null,

        var content: String? = null,

        @SerializedName("_active")
        var isActive: Boolean = false,

        @SerializedName("__v")
        var version: String? = null,

        @SerializedName("created_at")
        var createdAt: String? = null,

        @SerializedName("updated_at")
        var updatedAt: String? = null,

        var lang: String? = null) : Parcelable {

        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readByte() != 0.toByte(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(id)
                parcel.writeString(eid)
                parcel.writeString(customModuleEid)
                parcel.writeString(content)
                parcel.writeByte(if (isActive) 1 else 0)
                parcel.writeString(version)
                parcel.writeString(createdAt)
                parcel.writeString(updatedAt)
                parcel.writeString(lang)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<AboutEntity> {
                override fun createFromParcel(parcel: Parcel): AboutEntity {
                        return AboutEntity(parcel)
                }

                override fun newArray(size: Int): Array<AboutEntity?> {
                        return arrayOfNulls(size)
                }
        }


}
