package com.example.mydiscordxml.views.adapters

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.mydiscordxml.R
import dev.kord.core.cache.data.MessageData
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.entity.channel.TopGuildChannel
import java.text.DateFormat
import java.util.Date
import java.util.Locale

class MessagesListAdapter(
    private val messages: List<MessageData>,
    private val onAttachementClickHandler: (String) -> Unit,
): RecyclerView.Adapter<MessagesListAdapter.MessageCellViewHolder>() {
    inner class MessageCellViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var message_cell_author: TextView = itemView.findViewById(R.id.message_cell_author)
        var message_cell_date: TextView = itemView.findViewById(R.id.message_cell_date)
        var message_cell_content: TextView = itemView.findViewById(R.id.message_cell_content)
        var message_cell_image: ImageView = itemView.findViewById(R.id.message_cell_image)
        //var message_cell_image: TextView = itemView.findViewById(R.id.message_cell_image)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageCellViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_cell_layout, parent, false)
        return MessageCellViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageCellViewHolder, position: Int) {
        val message = this.messages[position]
        val holderContext = holder.itemView.context

        val locale = Locale.FRENCH
        val date = DateFormat.getDateInstance(DateFormat.MEDIUM, locale).format(
            Date(message.timestamp.epochSeconds * 1000)
        )
        val time = DateFormat.getTimeInstance(DateFormat.SHORT, locale).format(
            Date(message.timestamp.epochSeconds * 1000)
        )

        val messageDate = String.format("%s %s", date, time);

        // avatars.getOrDefault(message.author.id, "https://cdn.discordapp.com/embed/avatars/0.png"),
        // only first attachment is supported now
        val attachment = message.attachments.firstOrNull()
        if (attachment != null &&
            attachment.contentType.value?.startsWith("image") == true
        ) {
            holder.message_cell_image.load(attachment.url)
            holder.message_cell_image.setOnClickListener {
                onAttachementClickHandler(attachment.url)
                holder.message_cell_content.text = message.content
            }
            holder.message_cell_image.visibility = View.VISIBLE
        } else {
            holder.message_cell_image.visibility = View.GONE
        }

        holder.message_cell_author.text = message.author.username
        holder.message_cell_date.text = messageDate
        holder.message_cell_content.text = message.content
    }
}

