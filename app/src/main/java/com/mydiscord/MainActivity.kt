package com.mydiscord

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mydiscord.di.injectModuleDependencies
import com.mydiscord.ui.theme.MydiscordTheme
import com.mydiscord.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val profileViewModel: ProfileViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injectModuleDependencies(this@MainActivity)

        setContent {
            MydiscordTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Column {
                        Text("test1")
                        Greeting("Android")
                        ProfileButton { openProfileActivity() }
                    }
                }
            }
        }
    }

    private fun openProfileActivity() {
        startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
    }
}

@Composable
fun ProfileButton(onClick: () -> Unit) {
    Button(onClick = { onClick() }) {
        Text("Profile")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MydiscordTheme {
        Greeting("Android")
    }
}