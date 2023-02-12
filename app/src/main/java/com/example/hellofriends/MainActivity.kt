package com.example.hellofriends

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.Navigation
import com.example.hellofriends.route.Navigation
import com.example.hellofriends.ui.theme.HelloFriendsTheme
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.SocketAddress
import java.net.URI
import java.util.Objects

class MainActivity : ComponentActivity() {

    private lateinit var webSocketClient:WebSocketClient
    val uri: URI = URI(WEB_SOCKET_URL)
    companion object {
        const val WEB_SOCKET_URL = "wss://ws-feed.pro.coinbase.com"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloFriendsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Navigation(){}
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.e("TAG", "onStart: ", )
        webSocketClient = object : WebSocketClient(uri) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Log.e("TAG", "onOpen: ")
                subscribe()
            }

            override fun onMessage(message: String?) {
                TODO("Not yet implemented")
                Log.e("TAG", "onMessage: ${message}")
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                TODO("Not yet implemented")
            }

            override fun onError(ex: Exception?) {
                TODO("Not yet implemented")
                Log.e("TAG", "onError: ${ex?.message}")
            }

        }
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
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HelloFriendsTheme {
        Greeting("Android")
    }
}