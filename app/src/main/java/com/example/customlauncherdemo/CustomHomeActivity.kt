package com.example.customlauncherdemo

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.customlauncherdemo.ui.theme.CustomLauncherDemoTheme


class CustomHomeActivity : ComponentActivity() {
    private var pkgAppsList: List<ResolveInfo>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        onBackPressedDispatcher.addCallback(this) {
            // Back is pressed
            // Do nothing
        }
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        pkgAppsList =
            this@CustomHomeActivity.packageManager.queryIntentActivities(mainIntent, 0)
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
        Box(modifier = modifier
            .fillMaxSize()
            .padding(top = 24.dp)) {
            LazyVerticalGrid(modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(5),
                content = {
                    items(pkgAppsList!!) {
                        Column(modifier = Modifier
                            .padding(4.dp)
                            .width(75.dp)
                            .height(85.dp)
                            .clickable {
                                launchApplication(it)
                            }) {
                            val icon =
                                rememberAsyncImagePainter(it.activityInfo.loadIcon(packageManager))
                            Image(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .width(55.dp)
                                    .height(55.dp),
                                painter = icon,
                                contentDescription = null
                            )
                            Text(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                text = it.activityInfo.loadLabel(packageManager).toString(),
                                maxLines = 1,
                                fontSize = 12.sp,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            )
            Button(modifier = Modifier.align(alignment = Alignment.BottomCenter), onClick = {
                disableLauncher()
            }) {
                Text(text = stringResource(id = R.string.reset_launcher))
            }
        }
    }

    private fun launchApplication(it: ResolveInfo) {
        try {
            val launchIntent =
                packageManager.getLaunchIntentForPackage(it.activityInfo.packageName)
            startActivity(launchIntent)
        } catch (e: Exception) {
            Toast.makeText(this@CustomHomeActivity, "Application not found", Toast.LENGTH_SHORT)
                .show()
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