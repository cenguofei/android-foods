package cn.example.foods

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.model.remoteModel.Order
import com.example.model.remoteModel.OrderDetail
import com.example.model.remoteModel.User
import com.example.network.remote.repository.RemoteRepository
import kotlinx.coroutines.launch


@Preview
@Composable
fun Fix() {
    TestRemoteService(remoteRepository = RemoteRepository())
}

@Composable
fun TestRemoteService(remoteRepository: RemoteRepository) {
    Log.v("http_test", "TestRemoteService")
    val coroutineScope = rememberCoroutineScope()
    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Log.v("http_test", "Scaffold")
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .background(Color.Blue.copy(alpha = 0.5f))
        ) {
            TestButton("login") {
                coroutineScope.launch {
//                    val hashMap = remoteRepository.login(username = "cgf", password = "abc")
//                    hashMap.string()
                }
            }

            TestButton("register exist") {
                coroutineScope.launch {
                    val hashMap =
                        remoteRepository.register(
                            User(
                                username = "cgf",
                                password = "abc",
                                email = "qq.com",
                                tel = "111"
                            )
                        )
                    hashMap.string()
                }
            }

            TestButton("postOrder") {
                coroutineScope.launch {
                    val postOrder = remoteRepository.postOrder(
                        order
                    )
                    postOrder.string()
                }
            }

            TestButton("register") {
                coroutineScope.launch {
                    val hashMap = remoteRepository.register(
                        User(
                            username = "chenguofei",
                            password = "aaa",
                            email = "feifei@qq.com",
                            tel = "10086"
                        )
                    )
                    hashMap.string()
                }
            }

            TestButton("getAllFood") {
                coroutineScope.launch {
                    remoteRepository.getAllFood().collect {
                        Log.v("http_test", it.toString())
                    }
                }
            }

//            TestButton("postOrder") {
//                coroutineScope.launch {
//                    val allFood = remoteService.postOrder(Order(1000L,"111",""))
//                    Log.v("http_test",allFood.toString())
//                }
//            }

            TestButton("getUserOrders") {
                coroutineScope.launch {
                    remoteRepository.getUserOrders("admin").collect { v ->
                        v.string()
                    }
                }
            }
        }
    }
}

fun <K, V> Map<K, V>.string() {
    val builder = StringBuilder()
    builder.append("{")
    keys.forEach {
        val item = get(it)
        builder.append(it).append(":").append(item).append(",")
    }
    builder.append("}")
    Log.v("http_test", builder.toString())
}

@Composable
fun TestButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick, modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(text = text, color = Color.Black)
    }
}

val order = Order(
    999L, "999", "1", null, 50.0, "address", "cgf", "10086",
    listOf(
        OrderDetail(
            888L, "小菜", 2.1, "2222222", 1
        )
    )
)
fun String.logv(tag:String = "cgf") = Log.v(tag,this)