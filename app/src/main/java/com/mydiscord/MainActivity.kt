package com.mydiscord

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.mydiscord.di.injectModuleDependencies
import com.mydiscord.ui.theme.MydiscordTheme
import com.mydiscord.viewmodel.MainViewModel
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injectModuleDependencies(this@MainActivity)

        setContent {
            MydiscordTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    // center the column
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                    ) {
                        val givenToken = remember { mutableStateOf(TextFieldValue()) }
                        TextField(
                            value = givenToken.value,
                            onValueChange = {
                                givenToken.value = it
                            },
                            label = { Text("Token") }
                        )
                        Button(onClick = {
                            val token = givenToken.value.text
                            login()
                            //login(token)
                        },
                            enabled = givenToken.value.text.isNotEmpty()
                        ) {
                            Text("Login")
                        }
                    }
                }
            }
        }
    }

    // selfbot
    // MTE5NzMwMTgxODUzNTk3NzE1MA.G16fLj.oJ4j-gVoaRLscG0xCvBlzN4HMGXmGd5NJrxO8c

    // mydiiscord (bot)
    // MTE5NzI4NTg2NDc3OTMwNTA2Mg.GH2OyU.F4T9LBUf0a6bo-5v7iOrQJU_SeFLN7AnGWyS1I
    private fun login(token: String = "MTE5NzI4NTg2NDc3OTMwNTA2Mg.GH2OyU.F4T9LBUf0a6bo-5v7iOrQJU_SeFLN7AnGWyS1I") {
        lifecycleScope.launch {
            val auth = this@MainActivity.mainViewModel.loginWithToken(token)
            auth.onFailure {
                println("Failed to login")
                println(it.message)
            }

            auth.onSuccess {
                println("Logged in")
                openGuildsActivity()
            //openProfileActivity()
            }
        }
    }

    private fun openProfileActivity() {
        startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
    }

    private fun openGuildsActivity() {
        startActivity(Intent(this@MainActivity, GuildsActivity::class.java))
    }
}
