package com.mydiscord.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DiscordNavBar(
    selectedNavItem: NavItem,
    onNavItemClicked: (NavItem) -> Unit
) {
    BottomAppBar(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            NavItem.values().forEach { navItem ->
                DiscordNavButton(
                    text = navItem.name,
                    isSelected = navItem == selectedNavItem,
                    onClick = { onNavItemClicked(navItem) }
                )
            }
        }
    }
}

enum class NavItem {
    SERVEURS,
    MESSAGES,
    TOI
}
@Composable
private fun DiscordNavButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Text(
        text = text,
        color = if (isSelected) Color.Gray else Color.Black,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewDiscordNavBar() {
    DiscordNavBar(selectedNavItem = NavItem.SERVEURS) {}
}
