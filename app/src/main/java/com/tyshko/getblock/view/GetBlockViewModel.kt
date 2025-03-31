package com.tyshko.getblock.view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyshko.getblock.data.repository.RpcRepository
import com.tyshko.getblock.models.stack.Block
import com.tyshko.getblock.models.stack.UiStack
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class GetBlockViewModel : ViewModel() {
    companion object{
        private val TIME_OUT: Long = 60_000L
        private const val amountOfBlock: Int = 5
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
                    fetchBlocks(slotRangeStart.toLong(), slotRangeEnd.toLong(), epoch)
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

    fun fetchBlock(blockNumber: Long){
        viewModelScope.launch {
            while (true) {
                try {
                    val block = repository.getBlock(blockNumber)

                    val blockModel = Block(
                        block = blockNumber,
                        signature = block.result.blockhash,
                        time = block.result.blockTime,
                        epoch = _stack.value.epoch,
                        rewardLamports = block.result.rewards[0].lamports,
                        previousBlockHash = block.result.previousBlockhash
                    )

                    _stack.update { current ->
                        current.copy(
                            currentBlock = blockModel
                        )
                    }
                } catch (e: Exception){
                    Log.e("GetBlockViewModel", "Ошибка при получении Block: ${e.message}", e)
                }
                delay(TIME_OUT)
            }
        }
    }

    fun fetchBlocks(startSlot: Long, endSlot: Long? = null, epoch: Int) {
        viewModelScope.launch {
            while (true) {
                try {
                    val response = repository.getBlocks(startSlot, endSlot).takeLast(amountOfBlock)
                    val ListOfBlocks = response.map { block ->
                        val blockInfo = repository.getBlock(block)
                        Block(
                            time = blockInfo.result.blockTime,
                            block = block,
                            signature = blockInfo.result.blockhash,
                            epoch = epoch,
                            rewardLamports = blockInfo.result.rewards[0].lamports,
                            previousBlockHash = blockInfo.result.previousBlockhash
                        )
                    }

                    _stack.update { current ->
                        current.copy(
                            blocks = ListOfBlocks
                        ) }
                } catch (e: Exception) {
                    Log.e("GetBlockViewModel", "Ошибка при получении блоков: ${e.message}", e)
                }
                delay(TIME_OUT)
            }
        }
    }

    fun setCurrentBlock(block: Block) {
        _stack.update { currentState ->
            currentState.copy(
                currentBlock = block
            )
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