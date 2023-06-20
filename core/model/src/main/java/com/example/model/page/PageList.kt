package com.example.model.page

import com.example.model.remoteModel.Food

data class PageList<T>(
    val total:Long = 0,

    val rows:List<T>
) {
    fun isEmpty(): Boolean = rows.isEmpty()
}

fun <T>emptyPageList() : PageList<T> = PageList<T>(0,listOf())