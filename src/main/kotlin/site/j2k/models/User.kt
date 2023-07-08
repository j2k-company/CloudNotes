package site.j2k.models

import kotlinx.serialization.Serializable

@Serializable
data class User(val id: Int, val nickname: String)
