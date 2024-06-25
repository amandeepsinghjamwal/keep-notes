package database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Notes(
    @PrimaryKey(autoGenerate = true)
    val id:Int? = null,
    val title:String,
    val body:String,
    val createdDate:Long,
    val updatedDate:Long,
    val colorHex:Long = generateRandomColor(),

    ){
    companion object {
        val colors = listOf(0xFF9EFFFF,0xFF91F48F,0xFFFD99FF,0xFFFFF599,0xFFFF9E9E,0xFFB69CFF)
        fun generateRandomColor() = colors.random()
    }
}
