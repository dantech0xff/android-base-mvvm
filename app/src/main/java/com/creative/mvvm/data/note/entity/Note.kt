package com.creative.mvvm.data.note.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.json.JSONArray
import java.util.*

@Entity(
    tableName = "table_notes"
)
data class Note(@PrimaryKey(autoGenerate = true)
                @ColumnInfo(name = "id")
                var id: Int = 0,
                @ColumnInfo(name = "title")
                    var title: String = "",
                @ColumnInfo(name = "description")
                    var description: String = "",
                @ColumnInfo(name = "photo_path")
                    var photoPath: String = "",
                @ColumnInfo(name = "date_updated")
                    var date_updated: Date? = null,
                @ColumnInfo(name = "extra_setting")
                var extra_setting: String = ""
) {

    fun getListPhoto(): MutableList<String> {
        return mutableListOf<String>().let { list ->
            if (photoPath.isNotEmpty()) {
                try {
                    JSONArray(photoPath).let { json ->
                        for (i in 0 until json.length()) {
                            list.add(json[i] as String)
                        }
                    }
                } catch (e: Exception) { }
            }
            list
        }
    }
}