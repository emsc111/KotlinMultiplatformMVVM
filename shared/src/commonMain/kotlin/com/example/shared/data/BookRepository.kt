package com.example.shared.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.shared.db.DeweyDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

// Simple domain model used in shared code and UI
data class Book(
    val id: Long,
    val title: String,
    val dewey: String
)

class BookRepository(
    private val db: DeweyDatabase
) {

    // Stream list of books; uses the SQLDelight mapper instead of a generated row type.
    fun books(): Flow<List<Book>> =
        db.deweyQueries
            .selectAll { id, title, dewey ->
                Book(
                    id = id ?: 0L,
                    title = title ?: "",
                    dewey = dewey ?: ""
                )
            }
            .asFlow()
            .mapToList(Dispatchers.IO)

    // Insert a new book
    suspend fun addBook(title: String, dewey: String) = withContext(Dispatchers.IO) {
        db.deweyQueries.insertBook(
            title = title,
            dewey = dewey
        )
    }

    // Delete by id (only if you added `deleteBook` to your .sq file)
    suspend fun deleteBook(id: Long) = withContext(Dispatchers.IO) {
        db.deweyQueries.deleteBook(id)
    }

    // If you really want a "wipe all" helper, either add the .sq query OR remove this method.
    // .sq option:
    // deleteAll:
    // DELETE FROM books;
    //
    // Then uncomment:
    // suspend fun deleteAll() = withContext(Dispatchers.IO) {
    //     db.deweyQueries.deleteAll()
    // }
}
