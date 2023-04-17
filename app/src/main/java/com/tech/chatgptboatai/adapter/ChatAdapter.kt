package com.tech.chatgptboatai.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tech.chatgptboatai.R
import com.tech.chatgptboatai.model.MessageModel


class ChatAdapter(
    private var messageList: ArrayList<MessageModel>,
    var context: Context,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var SENDER_VIEW_TYPE = 1
    private var RECEIVER_VIEW_TYPE = 2
    private var RECEIVER_IMAGE_TYPE = 3
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == SENDER_VIEW_TYPE) {
            val view = LayoutInflater.from(context).inflate(R.layout.sample_sender, parent, false)
            return SenderViewHolder(view)
        } else if (viewType == RECEIVER_IMAGE_TYPE) {
            val view = LayoutInflater.from(context).inflate(R.layout.image_layout, parent, false)
            return ReceiverImageViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.sample_receiver, parent, false)
            return ReceiverViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val messageModel = messageList[position]

        if (holder.javaClass == SenderViewHolder::class.java) {
            (holder as SenderViewHolder).senderText.text = messageModel.message
        } else if(holder.javaClass == ReceiverImageViewHolder::class.java){

            Glide.with(context)
                .load(messageModel.message)
                .into((holder as ReceiverImageViewHolder).imageView)
        }
        else {
            (holder as ReceiverViewHolder).receiverText.text = messageModel.message
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {

        if (messageList[position].isUser) {
            return SENDER_VIEW_TYPE
        } else if (messageList[position].isImage) {
            return RECEIVER_IMAGE_TYPE
        } else {
            return RECEIVER_VIEW_TYPE
        }
    }

    internal class ReceiverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var receiverText: TextView
        var receiverTime: TextView

        init {
            receiverText = itemView.findViewById(R.id.receiverText)
            receiverTime = itemView.findViewById(R.id.receiverTime)
        }
    }
    internal class ReceiverImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView:ImageView

        init {
            imageView = itemView.findViewById(R.id.imageView)
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
