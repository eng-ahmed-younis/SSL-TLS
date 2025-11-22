package com.example.ssl_tls.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.ssl_tls.BuildConfig
import com.example.ssl_tls.data.data_source.romote.service.RetrofitBuilder
import com.example.ssl_tls.presentation.theme.SSLTLSTheme
import kotlinx.coroutines.launch
/**
 * two type to make SSL PLANNING CERTIFICATE
 * 1- xml
 * 2- okhttp
 * */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Log.d("MainActivity", "onCreate: ${BuildConfig.BASE_URL}")

            SSLTLSTheme {
                val api = RetrofitBuilder.getSSLApiService()
                lifecycleScope.launch {
                    try {
                        Log.d("MainActivity", "onCreate: ${api.getSupardatingApi()}")
                    }catch (exception: Exception){
                        Log.d("MainActivity", "onCreate: ${exception.message}")
                        Log.d("MainActivity", "onCreate: ${exception.cause}")
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SSLTLSTheme {
        Greeting("Android")
    }
}