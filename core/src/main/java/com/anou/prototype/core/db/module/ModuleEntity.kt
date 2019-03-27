
package com.anou.prototype.core.db.module

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.annotation.NonNull

import com.google.gson.annotations.SerializedName


@Entity(tableName = "Module")
data class ModuleEntity(@PrimaryKey
                        @NonNull
                        var id: String,
                        var title: String? = null,
                        var type: String? = null,
                        @SerializedName("created_at")
                        var createdAt: String? = null,
                        @SerializedName("updated_at")
                        var updatedAt: String? = null,
                        var position: Int = 0) : Parcelable, Comparable<ModuleEntity> {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun compareTo(other: ModuleEntity): Int {
        return if (id == other.id) {
            0
        } else {
            title?.compareTo(other.title ?: "") ?: -1
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(type)
        parcel.writeString(createdAt)
        parcel.writeString(updatedAt)
        parcel.writeInt(position)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ModuleEntity> {
        override fun createFromParcel(parcel: Parcel): ModuleEntity {
            return ModuleEntity(parcel)
        }

        override fun newArray(size: Int): Array<ModuleEntity?> {
            return arrayOfNulls(size)
        }

        val ABOUT = "about"
        val TWEET = "tweet"
        val SETTING = "setting"
    }


}