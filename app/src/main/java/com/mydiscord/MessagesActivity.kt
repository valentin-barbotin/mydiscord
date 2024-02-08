package com.mydiscord

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mydiscord.di.injectModuleDependencies

class MessagesActivity: ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injectModuleDependencies(this@MessagesActivity)

    }


    @Composable
    @Preview(showBackground = true)
    private fun Content(){

    }
}