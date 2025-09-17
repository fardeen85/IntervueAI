package com.fardeen.intervueai.local


import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Central Room database for the app.
 * Add all @Entity classes to the 'entities' array.
 */
@Database(
    entities = [
        ChatEntity::class // add more entities here when needed
    ],
    version = 1,
    exportSchema = false // keep true if you want schema versioning
)
abstract class AppDatabase : RoomDatabase() {

    // Provide DAOs here
    abstract fun chatDao(): ChatDao
}
