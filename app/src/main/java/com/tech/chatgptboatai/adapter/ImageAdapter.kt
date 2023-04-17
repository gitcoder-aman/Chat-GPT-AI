package com.tech.chatgptboatai.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tech.chatgptboatai.R
import com.tech.chatgptboatai.model.MessageModel


class ImageAdapter(
    private var list: ArrayList<MessageModel>,
    var context: Context, ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var SENDER_VIEW_TYPE = 1
    private var RECEIVER_VIEW_TYPE = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == SENDER_VIEW_TYPE) {
            val view = LayoutInflater.from(context).inflate(R.layout.sample_sender, parent, false)
            SenderViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.image_layout, parent, false)
            ReceiverViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val messageModel = list[position]
        if (holder.javaClass == SenderViewHolder::class.java) {
            (holder as SenderViewHolder).senderText.text = messageModel.message
        } else {
            Glide.with(holder.itemView.context)
                .load(messageModel.message)
                .into((holder as ReceiverViewHolder).receiveImage)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {

        return if (list[position].isImage) {
            SENDER_VIEW_TYPE
        } else {
            RECEIVER_VIEW_TYPE
        }
    }

    internal class ReceiverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var receiveImage: ImageView

        init {
            receiveImage = itemView.findViewById(R.id.imageView)
        }
    }

    internal class SenderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var senderText: TextView
        var senderTime: TextView

        init {
            senderText = itemView.findViewById(R.id.senderText)
            senderTime = itemView.findViewById(R.id.senderTime)
        }
    }
}
