package com.example.fullguideroom.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fullguideroom.presentation.component.AddContactDialog
import com.example.fullguideroom.presentation.component.ContactItem
import com.example.fullguideroom.presentation.component.SortOptions

@Composable
fun ContactScreen(
    state: ContactState, onEvent: (ContactEvent) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(ContactEvent.ShowDialog)
            }) {
                Icon(
                    imageVector = Icons.Default.Add, contentDescription = "Add contact"
                )
            }
        },
    ) { _ ->
        Box(modifier = Modifier.fillMaxSize()) {
            if (state.isAddingContact) {
                AddContactDialog(state = state, onEvent = onEvent)
            }
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    SortOptions(state.sortType, onEvent)
                }
                items(state.contacts) { contact ->
                    ContactItem(contact, onEvent)
                }
            }
        }
    }
}



