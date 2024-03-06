package com.example.mydiscordxml.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.mydiscordxml.R
import dev.kord.core.entity.Guild

class GuildsListAdapter(
    private val guilds: List<Guild>,
    private val onClickHandler: OnGuildClicked
): RecyclerView.Adapter<GuildsListAdapter.GuildCellViewHolder>() {
    inner class GuildCellViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var guild_cell_ib: ImageButton = itemView.findViewById(R.id.guild_cell_ib)
    }

    override fun getItemCount(): Int {
        return guilds.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuildCellViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.guild_cell_layout, parent, false)
        return GuildCellViewHolder(view)
    }

    override fun onBindViewHolder(holder: GuildCellViewHolder, position: Int) {
        val guild = this.guilds[position]
        val holderContext = holder.itemView.context

        holder.guild_cell_ib.load(guild.icon?.cdnUrl?.toUrl() ?: "https://cdn.discordapp.com/icons/897746126772502569/8cc146043482be4eb78d522dcb25545f.webp?size=240")

        holder.guild_cell_ib.setOnClickListener {
            onClickHandler.displayGuild(guild)
        }
    }
}

interface OnGuildClicked {
    fun displayGuild(guild: Guild)
}