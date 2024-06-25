package domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import database.Notes
import database.NotesDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(private val notesDao: NotesDao): ViewModel() {

    private val _stateUi = MutableStateFlow<StateUi>(StateUi())
    val stateUi: StateFlow<StateUi> get() = _stateUi.asStateFlow()
    init {
        viewModelScope.launch {
            notesDao.getAllNotes().collect{notes->
                _stateUi.update {
                    it.copy(
                        notesList = notes
                    )
                }
            }
        }
    }

    fun deleteNoteById(id: Int, onComplete: () -> Unit) {
        viewModelScope.launch {
            notesDao.deleteNote(id)
            onComplete()
        }
    }

    fun createNote(note: Notes, onComplete: (Long) -> Unit) {
        viewModelScope.launch {
            val id = notesDao.saveNote(note)
            onComplete(id)
        }
    }

    fun sortList(selectedIndex: Int) {

    }

    fun updateSelectedModel(note: Notes) {
        _stateUi.update {
            it.copy(
                selectedModel = note
            )
        }
    }
}