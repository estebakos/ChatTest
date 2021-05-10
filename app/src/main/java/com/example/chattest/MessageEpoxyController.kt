package com.example.chattest

import android.view.View
import com.airbnb.epoxy.Typed2EpoxyController

class MessageEpoxyController : Typed2EpoxyController<Map<String, List<MessageItem>>, Int>() {

    private var onInteractionListener: OnInteractionListener? = null

    override fun buildModels(messageItems: Map<String, List<MessageItem>>, emptyMessage: Int) {
        if (!messageItems.isNullOrEmpty()) {
            messageItems.forEach {
                it.value.forEach { item ->
                    if(item.name != "Michel") {
                        messageEpoxyHolder {
                            id(item.hashCode())
                            item(item)
                            itemClickListener(View.OnClickListener {
                                onInteractionListener?.onItemClick(item)
                            })
                        }
                    } else {
                        messageMeEpoxyHolder {
                            id(item.hashCode())
                            item(item)
                            itemClickListener(View.OnClickListener {
                                onInteractionListener?.onItemClick(item)
                            })
                        }
                    }
                }
            }
        }
    }

    fun setOnItemClickListener(onInteractionListener: OnInteractionListener) {
        this.onInteractionListener = onInteractionListener
    }

    interface OnInteractionListener {
        fun onItemClick(messageItem: MessageItem)
    }

    companion object {
        private const val EMPTY_ITEM_ID = "EMPTY_ITEM_ID"
    }
}
