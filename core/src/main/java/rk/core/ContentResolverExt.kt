package rk.core

import android.content.ContentResolver
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.CancellationSignal
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext

fun ContentResolver.observe(uri: Uri) = callbackFlow {
    trySend(false)
    val observer = object : ContentObserver(null) {
        override fun onChange(selfChange: Boolean) {
            trySend(selfChange)
        }
    }
    registerContentObserver(uri, true, observer)
    awaitClose {
        unregisterContentObserver(observer)
    }
}

suspend fun ContentResolver.coUery(
    uri: Uri,
    columns: Array<String>? = null,
    selection: String? = null,
    selectionArgs: Array<String>? = null,
    sortOrder: String? = null,
    cancellationSignal: CancellationSignal? = null,
    coroutineDispatcher: CoroutineDispatcher,
    onResult: (Cursor?) -> Unit
) {
    withContext(coroutineDispatcher) {
        onResult(
            query(
                uri,
                columns,
                selection,
                selectionArgs,
                sortOrder,
                cancellationSignal
            )
        )
    }

}
suspend fun ContentResolver.coUery(
    uri: Uri,
    columns: Array<String>? = null,
    selection: String? = null,
    selectionArgs: Array<String>? = null,
    sortOrder: String? = null,
    cancellationSignal: CancellationSignal? = null,
    coroutineDispatcher: CoroutineDispatcher,

    ): Cursor? {
    return withContext(coroutineDispatcher) {
        query(
            uri,
            columns,
            selection,
            selectionArgs,
            sortOrder,
            cancellationSignal
        )

    }

}
