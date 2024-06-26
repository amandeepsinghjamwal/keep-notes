package di

import datastore.createDataStore
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import domain.MainViewModel
import getNotesDatabase

actual fun viewModelModule(context: Any?) = module {
    single{
        createDataStore(null)
    }
    single{
        getNotesDatabase().notesDao()
    }
    singleOf(::MainViewModel)
}