package com.mydiscord.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mydiscord.ui.theme.DiscordLight

@Composable
@Preview(showBackground = true)
fun GuildChannelHeader(
    modifier: Modifier = Modifier,
    text: String = "channel",
    onClick: () -> Unit = {},
    onClickBack: () -> Unit = {},
    onClickSearch: () -> Unit = {}
) {
    Row(
        modifier = modifier
            //.padding(10.dp)
            .defaultMinSize(minHeight = 50.dp)
            .background(DiscordLight)
            .fillMaxWidth()
    ) {
        IconButton(onClick = onClickBack) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = "back",
                modifier = Modifier
                    .size(20.dp)
            )
        }
        TextButton(
            onClick = onClick,
        ) {
            Text(text)
        }
        IconButton(
            onClick = onClickSearch,
            modifier.weight(2f)
                .width(20.dp)
        ) {
            Icon(
                Icons.Filled.Search,
                contentDescription = "search",
                modifier = Modifier
                    .size(20.dp)
                // TODO: put search icon at the end
            )
        }
    }
}