package site.j2k.models

import kotlinx.serialization.Serializable

@Serializable
data class Note(val id: Int, val author: User, val content: String)
