package di

import android.content.Context
import datastore.createDataStore
import domain.MainViewModel
import org.co.notes.database.getNotesDatabase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun viewModelModule (context: Any?)= module {
    single{
        getNotesDatabase(context as Context).notesDao()
    }
    single {
        createDataStore(context)
    }
    viewModelOf(::MainViewModel)
}
