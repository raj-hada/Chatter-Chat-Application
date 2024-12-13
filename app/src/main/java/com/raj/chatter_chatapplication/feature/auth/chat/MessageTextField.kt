package com.raj.chatter_chatapplication.feature.auth.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.raj.chatter_chatapplication.R

@Composable
fun MessageTextField(
    onSendMessage: (String) -> Unit
) {
    val msg = remember {
        mutableStateOf("")
    }

    val hideKeyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {

        TextField(
            value = msg.value,
            onValueChange = {
                msg.value = it
            },
            modifier = Modifier
                .weight(1f)
                .padding(
                    start = 12.dp, end = 8.dp,
                    bottom = 8.dp
                )
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(50)
                ),
            shape = RoundedCornerShape(50),
            maxLines = 5,
            placeholder = { Text(text = "Message") },


            leadingIcon = {
                    IconButton(
                        modifier = Modifier.padding(8.dp)
                            .background(Color.Gray, CircleShape)
                            .size(40.dp),
                        onClick = {
                            if (msg.value.isNotBlank()){
                                onSendMessage(msg.value)
                                msg.value = ""
                                hideKeyboardController?.hide()
                            }
                        }) {
                        Icon(
                            painter = painterResource(R.drawable.attach),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                        )
                    }

            },



            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                hideKeyboardController?.hide()
            }),

            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            )
        )
        Box(
            modifier = Modifier.padding(bottom = 8.dp, end = 8.dp)
                .background(Color.Gray, CircleShape)
        ) {
            IconButton(onClick = {
                if (msg.value.isNotBlank()) {
                    onSendMessage(msg.value)
                    msg.value = ""
                    hideKeyboardController?.hide()
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "send",
                    modifier = Modifier.rotate(-45f).padding(8.dp).align(Alignment.Center)
                    )
            }
        }
    }
}