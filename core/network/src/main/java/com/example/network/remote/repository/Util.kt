package com.example.network.remote.repository

/**
 * @param low 快排起点，默认为0
 * @param high 快排终点，默认为最后一个
 */
fun <E:Comparable<E>>MutableList<E>.quickSort(
    low:Int = 0,
    high:Int = size - 1
) {
    if (low < high) {
        val pivot = partition(low,high)
        quickSort(low,pivot-1)
        quickSort(pivot+1,high)
    }
}


/**
 * 处理后 l..index 的元素均小于index处，index处均小于 index..high
 * @return 分割点索引 index
 */
private fun <E:Comparable<E>>MutableList<E>.partition(l:Int,h:Int) :Int {
    var low = l
    var high = h
    val pivot = get(low)
    while (low < high) {
        while (low < high && get(high) >= pivot) {
            high--
        }
        this[low] = this[high]
        while (low < high && get(low) <= pivot) {
            low++
        }
        this[high] = this[low]
    }
    this[low] = pivot
    return low
}