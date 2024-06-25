import database.NotesDatabase
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import platform.Foundation.NSHomeDirectory

fun getNotesDatabase(): NotesDatabase {
    val dbFile = NSHomeDirectory() + "/notes.db"
    return Room.databaseBuilder<NotesDatabase>(
        name = dbFile,
        factory = { NotesDatabase::class.instantiateImpl() }
    )
        .setDriver(BundledSQLiteDriver())
        .build()
}