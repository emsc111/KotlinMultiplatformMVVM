package com.example.kotlinmpmvvmdatabase.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.shared.db.DeweyDatabase
import com.example.shared.data.Book
import com.example.shared.data.BookRepository
import com.example.shared.viewmodel.DeweyUiState
import com.example.shared.viewmodel.DeweyViewModel
import kotlinx.coroutines.launch
import androidx.compose.material3.TopAppBar

@Composable
fun DeweyScreen(database: DeweyDatabase) {
    val repo = remember(database) { BookRepository(database) }
    val vm = remember(repo) { DeweyViewModel(repo) }
    val state by vm.state.collectAsState()

    DeweyContent(
        state = state,
        onAdd = { title, dewey -> vm.addBook(title, dewey) }
    )
}

@Composable
private fun DeweyContent(
    state: DeweyUiState,
    onAdd: suspend (String, String) -> Unit
) {
    val scope = rememberCoroutineScope()
    var title by rememberSaveable { mutableStateOf("") }
    var dewey by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            // If SmallTopAppBar errors, change to TopAppBar(title = { Text("Dewey Books") })
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(title = { Text("Dewey Books") })
        }
    ) { padding -> // <-- keep this name and use it below
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = dewey,
                onValueChange = { dewey = it },
                label = { Text("Dewey code") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    scope.launch {
                        onAdd(title, dewey)
                        title = ""
                        dewey = ""
                    }
                },
                enabled = title.isNotBlank() && dewey.isNotBlank()
            ) {
                Text("Add")
            }

            if (state.loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            state.error?.let { msg ->
                Text(text = msg, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.books) { book ->
                    BookRow(book)
                }
            }
        }
    }
}

@Composable
private fun BookRow(book: Book) {
    Surface(
        tonalElevation = 2.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(book.title)
            Text(book.dewey)
        }
    }
}
