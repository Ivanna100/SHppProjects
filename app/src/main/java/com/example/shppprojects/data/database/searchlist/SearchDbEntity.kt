package com.example.shppprojects.data.database.searchlist

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shppprojects.data.model.Contact

@Entity(tableName = "search_list")
data class SearchDbEntity(
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
        fun toEntity(contact: Contact) = SearchDbEntity(
            id = contact.id,
            name = contact.name,
            career = contact.career,
            photo = contact.photo,
            address = contact.address
        )
    }

}