package org.co.notes.database

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import database.NotesDatabase

fun getNotesDatabase(context: Context): NotesDatabase {
    val dbFile = context.getDatabasePath("notes.db")
    return Room.databaseBuilder<NotesDatabase>(
        context = context.applicationContext,
        name = dbFile.absolutePath
    )
        .setDriver(BundledSQLiteDriver())
        .build()
}