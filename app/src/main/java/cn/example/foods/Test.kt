package cn.example.foods

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.designsystem.R
import com.example.model.remoteModel.Order
import com.example.model.remoteModel.OrderDetail
import com.example.model.remoteModel.User
import com.example.network.remote.repository.RemoteRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime


@Preview
@Composable
fun GlideTest() {
    val context = LocalContext.current
    AndroidView(factory = {
        val imageView = ImageView(context).apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
        Glide
            .with(context)
            .load("http://10.129.67.213:80/food/showimg/49584efd-9b67-4a07-9ddb-191703fbf303.png")
            .placeholder(R.drawable.food13)
            .error(R.drawable.food11)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.v("glide", "--onLoadFailed--")
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.v("glide", "--onResourceReady--")
                    if (resource is BitmapDrawable) {
                        val bitmap = resource.bitmap
//                val imageBitmap = bitmap.asImageBitmap()
                        Log.d("glide", bitmap.toString() + "resource is BitmapDrawable")
                    } else {
                        Log.d("glide", "resource not a BitmapDrawable")
                    }
                    return false
                }

            }).into(imageView)
        imageView
    })
}


@Preview
@Composable
fun Fix() {
//    TestRemoteService(remoteRepository = RemoteRepository())
}

@RequiresApi(Build.VERSION_CODES.O)
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
@RequiresApi(Build.VERSION_CODES.O)
val order = Order(
    999L, "999", "1", "",50.0, "address", "cgf", "10086",
    orderDetailList = listOf(
        OrderDetail(
            888L, "小菜", 2.1, "2222222", 1.0
        )
    )
)
fun String.logv(tag:String = "cgf") = Log.v(tag,this)