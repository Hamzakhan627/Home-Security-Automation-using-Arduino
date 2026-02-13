package com.example.jobexpensemanager.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "jobs")
data class JobEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val totalAmount: Double,
    val startDateEpochDay: Long,
    val createdAtMillis: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "expenses",
    foreignKeys = [
        ForeignKey(
            entity = JobEntity::class,
            parentColumns = ["id"],
            childColumns = ["jobId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("jobId"), Index("dateEpochDay")]
)
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val jobId: Long,
    val name: String,
    val amount: Double,
    val dateEpochDay: Long
)

@Entity(
    tableName = "credits",
    foreignKeys = [
        ForeignKey(
            entity = JobEntity::class,
            parentColumns = ["id"],
            childColumns = ["jobId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("jobId")]
)
data class CreditEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val jobId: Long,
    val sourceName: String,
    val amount: Double,
    val createdAtMillis: Long = System.currentTimeMillis()
)

data class DailyExpenseSummary(
    val dateEpochDay: Long,
    val totalExpense: Double
)
