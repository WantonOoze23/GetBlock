package com.tyshko.getblock.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import com.tyshko.getblock.view.GetBlockViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderSearchBar(
    modifier: Modifier = Modifier,
    viewModel: GetBlockViewModel,
    onSearchClick: () -> Unit = {},
) {
    var searchQuery by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    SearchBar(
        query = searchQuery,
        onQueryChange = { searchQuery = it },
        onSearch = {
            val number = searchQuery.toLongOrNull()
            if (number != null && number in viewModel.stack.value.slotRangeStart..viewModel.stack.value.slotRangeEnd) {
                viewModel.fetchBlock(number)
                onSearchClick()
            } else {
                showErrorDialog = true
            }
            focusManager.clearFocus()
        },
        active = false,
        onActiveChange = {},
        placeholder = { Text("Search transactions, blocks, programs and tokens") },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
        modifier = modifier
            .fillMaxWidth()
            .clickable { focusManager.clearFocus() }
    ) {}

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Error input") },
            text = { Text("Wrong input. Try one more time") },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text("ОК")
                }
            },
            dismissButton = null
        )
    }
}