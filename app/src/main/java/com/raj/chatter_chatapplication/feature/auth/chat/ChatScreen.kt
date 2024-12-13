package com.raj.chatter_chatapplication.feature.auth.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.raj.chatter_chatapplication.model.Message
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController, channelId: String) {

    val viewModel: ChatViewModel = hiltViewModel()
    var channelName by remember {
        mutableStateOf("Loading...")
    }

    LaunchedEffect(channelId) {
        viewModel.getChannelName(channelId){ channel ->
            channelName = channel ?: "Unknown Channel"
        }
    }



        Scaffold(
        topBar = {
            ChannelAppBar(navController, channelName)
        }
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {

            LaunchedEffect(key1 = true) {
                viewModel.listenForMessages(channelId)
            }
            val messages = viewModel.message.collectAsState()
            ChatMessages(
                messages = messages.value,
                onSendMessage = { message ->
                    viewModel.sendMessage(channelId, message)
                },

            )
        }
    }

}

@Composable
fun ChatMessages(
    messages: List<Message>,
    onSendMessage: (String) -> Unit
) {


    val gradient = Brush.horizontalGradient(
        listOf(Color(0xffa18cd1), Color(0xfffbc2eb)),
        startX = 0.0f,
        endX = 1000.0f,
        tileMode = TileMode.Repeated
    )


    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f) // Takes up all available space except for the Row below
                .padding(bottom = 1.dp),
           // reverseLayout = true// Prevent overlap with the TextField
        ) {
            items(messages) { message ->
                ChatBubble(message = message)
            }
        }
        MessageTextField(onSendMessage)
    }
}


@Composable
fun TrailingIcon(
    msg: MutableState<String>,
    onSendMessage: (String) -> Unit,
    onVoiceClick: () -> Unit
) {
    if(msg.value.isEmpty()){
        IconButton(onClick = {
            onVoiceClick()
        }) {
            Icon(
                imageVector = Icons.Default.Call,
                contentDescription = null
            )
        }
    }else{
        Box(
            modifier = Modifier.background(Color.LightGray, CircleShape).size(40.dp)
        ) {
            IconButton(onClick = {
                onSendMessage(msg.value)
            }) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = null,
                    modifier = Modifier
                        .rotate(-45F)
                )
            }
        }
    }
}

@Composable
fun ChatBubble(message: Message) {
    val isCurrentUser = message.senderId == Firebase.auth.currentUser?.uid
    val bubbleColor = if (isCurrentUser) {
        Color.Gray // Color for outgoing message,
    } else {
        Color.LightGray // Color for incoming message
    }
    val shape = if(isCurrentUser) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp)
    }else{
        RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp, bottomEnd = 16.dp)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 18.dp),
        horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start // Align based on sender
    ) {
        // Message bubble
        Box(
            modifier = Modifier
                .background(color = bubbleColor, shape = shape) // Rounded corners
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
        ) {
            Text(
                text = message.message,
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                maxLines = Int.MAX_VALUE // Allow message to wrap to next line
            )
        }

        // Timestamp
        Text(
            text = formatCurrentTime(message.createdAt),
            color = Color.DarkGray,
            fontSize = 10.sp,
            modifier = Modifier.padding(top = 4.dp) // Add spacing between bubble and timestamp
        )
    }
}


fun formatCurrentTime(time: Long): String {
    val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return dateFormat.format(Date(time))
}


@Preview
@Composable
fun per(){
    ChatScreen(rememberNavController(),"4665")
}


