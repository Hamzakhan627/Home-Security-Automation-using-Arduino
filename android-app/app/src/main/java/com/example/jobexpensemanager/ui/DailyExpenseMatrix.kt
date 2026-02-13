package com.example.jobexpensemanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jobexpensemanager.data.DailyExpenseSummary

@Composable
fun DailyExpenseMatrix(
    dailySummary: List<DailyExpenseSummary>,
    onDayClick: (epochDay: Long) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(dailySummary) { day ->
            Card(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clickable { onDayClick(day.dateEpochDay) }
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text("Day ${day.dateEpochDay}")
                    Text("${"%.2f".format(day.totalExpense)}")
                }
            }
        }
    }
}
