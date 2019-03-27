
package com.anou.prototype.core.db

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.annotation.NonNull

import com.google.gson.annotations.SerializedName


@Entity(tableName = "Module")
data class ModuleEntity(@PrimaryKey
                        @NonNull
                        @SerializedName("_id")
                        var id: String,
                        @SerializedName("app_eid")
                        var appEid: String? = null,
                        var eid: String? = null,
                        var title: String? = null,
                        var slug: String? = null,
                        var type: String? = null,
                        var description: String? = null,
                        var isActive: Boolean = false,
                        var isGeocode: Boolean = false,
                        @SerializedName("created_at")
                        var createdAt: String? = null,
                        @SerializedName("updated_at")
                        var updatedAt: String? = null,
                        var content: String? = null,
                        var position: Int = 0) : Parcelable, Comparable<ModuleEntity> {

    override fun compareTo(other: ModuleEntity): Int {
        return if (id == other.id) {
            0
        } else {
            eid?.compareTo(other.eid ?: "") ?: slug?.compareTo(other.slug ?: "") ?: -1
        }
    }

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(appEid)
        parcel.writeString(eid)
        parcel.writeString(title)
        parcel.writeString(slug)
        parcel.writeString(type)
        parcel.writeString(description)
        parcel.writeByte(if (isActive) 1 else 0)
        parcel.writeByte(if (isGeocode) 1 else 0)
        parcel.writeString(createdAt)
        parcel.writeString(updatedAt)
        parcel.writeString(content)
        parcel.writeInt(position)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ModuleEntity> {

        val TEXT_TYPE = "text"
        val ABOUT = "about-us"
        val HOME = "home"
        val QUIZ = "scored-assessment"
        val NEWS = "news"
        val RESOURCES = "resources"
        val LIBRARY = "library"
        val CHECKLIST = "checklists"
        val VIDEOS = "videos"
        val PDF = "pdfs"
        val FAQ = "faq"
        val REPORTING = "reporting"
        val CATEGORY = "category"
        val PUBLIC_USER = "public-user-registration"
        val PRIVATE_USER = "private-user-registration"
        val EVENTS = "events"
        val ACCESS_CODE = "access-code"

        override fun createFromParcel(parcel: Parcel): ModuleEntity {
            return ModuleEntity(parcel)
        }

        override fun newArray(size: Int): Array<ModuleEntity?> {
            return arrayOfNulls(size)
        }
    }
}