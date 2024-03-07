package com.mydiscord

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mydiscord.di.injectModuleDependencies
import com.mydiscord.ui.theme.MydiscordTheme

class ParameterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //injectModuleDependencies(this@ParameterActivity)

        setContent {
            MydiscordTheme(darkTheme = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Content()
                }
            }
        }
    }
}

@Composable
fun Content(){

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable

fun HeaderParameter(modifier: Modifier = Modifier){

    Column (
        modifier = modifier.fillMaxSize(),
    ){
        Text(
            text = "Paramètres"
        )
        TextButton(onClick = { println("ok") }) {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MydiscordTheme(darkTheme = true) {
            HeaderParameter(Modifier)
        }

}
