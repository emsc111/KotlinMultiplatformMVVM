package com.example.shared.viewmodel

import com.example.shared.data.Book
import com.example.shared.data.BookRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class DeweyUiState(
    val books: List<Book> = emptyList(),
    val loading: Boolean = true,
    val error: String? = null
)

/**
 * Pure KMP view-model (no AndroidX dependency).
 * It owns its own scope to work from commonMain.
 */
class DeweyViewModel(
    private val repo: BookRepository
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _state = MutableStateFlow(DeweyUiState())
    val state: StateFlow<DeweyUiState> = _state.asStateFlow()

    init {
        // Start observing the SQLDelight flow
        scope.launch {
            repo.books()
                .catch { e -> _state.value = _state.value.copy(loading = false, error = e.message) }
                .collectLatest { list ->
                    _state.value = DeweyUiState(books = list, loading = false, error = null)
                }
        }
    }

    suspend fun addBook(title: String, dewey: String) {
        // minimal guard; keep UI responsive
        if (title.isBlank() || dewey.isBlank()) return
        withContext(Dispatchers.Default) {
            repo.addBook(title.trim(), dewey.trim())
        }
        // No need to “clear()” anything here; clear your text fields in the UI after calling this.
    }
}
