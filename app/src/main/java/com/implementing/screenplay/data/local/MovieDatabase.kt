package com.implementing.screenplay.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomDatabase
@AutoMigration(from = 1, to = 2)
@Database(version = 1, entities = [MyListMovie::class], exportSchema = false)
abstract class MovieDatabase(): RoomDatabase() {
    abstract fun movieDao(): MovieDao
}