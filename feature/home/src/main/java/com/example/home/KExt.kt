package com.example.home

import androidx.compose.runtime.Composable

fun <T> List<T>.toPairs(): MutableList<Pair<T, T?>> {
    if (this.isEmpty()) {
        return mutableListOf()
    }
    if (this.size == 1) {
        return mutableListOf(this[0] to null)
    }

    val pairs = mutableListOf<Pair<T, T?>>()
    var pairList = mutableListOf<T>()
    for (i in indices) {
        if (pairList.size == 2) {
            pairs.add(pairList[0] to pairList[1])
            pairList = mutableListOf()
        } else {
            pairList.add(this[i])
        }
    }
    if (pairList.isNotEmpty()) {
        pairs.add(pairList[0] to null)
    }
    return pairs
}
