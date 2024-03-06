package com.example.mydiscordxml

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mydiscordxml.di.injectModuleDependencies
import com.example.mydiscordxml.viewmodel.ChannelViewModel
import com.example.mydiscordxml.viewmodel.GuildsViewModel
import com.example.mydiscordxml.views.adapters.ChannelsListAdapter
import com.example.mydiscordxml.views.adapters.MessagesListAdapter
import dev.kord.core.cache.data.MessageData
import dev.kord.core.entity.Message
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.entity.channel.TopGuildChannel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.InputStream
import java.nio.file.Path
import java.nio.file.Paths

class ChannelActivity : AppCompatActivity() {
    private val guildsViewModel: GuildsViewModel by viewModel()
    private val channelViewModel: ChannelViewModel by viewModel()

    private lateinit var messagesRv: RecyclerView
    private lateinit var channelName: TextView
    private lateinit var messageInput: EditText
    private lateinit var sendMessageBtn: Button
    private lateinit var sendFileBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel)

        injectModuleDependencies(this@ChannelActivity)

        this.messagesRv = findViewById(R.id.messages_rv)
        this.channelName = findViewById(R.id.channel_name)
        this.messageInput = findViewById(R.id.message_input)
        this.sendMessageBtn = findViewById(R.id.send_message_btn)
        this.sendFileBtn = findViewById(R.id.send_file_btn)

        this.lifecycleScope.launch {
            val id = this@ChannelActivity.guildsViewModel.selectedGuild.value ?: return@launch
            val guild = this@ChannelActivity.guildsViewModel.guilds.value?.first() {
                it.id == id
            }

            if (guild != null) {
                this@ChannelActivity.channelViewModel.refreshChannel()
                this@ChannelActivity.channelViewModel.messages.observe(this@ChannelActivity) {
                    this@ChannelActivity.setupChannelsList(it)
                }
                this@ChannelActivity.channelViewModel.channel.observe(this@ChannelActivity) {
                    this@ChannelActivity.channelName.text = it.name
                }

                this@ChannelActivity.sendMessageBtn.setOnClickListener {
                    val message = this@ChannelActivity.messageInput.text.toString()
                    if (message.isNotEmpty()) {
                        this@ChannelActivity.channelViewModel.sendMessage(message)
                        this@ChannelActivity.messageInput.text.clear()
                        // TODO: hide keyboard
                    }
                }

                this@ChannelActivity.sendFileBtn.setOnClickListener {
                    this@ChannelActivity.openFilePicker()
                }
            }
        }
    }

    private fun setupChannelsList(channels: List<MessageData>) {
        val adapter = MessagesListAdapter( channels,this::openAttachment)
        messagesRv.layoutManager = LinearLayoutManager( this)
        messagesRv.adapter = adapter
    }

    private fun openAttachment(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.component = ComponentName("com.android.chrome", "com.google.android.apps.chrome.Main")
        intent.addCategory(Intent.CATEGORY_BROWSABLE)
        intent.addCategory(Intent.CATEGORY_DEFAULT)

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            intent.component = ComponentName("com.android.browser", "com.android.browser.BrowserActivity")
            startActivity(intent)
        }
    }

    private val pickFile = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                handleSelectedFile(uri)
            }
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }

        println("Opening file picker...")

        pickFile.launch(intent)
    }

    private fun handleSelectedFile(uri: Uri) {
        val fileName = getFileName(uri)

        val path = this.openAndWriteToFile(uri)
        this.channelViewModel.sendFileMessage(path, fileName)

        Toast.makeText(this, "Fichier sélectionné : $fileName", Toast.LENGTH_SHORT).show()
    }
    private fun openAndWriteToFile(uri: Uri): Path {
        val inputStream: InputStream? = contentResolver.openInputStream(uri)

        if (inputStream != null) {
            val destinationFile = File.createTempFile("temp", null, cacheDir)
            destinationFile.writeBytes(inputStream.readBytes())

            inputStream.close()
            return Paths.get(destinationFile.absolutePath)
        } else {
            throw Exception("Failed to open InputStream for the URI")
        }
    }

    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        if (result == null) {
            result = uri.lastPathSegment
        }
        return result.orEmpty()
    }
}