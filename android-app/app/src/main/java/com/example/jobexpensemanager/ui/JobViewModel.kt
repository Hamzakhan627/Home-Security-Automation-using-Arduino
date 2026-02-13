package com.example.jobexpensemanager.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobexpensemanager.data.JobRepository
import com.example.jobexpensemanager.domain.JobDashboard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class JobViewModel(
    private val repository: JobRepository
) : ViewModel() {

    val jobs = repository.observeJobs().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val selectedJobId = MutableStateFlow<Long?>(null)

    val dashboard: StateFlow<JobDashboard?> = selectedJobId
        .flatMapLatest { id ->
            if (id == null) kotlinx.coroutines.flow.flowOf(null)
            else repository.observeJobDashboard(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun selectJob(jobId: Long) {
        selectedJobId.value = jobId
    }

    fun createJob(name: String, totalAmount: Double, startDateEpochDay: Long) {
        viewModelScope.launch {
            val id = repository.createJob(name, totalAmount, startDateEpochDay)
            selectedJobId.value = id
        }
    }

    fun addExpense(jobId: Long, name: String, amount: Double, dateEpochDay: Long) {
        viewModelScope.launch {
            repository.addExpense(jobId, name, amount, dateEpochDay)
        }
    }

    fun addCredit(jobId: Long, sourceName: String, amount: Double) {
        viewModelScope.launch {
            repository.addCredit(jobId, sourceName, amount)
        }
    }
}
