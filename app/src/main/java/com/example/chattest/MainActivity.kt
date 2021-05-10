package com.example.chattest

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.example.chattest.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class MainActivity : AppCompatActivity(), MessageEpoxyController.OnInteractionListener {

    private val messageEpoxyController = MessageEpoxyController()

    private val fireStoreChat by lazy {
        FirebaseFirestore.getInstance().collection(COLLECTION_KEY).document(DOCUMENT_KEY)
    }

    private val messageList = mutableListOf<MessageItem>()

    // Binding
    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        realtimeUpdateListener()
        binding.btnSend.setOnClickListener{
            hideKeyboard()
            sendMessage()
            binding.etMessage.setText("")
        }
        setupRecyclerView()
    }

    // region Views
    private fun setupRecyclerView() = with(binding) {
        messageEpoxyController.setOnItemClickListener(this@MainActivity)
        val linearLayoutManager = LinearLayoutManager(this@MainActivity)

        rvChat.apply {
            layoutManager = linearLayoutManager
            setController(messageEpoxyController)
        }

        messageEpoxyController.adapter.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                linearLayoutManager.scrollToPosition(messageList.size -1)
            }
        })
    }

    override fun onItemClick(messageItem: MessageItem) {

    }

    private fun sendMessage() {
        val newMessage = mapOf(
            NAME_FIELD to "Michel",
            TEXT_FIELD to binding.etMessage.text.toString(),
            DATE_FIELD to Date().toString(),
            EMAIL_FIELD to "esteban0721@outlook.com"
        )
        fireStoreChat.set(newMessage)
            .addOnSuccessListener {
                Toast.makeText(this@MainActivity, "Message Sent", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e -> Log.e("ERROR", e.message ?: "") }
    }

    private fun realtimeUpdateListener() {
        fireStoreChat.addSnapshotListener { documentSnapshot, e ->
            when {
                e?.message != null -> Log.e("ERROR", e.message ?: "")
                documentSnapshot != null && documentSnapshot.exists() -> {
                    with(documentSnapshot) {
                        messageList.add(
                            MessageItem(
                                name = if (data?.get(NAME_FIELD) != null) data?.get(NAME_FIELD) as String else "",
                                email = if (data?.get(EMAIL_FIELD) != null) data?.get(EMAIL_FIELD) as String else "",
                                date = if (data?.get(DATE_FIELD) != null) data?.get(DATE_FIELD) as String else "",
                                text = if (data?.get(TEXT_FIELD) != null) data?.get(TEXT_FIELD) as String else ""
                            )
                        )

                        renderMessageList()
                    }
                }
            }
        }
    }

    private fun renderMessageList() {
        val messageByDate = messageList.groupBy { it.date }
        messageEpoxyController.setData(messageByDate, R.string.app_name)

    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }


    // endregion

    companion object {
        const val COLLECTION_KEY = "Chat"
        const val DOCUMENT_KEY = "Message"
        const val NAME_FIELD = "Name"
        const val TEXT_FIELD = "Text"
        const val DATE_FIELD = "Date"
        const val EMAIL_FIELD = "Email"
    }

}