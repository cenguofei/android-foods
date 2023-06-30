package com.example.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


enum class TopStates {
    EXPANDED,// 默认展开状态
    SCROLLING,//中间状态 ,中间状态时 connection 也要消耗掉全部事件
    COLLAPSED// 折叠状态
}

@Composable
fun rememberCollapsableLayoutState(minTopHeightDp: Dp = 0.dp): CollapsableLayoutState {
    val density = LocalDensity.current
    return remember(minTopHeightDp, density) {
        CollapsableLayoutState(minTopHeightDp, density)
    }
}

class CollapsableLayoutState(
    private val minTopHeightDp: Dp,
    density: Density
) {
    private val offsetState: MutableState<Offset> = mutableStateOf(Offset.Zero)
    private val topState: MutableState<TopStates> = mutableStateOf(TopStates.EXPANDED)

    //top 最大高度需要 top 容器经过测量后得到
    private val maxHeightState: MutableState<Int> = mutableIntStateOf(-1)

    //默认 EXPANDED 状态 ，progress 默认为 1
    private val expendProgressState = mutableFloatStateOf(1f)

    val currentTopState
        get() = topState.value

    val minTopHeightPx = with(density) {
        minTopHeightDp.toPx().toInt()
    }

    val maxTopHeightPx
        get() = maxHeightState.value

    val maxOffsetY
        get() = maxTopHeightPx - minTopHeightPx

    val expendProgress
        @SuppressLint("AutoboxingStateValueProperty")
        get() = expendProgressState.value

    fun shouldConsumeAvailable(available: Offset): Boolean {
        return topState.value == TopStates.SCROLLING //处于折叠和展开状态
                //展开状态 向上滑动
                || topState.value == TopStates.EXPANDED && available.y < 0
                //折叠状态 向下滑动
                || topState.value == TopStates.COLLAPSED && available.y > 0
    }

    fun plusOffset(offset: Offset) {
        offsetState.value = offsetState.value + offset
    }

    /**
     * 设置 top 容器最大高度
     * @param maxHeight Int
     */
    fun updateMaxTopHeight(maxHeight: Int) {
        if (maxHeight == maxHeightState.value) return
        maxHeightState.value = maxHeight
    }

    /**根据 offsetState 计算当前 top 容器的高度
     */
    @SuppressLint("AutoboxingStateValueProperty")
    fun calcTopHeight(): Int {
        val curTopHeight =
            (maxTopHeightPx + offsetState.value.y.toInt()).coerceIn(minTopHeightPx, maxTopHeightPx)
        //根据 curTopHeight 设置 LayoutState ，计算 expendProgressState
        when (curTopHeight) {
            minTopHeightPx -> {
                offsetState.value = Offset(0f, -maxOffsetY.toFloat())
                expendProgressState.value = 0f
                updateLayoutState(TopStates.COLLAPSED)
            }

            maxTopHeightPx -> {
                offsetState.value = Offset.Zero
                expendProgressState.value = 1f
                updateLayoutState(TopStates.EXPANDED)
            }

            else -> {
                val offsetY = (maxTopHeightPx - curTopHeight).toFloat()
                expendProgressState.value = 1 - offsetY / maxOffsetY
                updateLayoutState(TopStates.SCROLLING)
            }
        }
        return curTopHeight
    }

    private fun updateLayoutState(state: TopStates) {
        if (state == topState.value) return
        topState.value = state
    }
}

class CollapsableScrollConnection(
    private val isChildScrolled: State<Boolean> = mutableStateOf(false),
    private val state: CollapsableLayoutState
) : NestedScrollConnection {

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        if (isChildScrolled.value) return Offset.Zero
        if (state.shouldConsumeAvailable(available)) {
            state.plusOffset(available)
            return available
        }
        return Offset.Zero
    }
}


/**
 * 原文: https://juejin.cn/post/7231804508082815035#heading-3
 */
@Composable
fun CollapsableLayout(
    topContent: @Composable () -> Unit,
    bottomContent: @Composable () -> Unit,
    bottomContentScrolled: State<Boolean> = mutableStateOf(false),
    state: CollapsableLayoutState = rememberCollapsableLayoutState(0.dp),
) {
    val connection: CollapsableScrollConnection = remember {
        CollapsableScrollConnection(bottomContentScrolled, state)
    }

    val heightModifier = if (state.maxTopHeightPx != -1) {
        Modifier.height(with(LocalDensity.current) {
            state.calcTopHeight().toDp() /*+ lineHeight.value.dp*/
        })
    } else {
        Modifier
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(connection)
    ) {
        Box(
            modifier = Modifier
                .then(heightModifier)
                .onSizeChanged {

                    //设置 top 最大高度
                    if (state.maxTopHeightPx == -1) {
                        state.updateMaxTopHeight(it.height)
                    }
                }
        ) { topContent() }
        Box(
            modifier = Modifier
                .fillMaxSize()
//                .scrollable(rememberScrollState(), Orientation.Vertical)
        ) { bottomContent() }
    }
}
