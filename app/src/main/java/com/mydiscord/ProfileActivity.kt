package com.mydiscord

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mydiscord.ui.theme.MydiscordTheme
import com.mydiscord.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CardDefaults.elevatedCardElevation
import coil.compose.AsyncImage
import com.mydiscord.di.injectModuleDependencies
import com.mydiscord.ui.components.cardModifier
import kotlinx.coroutines.*

class ProfileActivity : ComponentActivity() {

    private val profileViewModel: ProfileViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injectModuleDependencies(this@ProfileActivity)

        setContent {
            MydiscordTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Content(profileViewModel)
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun About(modifier: Modifier = Modifier) {
    Card(
        elevation = elevatedCardElevation(),
        modifier = modifier.cardModifier()
    ) {
        Surface(
            modifier = Modifier.padding(15.dp),
            color = Color.Transparent
        ) {
            Column {
                Text(
                    text = "About me",
                )
                Text(
                    text = "Hello world",
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Member since",
                )
                Text(
                    text = "Unknown",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
//@Preview(showBackground = true)
private fun Content(profileViewModel: ProfileViewModel = viewModel()) {
    val user = profileViewModel.getUser()

    val username = user?.username ?: "Unknown"
    val avatar = user?.avatar.toString() ?: ""
    val banner = user?.banner.toString() ?: ""

    //val avatar = "https://media.discordapp.net/attachments/899987990313594880/1199666461111758878/20240124_103313.jpg?ex=65c35f75&is=65b0ea75&hm=ad0da9789cf66672e3040c2c9e46ec54d1e5e2150f6a1fbb4bb371b62dc75a58&=&format=webp&width=1078&height=1356"
    //val banner = "https://media1.tenor.com/m/wlYwELBQkI8AAAAd/mukbang-food.gif"

    Column {
        UserImages(Modifier, avatar, banner)

        Spacer(Modifier.height(8.dp))

        Me(Modifier, username)
        //Spacer(Modifier.height(2.dp))
        UserActivity(Modifier)
        //Spacer(Modifier.height(2.dp))
        About(Modifier)
    }
}

@Composable
@Preview(showBackground = true)
private fun UserImages(modifier: Modifier = Modifier, avatar: String = "", banner: String = "") {
    val imageSize = 60.dp

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        AsyncImage(
            model = banner,
            contentDescription = "banner",
            Modifier
                .height(60.dp)
                .fillMaxWidth()
        )
        Surface(
            modifier = Modifier
                .offset(
                    y = (-(imageSize / 20)),
                    x = (imageSize / 4),
                )
                .border(
                    BorderStroke(1.dp, Color.Black),
                    CircleShape
                )
                .offset(x = (imageSize / 20)),
            color = Color.Transparent
        ) {
            AsyncImage(
                model = avatar,
                contentDescription = "photo",
                Modifier
                    .size(imageSize)
                    .clip(CircleShape)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun Me(modifier: Modifier = Modifier, username: String = "John Doe") {
    Card(
        elevation = elevatedCardElevation(),
        modifier = modifier.cardModifier()
    ) {
        Surface(
            modifier = Modifier.padding(15.dp),
            color = Color.Transparent
        ) {
            Column {
                Text(
                    text = username,
                )
                Text(
                    text = ".userid",
                    modifier = Modifier,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun UserActivity(modifier: Modifier = Modifier) {
    Card(
        elevation = elevatedCardElevation(),
        modifier = modifier.cardModifier()
    ) {
        Surface(
            modifier = Modifier.padding(15.dp),
            color = Color.Transparent
        ) {
            Column {
                Text(
                    text = "todo",
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MydiscordTheme {
        Column {
            Card(
                elevation = elevatedCardElevation(),
                modifier = Modifier.cardModifier()
            ) {
                Surface(
                    modifier = Modifier.padding(15.dp),
                    color = Color.Transparent
                ) {
                    Text(text = "Hello World!")
                }
            }
        }
    }
}