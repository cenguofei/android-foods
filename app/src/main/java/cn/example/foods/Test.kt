package cn.example.foods

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun Test() {
    Text(text = "hello")
}

@Preview
@Composable
fun Test1() {

    Text(text = "kjkjkkjj")
}

class Ac : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(ComposeView(this).apply {
            setContent {
                Test()
            }
        })
    }
}