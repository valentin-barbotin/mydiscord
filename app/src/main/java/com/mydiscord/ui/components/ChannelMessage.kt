package com.mydiscord.ui.components

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.FileUtils
import android.text.format.Formatter.formatFileSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.AsyncImage
import com.mydiscord.myImageLoader
import com.mydiscord.ui.theme.DarkColorScheme
import dev.kord.core.cache.data.MessageData
import java.lang.String.format
import java.text.DateFormat
import java.util.Date
import java.util.Locale


@Composable
fun GuildChannelMessage(
    message: MessageData,
    avatar: String,
    onClick: () -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(
        horizontal = 16.dp,
        vertical = 2.dp
    ),
    openAttachment: (String) -> Unit,
    context: Context = LocalContext.current
) {
    val locale = Locale.FRENCH
    val date = DateFormat.getDateInstance(DateFormat.MEDIUM, locale).format(
        Date(message.timestamp.epochSeconds * 1000)
    )
    val time = DateFormat.getTimeInstance(DateFormat.SHORT, locale).format(
        Date(message.timestamp.epochSeconds * 1000)
    )

    val messageDate = format("%s %s", date, time);

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(0.dp),
        colors = ButtonDefaults.textButtonColors(
            contentColor = DarkColorScheme.tertiary,
        ),
        contentPadding = contentPadding,
    ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
            ) {
                println(message)
                AsyncImage(
                    model = avatar,
                    contentDescription = "photo",
                    alignment = Alignment.TopCenter,
                    modifier = Modifier
                        .size(60.dp)
                        .padding(2.dp)
                        .clip(CircleShape)
                )
                Column(
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .padding(start = 10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(bottom = 5.dp, end = 10.dp)
                            .fillMaxWidth(),
                    ) {
                        Text(message.author.globalName.value ?: message.author.username)
                        Text(messageDate)
                    }
                    Text(message.content)
                    for (attachment in message.attachments) {
                        if (attachment.contentType.value?.startsWith("image") == true
                            || attachment.contentType.value?.startsWith("video") == true) {
                            AsyncImage(
                                model = attachment.url,
                                contentDescription = "media",
                                imageLoader = myImageLoader(context),
                                Modifier
                                    .padding(2.dp)
                                    .clip(RoundedCornerShape(5.dp))
                                    .clickable {
                                        openAttachment(attachment.url)
                                    }
                            )
                        } else {
                            Button(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(attachment.url))
                                    intent.component = ComponentName("com.android.chrome", "com.google.android.apps.chrome.Main")
                                    intent.addCategory(Intent.CATEGORY_BROWSABLE)
                                    intent.addCategory(Intent.CATEGORY_DEFAULT)
                                    try {
                                        startActivity(context, intent, null)
                                    } catch (e: ActivityNotFoundException) {
                                        intent.component = ComponentName("com.android.browser", "com.android.browser.BrowserActivity")
                                        startActivity(context, intent, null)
                                    }
                                },
                                shape = RoundedCornerShape(5.dp),
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = DarkColorScheme.tertiary,
                                ),
                                contentPadding = contentPadding,
                                modifier = Modifier
                                    .padding(2.dp)
                                    .clip(RoundedCornerShape(5.dp))
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(text = attachment.filename, fontWeight = FontWeight.Bold)
                                    Text(text = formatFileSize(context, attachment.size.toLong()))
                                }
                            }
                        }
                    }
                }
            }
    }
}
