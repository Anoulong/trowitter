package com.anou.prototype.core.db.feature

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Feature")
data class FeatureEntity(
        @PrimaryKey
        @NonNull
        @SerializedName("_id")
        var id: String,
        @ColumnInfo(index = true)
        @SerializedName("category_eid")
        var categoryEid: String?,
        var slug: String?,
        var eid: String?,
        @SerializedName("updated_at")
        var updatedAt: String?,
        @SerializedName("created_at")
        var createdAt: String?
) : Parcelable, Comparable<FeatureEntity> {

    override fun compareTo(other: FeatureEntity): Int {
        return if (id == other.id) {
            0
        } else {
            eid?.compareTo(other.eid ?: "") ?: slug?.compareTo(other.slug ?: "") ?: -1
        }
    }

    constructor(parcel: Parcel) : this(
            id = parcel.readString() ?: "",
            categoryEid = parcel.readString(),
            slug = parcel.readString(),
            eid = parcel.readString(),
            updatedAt = parcel.readString(),
            createdAt = parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(categoryEid)
        parcel.writeString(slug)
        parcel.writeString(eid)
        parcel.writeString(updatedAt)
        parcel.writeString(createdAt)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FeatureEntity> {
        override fun createFromParcel(parcel: Parcel): FeatureEntity {
            return FeatureEntity(parcel)
        }

        override fun newArray(size: Int): Array<FeatureEntity?> {
            return arrayOfNulls(size)
        }
    }
}