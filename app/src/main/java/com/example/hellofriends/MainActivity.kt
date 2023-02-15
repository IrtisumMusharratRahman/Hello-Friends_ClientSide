package com.example.hellofriends

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.hellofriends.route.Navigation
import com.example.hellofriends.ui.theme.HelloFriendsTheme
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI

class MainActivity : ComponentActivity() {

    private lateinit var webSocketClient:WebSocketClient
    val uri: URI = URI(WEB_SOCKET_URL)

    companion object {
        const val WEB_SOCKET_URL = "wss://ws-feed.pro.coinbase.com"
//        const val WEB_SOCKET_URL = "ws://localhost:8088/websocket"
    }

    var  msg:MutableState<String> = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloFriendsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
//                    Navigation(){}
                    Testing(msg.value)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.e("TAG", "onStart: ")
        webSocketClient = object : WebSocketClient(uri) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Log.e("TAG", "onOpen: ")
                subscribe()
//                webSocketClient.send("websocket")
            }

            override fun onMessage(message: String?) {
                Log.e("TAG", "onMessage: ${message}")
                val str = message.orEmpty()
                msg.value=str.take(str.length)
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Log.e("TAG", "onClose: ")
                unsubscribe()
            }

            override fun onError(ex: Exception?) {
                Log.e("TAG", "onError: ${ex?.message}")
            }

        }
        webSocketClient.connect()
    }

    override fun onStop() {
        super.onStop()
        webSocketClient.close()
    }

    private fun subscribe() {
        webSocketClient.send(
            "{\n" +
                    "    \"type\": \"subscribe\",\n" +
                    "    \"channels\": [{ \"name\": \"ticker\", \"product_ids\": [\"BTC-EUR\"] }]\n" +
                    "}"
        )
    }

    private fun unsubscribe(){
        webSocketClient.send(
            "{\n" +
                    "    \"type\": \"unsubscribe\",\n" +
                    "    \"channels\": [\"ticker\"]\n" +
                    "}"
        )
    }
}

@Composable
fun Testing(msg:String) {
    var message by remember {
        mutableStateOf("", policy = structuralEqualityPolicy())
    }
    if (msg!=null){
        message=msg
    }
    Text(text = message)
}
