package database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {
    @Upsert
    suspend fun saveNote(notes: Notes):Long

    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<Notes>>

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNote(id: Int)

}