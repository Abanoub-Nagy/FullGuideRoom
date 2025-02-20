package com.example.fullguideroom.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fullguideroom.presentation.ContactEvent
import com.example.fullguideroom.presentation.SortType

@Composable
fun SortOptions(
    sortType: SortType, onEvent: (ContactEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SortType.entries.forEach { sortTypeOption ->
            Row(modifier = Modifier
                .clickable {
                    onEvent(ContactEvent.SortContacts(sortTypeOption))
                }
                .padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = sortType == sortTypeOption, onClick = {
                    onEvent(ContactEvent.SortContacts(sortTypeOption))
                })
                Text(
                    text = sortTypeOption.name,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}