package domain

import database.Notes
import kotlinx.coroutines.flow.Flow

interface DataSource {
    fun getAllNotes(): Flow<List<Notes>>
    suspend fun addNote(notesModel:Notes):Long
    suspend fun deleteNoteById(id:Long):Boolean
}

data class StateUi(
    val notesList : List<Notes> = listOf(),
    val selectedModel: Notes = Notes(
        title = "",
        body = "",
        createdDate = 0L,
        updatedDate = 0L,
    ),
    val selectedSortIndex:Int = 0
)