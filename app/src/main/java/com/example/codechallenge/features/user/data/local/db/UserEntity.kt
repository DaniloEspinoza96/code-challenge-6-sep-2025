package com.example.codechallenge.features.user.data.local.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.codechallenge.features.user.domain.model.User

@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "lastname") val lastname: String,
    @ColumnInfo(name = "password") val password: String
) {
    companion object {
        fun fromDomain(
            user: User
        ): UserEntity {
            return UserEntity(
                email = user.email,
                name = user.name,
                lastname = user.lastname,
                password = user.password
            )
        }
    }
}