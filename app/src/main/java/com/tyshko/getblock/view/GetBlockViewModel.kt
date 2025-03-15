package com.tyshko.getblock.view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyshko.getblock.data.repository.RpcRepository
import com.tyshko.getblock.models.epoch.GetEpochInfo
import com.tyshko.getblock.models.rpc.RpcResponse
import com.tyshko.getblock.models.supply.GetSupply
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class GetBlockViewModel : ViewModel() {
    private val repository: RpcRepository = RpcRepository()

    private var _epoch = MutableStateFlow<GetEpochInfo?>(null)
    val epoch: StateFlow<GetEpochInfo?> get() = _epoch

    private val _supply = MutableStateFlow<GetSupply?>(null)
    val supply: StateFlow<GetSupply?> get() = _supply

    init {
        fetchEpoch()
        fetchSupply()
    }

    fun fetchEpoch() {
        viewModelScope.launch {
            try {
                val response = repository.getEpoch()
                _epoch.value = response.result
            } catch (e: Exception) {
                _epoch.value = null
                Log.e("GetBlockViewModel", "Ошибка при получении Epoch: ${e.message}", e)
            }
        }
    }

    fun fetchSupply() {
        viewModelScope.launch {
            try {
                val response: RpcResponse<GetSupply> = repository.getSupply()
                _supply.value = response.result
            } catch (e: Exception) {
                _supply.value = null
                Log.e("GetBlockViewModel", "Ошибка при получении Supply: ${e.message}", e)
            }
        }
    }

    companion object{
        const val TIME_OUT = 60_000
    }
}