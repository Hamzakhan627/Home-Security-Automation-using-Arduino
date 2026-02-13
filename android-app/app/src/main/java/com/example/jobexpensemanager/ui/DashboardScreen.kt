package com.example.jobexpensemanager.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jobexpensemanager.domain.JobDashboard

@Composable
fun DashboardScreen(
    dashboard: JobDashboard?,
    onAddExpense: () -> Unit,
    onAddCredit: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { if (selectedTab == 0) onAddExpense() else onAddCredit() }) {
                Icon(Icons.Default.Add, contentDescription = "Add entry")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            SummaryCards(dashboard)
            TabRow(selectedTabIndex = selectedTab) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Expenses") })
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Credits") })
            }

            when (selectedTab) {
                0 -> ExpenseList(dashboard)
                1 -> CreditList(dashboard)
            }
        }
    }
}

@Composable
private fun SummaryCards(dashboard: JobDashboard?) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        SummaryCard("Total", dashboard?.job?.totalAmount ?: 0.0)
        SummaryCard("Expenses", dashboard?.totalExpenses ?: 0.0)
        SummaryCard("Remaining", dashboard?.remainingAmount ?: 0.0)
    }
}

@Composable
private fun SummaryCard(title: String, amount: Double) {
    Card(modifier = Modifier.weight(1f)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, style = MaterialTheme.typography.labelMedium)
            Text("%.2f".format(amount), style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
private fun ExpenseList(dashboard: JobDashboard?) {
    LazyColumn {
        items(dashboard?.expenses.orEmpty()) { expense ->
            Text(
                text = "${expense.name} • ${expense.amount} • ${expense.dateEpochDay}",
                modifier = Modifier.padding(vertical = 6.dp)
            )
        }
    }
}

@Composable
private fun CreditList(dashboard: JobDashboard?) {
    Column {
        Text("Total Credits: %.2f".format(dashboard?.totalCredits ?: 0.0), modifier = Modifier.padding(vertical = 8.dp))
        LazyColumn {
            items(dashboard?.credits.orEmpty()) { credit ->
                Text(
                    text = "${credit.sourceName} • ${credit.amount}",
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            }
        }
    }
}
