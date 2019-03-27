package com.anou.prototype.core.db.category

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Category")
data class CategoryEntity(
        @PrimaryKey
        @NonNull
        @SerializedName("_id")
        var id: String,
        @ColumnInfo(index = true)
        @SerializedName("custom_module_eid")
        var moduleEid: String?,
        var slug: String?,
        var eid: String?,
        var title: String?,
        var nbItems: Int = 0,
        var type: String?,
        var description: String?,
        @SerializedName("updated_at")
        var updatedAt: String?,
        @SerializedName("created_at")
        var createdAt: String?
) : Parcelable, Comparable<CategoryEntity> {

    override fun compareTo(other: CategoryEntity): Int {
        return if (eid == other.eid) {
            0
        } else {
            title?.compareTo(other.title ?: "") ?: slug?.compareTo(other.slug ?: "") ?: -1
        }
    }

    constructor(parcel: Parcel) : this(
            id = parcel.readString() ?: "",
            moduleEid = parcel.readString(),
            slug = parcel.readString(),
            eid = parcel.readString(),
            title = parcel.readString(),
            nbItems = parcel.readInt(),
            type = parcel.readString(),
            description = parcel.readString(),
            updatedAt = parcel.readString(),
            createdAt = parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(moduleEid)
        parcel.writeString(slug)
        parcel.writeString(eid)
        parcel.writeString(title)
        parcel.writeInt(nbItems)
        parcel.writeString(type)
        parcel.writeString(description)
        parcel.writeString(updatedAt)
        parcel.writeString(createdAt)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CategoryEntity> {
        override fun createFromParcel(parcel: Parcel): CategoryEntity {
            return CategoryEntity(parcel)
        }

        override fun newArray(size: Int): Array<CategoryEntity?> {
            return arrayOfNulls(size)
        }
    }
}