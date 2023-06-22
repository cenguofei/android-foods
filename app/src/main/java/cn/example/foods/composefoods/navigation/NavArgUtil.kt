package cn.example.foods.composefoods.navigation

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.navigation.NavType
import com.google.gson.Gson

inline fun <reified T : Parcelable> createParcelableNavType(nullable: Boolean = false): NavType<T> {
    return object : NavType<T>(nullable) {

        override val name: String
            get() = "SupportParcelable"

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun get(bundle: Bundle, key: String): T? {
            return bundle.getParcelable(key,T::class.java)
        }

        override fun parseValue(value: String): T {
            return Gson().fromJson(value, T::class.java)
        }

        override fun put(bundle: Bundle, key: String, value: T) {
            bundle.putParcelable(key, value)
        }
    }
}
