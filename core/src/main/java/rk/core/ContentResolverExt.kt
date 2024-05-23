package rk.core

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.os.CancellationSignal
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

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
