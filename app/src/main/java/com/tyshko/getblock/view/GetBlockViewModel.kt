package com.tyshko.getblock.view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyshko.getblock.data.repository.RpcRepository
import com.tyshko.getblock.models.stack.UiStack
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class GetBlockViewModel : ViewModel() {
    companion object{
        private val TIME_OUT: Long = 60_000
    }

    private val repository: RpcRepository = RpcRepository()

    private val _stack = MutableStateFlow(UiStack())
    val stack: StateFlow<UiStack> = _stack.asStateFlow()

    init {
        fetchEpoch()
        fetchSupply()
    }

    fun fetchEpoch() {
        viewModelScope.launch {
            while (true) {
                try {
                    val response = repository.getEpoch()

                    val absoluteSlot = response.result.absoluteSlot
                    val blockHeight = response.result.blockHeight
                    val epoch = response.result.epoch
                    val slotIndex = response.result.slotIndex
                    val slotsInEpoch = response.result.slotsInEpoch

                    _stack.value = _stack.value.copy(
                        absoluteSlotEpoch = absoluteSlot,
                        blockHeightEpoch = blockHeight,
                        epoch = epoch,
                        slotIndexEpoch = slotIndex,
                        slotsInEpochEpoch = slotsInEpoch,
                    )

                } catch (e: Exception) {
                    Log.e("GetBlockViewModel", "Ошибка при получении Epoch: ${e.message}", e)
                }
                delay(TIME_OUT)
            }
        }
    }

    fun fetchSupply() {
        viewModelScope.launch {
            while (true) {
                try {
                    val response = repository.getSupply()

                    val circulating = response.result.value.circulating
                    val nonCirculating = response.result.value.nonCirculating
                    val total = response.result.value.total

                    val percentCirculating = (circulating.toDouble() / total.toDouble()) * 100
                    val percentNonCirculating = (nonCirculating.toDouble() / total.toDouble()) * 100

                    _stack.update { current ->
                        current.copy(
                            circulatingSupply = circulating,
                            nonCirculatingSupply = nonCirculating,
                            totalSupply = total,
                            percentCirculatingSupply = percentCirculating,
                            percentNonCirculatingSupply = percentNonCirculating,
                        )
                    }

                } catch (e: Exception) {
                    Log.e("GetBlockViewModel", "Ошибка при получении Supply: ${e.message}", e)
                }
                delay(TIME_OUT)
            }
        }
    }
}