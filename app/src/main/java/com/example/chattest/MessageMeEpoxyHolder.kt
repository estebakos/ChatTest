package com.example.chattest

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@SuppressLint("NonConstantResourceId")
@EpoxyModelClass(layout = R.layout.item_chat_me)
abstract class MessageMeEpoxyHolder : EpoxyModelWithHolder<MessageHolder>() {

    @EpoxyAttribute
    lateinit var item: MessageItem

    @EpoxyAttribute
    lateinit var itemClickListener: View.OnClickListener

    override fun bind(holder: MessageHolder) {
        with(holder) {
            txtName.text = item.name
            txtMessage.text = item.text
            txtDate.text = item.date
            container.setOnClickListener(itemClickListener)
        }
    }
}

class MessageMeHolder : KotlinEpoxyHolder() {
    val txtName by bind<TextView>(R.id.txtName)
    val txtMessage by bind<TextView>(R.id.txtMessage)
    val txtDate by bind<TextView>(R.id.txtDate)
}
