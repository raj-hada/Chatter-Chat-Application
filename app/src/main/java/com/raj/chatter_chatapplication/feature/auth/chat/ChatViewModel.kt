package com.raj.chatter_chatapplication.feature.auth.chat

import android.util.Log
import androidx.compose.animation.core.rememberTransition
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.raj.chatter_chatapplication.model.Channel
import com.raj.chatter_chatapplication.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {


    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val message = _messages.asStateFlow()
    private val db = Firebase.database.reference
    private val _channel = MutableStateFlow<Channel?>(null)
    val channel = _channel.asStateFlow()

    fun sendMessage(channelID: String, messageText: String) {
        val message = Message(
            db.push().key ?: UUID.randomUUID().toString(),
            Firebase.auth.currentUser?.uid ?: "",
            messageText,
            System.currentTimeMillis(),
            Firebase.auth.currentUser?.displayName ?: "",
            null,
            null
        )

        db.child("messages").child(channelID).push().setValue(message)
    }

    fun listenForMessages(channelID: String) {
        db.child("messages").child(channelID).orderByChild("createdAt")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<Message>()
                    snapshot.children.forEach { data ->
                        val message = data.getValue(Message::class.java)
                        message?.let {
                            list.add(it)
                        }
                    }
                    _messages.value = list
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
    }

    // Function to get channel name from Firebase
    fun getChannelName(channelId: String, onComplete: (String?) -> Unit) {
        db.child("channel").child(channelId).get()
            .addOnSuccessListener { snapshot ->
                val channelName = snapshot.getValue(String::class.java)
                onComplete(channelName)  // Return the channel name through the callback
            }
            .addOnFailureListener { exception ->
                // Handle failure here (e.g., log or show an error message)
                Log.e("GetChannelError", "Error fetching channel name: ${exception.message}")
                onComplete(null)  // Return null if there's an error
            }
    }
}
