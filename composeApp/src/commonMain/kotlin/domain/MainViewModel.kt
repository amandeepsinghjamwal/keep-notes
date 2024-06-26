package domain

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import database.Notes
import database.NotesDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val notesDao: NotesDao,
    private val dataStore: androidx.datastore.core.DataStore<Preferences>
) : ViewModel() {

    companion object {
        const val SELECTED_SORT_INDEX = "selected_sort_index"
    }

    private val sortIndexKey =
        intPreferencesKey(SELECTED_SORT_INDEX)

    private val _stateUi = MutableStateFlow(StateUi())
    val stateUi: StateFlow<StateUi> get() = _stateUi.asStateFlow()

    init {
        getSelectedSortIndex()
    }

    private fun getSelectedSortIndex() {
        viewModelScope.launch {
            dataStore.data.map { pref ->
                val selectedIndex = pref[sortIndexKey] ?: 0
                println("selected index $selectedIndex")
                _stateUi.update {
                    it.copy(
                        selectedSortIndex = selectedIndex
                    )
                }
            }.first()
            getAllNotes{
                sortList()
            }
        }
    }

    fun deleteNoteById(id: Int, onComplete: () -> Unit) {
        viewModelScope.launch {
            notesDao.deleteNote(id)
            onComplete()
        }
    }

    private suspend fun getAllNotes(onComplete: () -> Unit) {
        notesDao.getAllNotes().collect { notes ->
            _stateUi.update {
                it.copy(
                    notesList = notes
                )
            }
            onComplete()
        }
    }


    private fun sortList() {
        val currentState = stateUi.value
        val sortedList = if (currentState.selectedSortIndex != 0) {
            currentState.notesList.toMutableList().sortedByDescending {
                when (currentState.selectedSortIndex) {
                    1 -> it.createdDate
                    2 -> it.updatedDate
                    else ->it.updatedDate
                }
            }
        } else {
            currentState.notesList.toMutableList().sortedBy {
                it.title
            }
        }
        _stateUi.value = currentState.copy(notesList = sortedList)
    }

    fun createNote(note: Notes, onComplete: (Long) -> Unit) {
        viewModelScope.launch {
            val id = notesDao.saveNote(note)
            onComplete(id)
        }
    }

    fun changeSortIndex(selectedIndex: Int) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[sortIndexKey] = selectedIndex
            }
            _stateUi.update {
                it.copy(
                    selectedSortIndex = selectedIndex
                )
            }
            sortList()
        }
    }

    fun updateSelectedModel(note: Notes) {
        _stateUi.update {
            it.copy(
                selectedModel = note
            )
        }
    }
}