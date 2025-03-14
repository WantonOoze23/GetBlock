package com.tyshko.getblock.view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyshko.getblock.data.repository.RpcRepository
import com.tyshko.getblock.models.epoch.GetEpochInfo
import com.tyshko.getblock.models.rpc.RpcResponse
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class GetBlockViewModel : ViewModel() {
    private val repository = RpcRepository()

    private val _epoch = MutableStateFlow<String?>(null)
    val epoch: StateFlow<String?> get() = _epoch

    private val _supply = MutableStateFlow<String?>(null)
    val supply: StateFlow<String?> get() = _supply

    init {
        fetchEpoch()
        fetchSupply()
    }

    fun fetchEpoch() {
        viewModelScope.launch {
            try {
                val response: RpcResponse<GetEpochInfo> = repository.getEpoch()
                _epoch.value = response.result.toString()
            } catch (e: Exception) {
                _epoch.value = "Error"
                Log.e("GetBlockViewModel", "Ошибка при получении Epoch: ${e.message}", e)
            }
        }
    }

    fun fetchSupply() {
        viewModelScope.launch {
            try {
                val response: RpcResponse<GetEpochInfo> = repository.getEpoch()
                _epoch.value = response.result.toString()
            } catch (e: Exception) {
                _epoch.value = "Error"
                Log.e("GetBlockViewModel", "Ошибка при получении Epoch: ${e.message}", e)
            }
        }
    }
}