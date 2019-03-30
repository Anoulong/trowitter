package com.anou.prototype.core.db.tweet

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

import com.google.gson.annotations.SerializedName


@Entity(tableName = "Tweet")
data class TweetEntity(
    @NonNull
    @PrimaryKey
    var id: String,

    var title: String? = null,

    var subtitle: String? = null,

    var image: String? = null,

    var description: String? = null,

    @SerializedName("created_at")
    var createdAt: String? = null,

    @SerializedName("updated_at")
    var updatedAt: String? = null

) : Parcelable, Comparable<TweetEntity> {
    override fun compareTo(other: TweetEntity): Int {
        return title?.compareTo(other.title ?: "") ?: -1
    }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(subtitle)
        parcel.writeString(image)
        parcel.writeString(description)
        parcel.writeString(createdAt)
        parcel.writeString(updatedAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TweetEntity> {
        override fun createFromParcel(parcel: Parcel): TweetEntity {
            return TweetEntity(parcel)
        }

        override fun newArray(size: Int): Array<TweetEntity?> {
            return arrayOfNulls(size)
        }
    }

}
