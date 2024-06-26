package datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

actual fun createDataStore(context: Any?): DataStore<Preferences> {
    require(value = context is Context,
        lazyMessage = { "Missing context." })
    return AppDatastore.getDataStore(
        producePath = { context.filesDir.resolve(dataStoreFileName).absolutePath }
    )
}