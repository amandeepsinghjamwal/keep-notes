package org.co.notes

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import org.co.notes.database.getNotesDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val notesDao = remember {
                getNotesDatabase(applicationContext).notesDao()
            }
            App()
        }
    }
}
