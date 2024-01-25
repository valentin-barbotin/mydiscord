package com.mydiscord.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

fun Modifier.cardModifier() = fillMaxWidth()
        .padding(15.dp)
        .clip(RoundedCornerShape(8.dp))


