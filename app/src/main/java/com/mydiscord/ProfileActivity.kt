package com.mydiscord

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mydiscord.ui.theme.MydiscordTheme
import com.mydiscord.viewmodel.ProfileViewModel
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileActivity : ComponentActivity() {

    private val profileViewModel: ProfileViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runBlocking {
            val auth = this@ProfileActivity.profileViewModel.loginWithToken("MTE5NzI4NTg2NDc3OTMwNTA2Mg.GH2OyU.F4T9LBUf0a6bo-5v7iOrQJU_SeFLN7AnGWyS1I")
            auth.onFailure {
                println("Failed to login")
                println(it.message)
            }

            auth.onSuccess {
                println("Logged in")
                println(this@ProfileActivity.profileViewModel.getUsername())
            }
        }

        setContent {
            MydiscordTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    About("John Doe")
                }
            }
        }
    }
}

@Composable
fun About(name: String, modifier: Modifier = Modifier) {
    Column {
        Text(
            text = "About me",
            modifier = modifier
        )
        Text(
            text = "$name!",
            modifier = modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    MydiscordTheme {
        About("John Doe")
    }
}