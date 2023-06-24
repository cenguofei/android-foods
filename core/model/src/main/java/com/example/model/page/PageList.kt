package com.example.model.page

data class PageList<T>(
    val total:Long = 0,

    val rows:List<T>
) {
    fun isEmpty(): Boolean = rows.isEmpty()
}

fun <T>emptyPageList() : PageList<T> = PageList(0,listOf())