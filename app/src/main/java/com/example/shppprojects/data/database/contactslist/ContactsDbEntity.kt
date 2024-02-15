package com.example.shppprojects.data.database.contactslist

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shppprojects.data.model.Contact

@Entity(
    tableName = "contacts_list"
)
data class ContactsDbEntity(
    @PrimaryKey val id: Long,
    val name: String? = null,
    val career: String? = null,
    val address: String? = null,
    val photo: String? = null,
) {
    fun toContact() = Contact(
        name = name,
        career = career,
        photo = photo,
        address = address,
        id = id
    )

    companion object {
        fun toEntity(contact: Contact) = ContactsDbEntity(
            id = contact.id,
            name = contact.name,
            career = contact.career,
            photo = contact.photo,
            address = contact.address
        )
    }

}