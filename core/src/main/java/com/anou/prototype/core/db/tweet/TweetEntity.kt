package com.anou.prototype.core.db.tweet

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anou.prototype.core.db.module.ModuleEntity

import com.google.gson.annotations.SerializedName
import java.util.*


@Entity(tableName = "Tweet")
data class TweetEntity(
    @NonNull
    @PrimaryKey
    var id: String,

    var username: String? = null,

    var tag: ArrayList<String>? = arrayListOf<String>(),

    var comment: String? = null,

    var type: String? = null,

    var image: String? = null,

    @SerializedName("created_at")
    var createdAt: String? = null,

    @SerializedName("updated_at")
    var updatedAt: String? = null

) : Parcelable, Comparable<TweetEntity> {
    override fun compareTo(other: TweetEntity): Int {
        return updatedAt?.compareTo(other.updatedAt ?: "") ?: -1
    }

    constructor(username: String?, comment: String?, createdAt: String?, updatedAt: String?) : this(
        UUID.randomUUID().toString(), username, null, comment, ModuleEntity.TWEET, "", createdAt, updatedAt
    )

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(username)
        parcel.writeStringList(tag)
        parcel.writeString(comment)
        parcel.writeString(type)
        parcel.writeString(image)
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
