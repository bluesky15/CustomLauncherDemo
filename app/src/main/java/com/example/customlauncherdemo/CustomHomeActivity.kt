package com.example.customlauncherdemo

import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.customlauncherdemo.ui.theme.CustomLauncherDemoTheme

class CustomHomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        onBackPressedDispatcher.addCallback(this) {
            // Back is pressed
            // Do nothing
        }
        setContent {
            CustomLauncherDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    @Composable
    fun HomeScreen(modifier: Modifier = Modifier) {
        Box(modifier = modifier.fillMaxSize()) {
            Button(modifier = Modifier.align(alignment = Alignment.BottomCenter), onClick = {
                disableLauncher()
            }) {
                Text(text = stringResource(id = R.string.reset_launcher))
            }
        }
    }

    private fun disableLauncher() {
        val pm = packageManager
        val componentName = ComponentName(this, CustomHomeActivity::class.java)
        pm.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
        this@CustomHomeActivity.finish()
    }

    @Composable
    @Preview(showBackground = true)
    fun HomeScreenPreview() {
        CustomLauncherDemoTheme {
            HomeScreen()
        }
    }
}