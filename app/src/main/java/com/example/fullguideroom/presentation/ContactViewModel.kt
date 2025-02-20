package com.example.fullguideroom.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fullguideroom.domain.Contact
import com.example.fullguideroom.data.ContactDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ContactViewModel(
    private val dao: ContactDao
) : ViewModel() {
    private val _sortType = MutableStateFlow(SortType.FIRST_NAME)
    private val _contacts = _sortType.flatMapLatest { sortType ->
        when (sortType) {
            SortType.FIRST_NAME -> dao.getContactsOrderedByFirstName()
            SortType.LAST_NAME -> dao.getContactsOrderedByLastName()
            SortType.PHONE_NUMBER -> dao.getContactsOrderedByPhoneNumber()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(ContactState())
    val state = combine(_state, _sortType, _contacts) { state, sortType, contacts ->
        state.copy(
            contacts = contacts, sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ContactState())

    fun onEvent(event: ContactEvent) {
        when (event) {
            is ContactEvent.DeleteContact -> deleteContact(event.contact)
            ContactEvent.HideDialog -> hideDialog()
            ContactEvent.SaveContact -> saveContact()
            is ContactEvent.SetFirstName -> setFirstName(event.firstName)
            is ContactEvent.SetLastName -> setLastName(event.lastName)
            is ContactEvent.SetPhoneNumber -> setPhoneNumber(event.phoneNumber)
            ContactEvent.ShowDialog -> showDialog()
            is ContactEvent.SortContacts -> sortContacts(event.sortType)
        }
    }

    private fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            dao.deleteContact(contact)
        }
    }

    private fun hideDialog() {
        _state.update {
            it.copy(isAddingContact = false)
        }
    }

    private fun saveContact() {
        val firstName = state.value.firstName
        val lastName = state.value.lastName
        val phoneNumber = state.value.phoneNumber

        if (firstName.isBlank() || lastName.isBlank() || phoneNumber.isBlank()) {
            return
        }

        val contact = Contact(
            firstName = firstName,
            lastName = lastName,
            phoneNumber = phoneNumber
        )

        viewModelScope.launch {
            dao.upsertContact(contact)
        }

        _state.update {
            it.copy(
                isAddingContact = false,
                firstName = "",
                lastName = "",
                phoneNumber = ""
            )
        }
    }

    private fun setFirstName(firstName: String) {
        _state.update {
            it.copy(firstName = firstName)
        }
    }

    private fun setLastName(lastName: String) {
        _state.update {
            it.copy(lastName = lastName)
        }
    }

    private fun setPhoneNumber(phoneNumber: String) {
        _state.update {
            it.copy(phoneNumber = phoneNumber)
        }
    }

    private fun showDialog() {
        _state.update {
            it.copy(isAddingContact = true)
        }
    }

    private fun sortContacts(sortType: SortType) {
        _sortType.value = sortType
    }
}