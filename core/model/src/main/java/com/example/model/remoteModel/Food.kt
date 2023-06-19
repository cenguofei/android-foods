package com.example.model.remoteModel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

/**
    支持的类型
    @Parcelize 支持多种类型：

    基元类型（及其 boxed 版本）
    对象和枚举
    String、CharSequence
    Exception
    Size、SizeF、Bundle、IBinder、IInterface、FileDescriptor
    SparseArray、SparseIntArray、SparseLongArray、SparseBooleanArray
    所有 Serializable（包括 Date）和 Parcelable 实现
    所有受支持类型的集合：List（映射到 ArrayList）、Set（映射到 LinkedHashSet）、Map（映射到 LinkedHashMap）
    此外，还有一些具体实现：ArrayList、LinkedList、SortedSet、NavigableSet、HashSet、LinkedHashSet、TreeSet、SortedMap、NavigableMap、HashMap、LinkedHashMap、TreeMap、ConcurrentHashMap
    所有受支持类型的数组
    所有受支持类型的可为 null 版本
 */
@Parcelize
data class Food(
    /**
     * 菜品Id
     */
    var id: Long,

    var createUserId:Long = 0,

    /**
     * 菜品名称
     */
    val foodName: String = "",

    /**
     * 描述
     */
    val taste: String = "",

    /**
     * 价格
     */
    val price: Double = 0.00,

    /**
     * 添加到购物车中的数量
     */
    val count: Long = 0,

    /**
     * 菜的图片
     */
    val foodPic: String = "",

    //测试用的
    val foodImg:Int = 0,

    /**
     * 菜单类型
     */
    val foodType: Long = 1,

    val foodCategory:String = "",

//    val createTime: Date? = null
) : Parcelable
