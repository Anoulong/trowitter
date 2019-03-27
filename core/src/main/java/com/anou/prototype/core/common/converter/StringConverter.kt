
package com.anou.prototype.core.common.converter


import androidx.room.TypeConverter
import com.google.gson.Gson
import java.util.*

/**
 * Utility class to help transform an json array to a Java array
 */
object StringConverter {
    @TypeConverter
    fun fromString(value: String): ArrayList<String> {
        value.isNotBlank().let {
            val strings = Gson().fromJson(value, Array<String>::class.java)
            return if (!strings.isNullOrEmpty()) {
                ArrayList(Arrays.asList(*strings))
            } else ArrayList()
        }
        return ArrayList()
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<String>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}