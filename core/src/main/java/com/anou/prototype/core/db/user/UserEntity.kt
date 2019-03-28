package com.anou.prototype.core.db.about

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

import com.google.gson.annotations.SerializedName


@Entity(tableName = "User")
data class UserEntity(
    @NonNull
    @PrimaryKey
    var id: String,

    var username: String? = null,

    var firstname: String? = null,

    var lastname: String? = null,

    var email: String? = null,

    var password: String? = null,

    @SerializedName("created_at")
    var createdAt: String? = null,

    @SerializedName("updated_at")
    var updatedAt: String? = null

) : Parcelable, Comparable<UserEntity> {
    override fun compareTo(other: UserEntity): Int {
        return lastname?.compareTo(other.lastname ?: "") ?: -1
    }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
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
        parcel.writeString(username)
        parcel.writeString(firstname)
        parcel.writeString(lastname)
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeString(createdAt)
        parcel.writeString(updatedAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserEntity> {
        override fun createFromParcel(parcel: Parcel): UserEntity {
            return UserEntity(parcel)
        }

        override fun newArray(size: Int): Array<UserEntity?> {
            return arrayOfNulls(size)
        }
    }


}
