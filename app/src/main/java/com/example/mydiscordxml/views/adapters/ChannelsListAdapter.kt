package com.example.mydiscordxml.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mydiscordxml.R
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.entity.channel.TopGuildChannel

class ChannelsListAdapter(
    private val channels: List<TopGuildChannel>,
    private val onClickHandler: OnChannelClicked
): RecyclerView.Adapter<ChannelsListAdapter.ChannelCellViewHolder>() {
    inner class ChannelCellViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var channel_cell_tv: TextView = itemView.findViewById(R.id.channel_cell_tv)
    }

    override fun getItemCount(): Int {
        return channels.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelCellViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.channel_cell_layout, parent, false)
        return ChannelCellViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChannelCellViewHolder, position: Int) {
        val channel = this.channels[position]
        val holderContext = holder.itemView.context

        holder.channel_cell_tv.text = channel.name

        if (channel is TextChannel) {
            holder.channel_cell_tv.setOnClickListener {
                onClickHandler.channelClicked(channel)
            }
        }
    }
}

interface OnChannelClicked {
    fun channelClicked(channel: TopGuildChannel)
}