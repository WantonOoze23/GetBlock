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

                    val epoch = response.result.epoch
                    val absoluteSlot = response.result.absoluteSlot

                    val slotRangeStart = epoch * response.result.slotsInEpoch
                    val slotRangeEnd = slotRangeStart + response.result.slotsInEpoch - 1
                    val timeRemaining = countTime(absoluteSlot.toLong(), slotRangeEnd.toLong())

                    _stack.value = _stack.value.copy(
                        epoch = epoch,
                        slotRangeStart = slotRangeStart,
                        slotRangeEnd = slotRangeEnd,
                        timeRemaining = timeRemaining
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

    private fun countTime(currentSlot: Long, endSlot: Long): String {
        val remainSlots = endSlot - currentSlot
        val remainSeconds = (remainSlots * 0.4).toLong()

        val days = (remainSeconds / 86400).toInt()
        val hours = ((remainSeconds % 86400) / 3600).toInt()
        val minutes = ((remainSeconds % 3600) / 60).toInt()
        val seconds = (remainSeconds % 60).toInt()

        return "%dd %02dh %02dm %02ds".format(days, hours, minutes, seconds)
    }
}