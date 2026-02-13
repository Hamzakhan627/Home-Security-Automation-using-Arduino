package com.example.jobexpensemanager.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [JobEntity::class, ExpenseEntity::class, CreditEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun jobExpenseDao(): JobExpenseDao
}
