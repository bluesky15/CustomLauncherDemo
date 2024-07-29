package com.example.customlauncherdemo

import android.app.role.RoleManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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


class CustomLauncherActivity : ComponentActivity() {
    private var activityResult: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initOnActivityResult()
        setContent {
            CustomLauncherDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun initOnActivityResult() {
        activityResult =
            this@CustomLauncherActivity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    this@CustomLauncherActivity.finish()
                } else {
                    Toast.makeText(
                        this@CustomLauncherActivity,
                        getString(R.string.please_select_custom_launcher),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }


    private fun chooseLauncher() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            enableComponentState()
            val roleManager =
                this@CustomLauncherActivity.getSystemService(Context.ROLE_SERVICE) as RoleManager
            roleManager.isRoleAvailable(RoleManager.ROLE_HOME)
            val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_HOME)
            activityResult?.launch(intent)
        } else {
            enableComponentState()
            startActivity(Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME))
            finish()
        }
    }

    private fun enableComponentState() {
        val p = packageManager
        val cN = ComponentName(this, CustomHomeActivity::class.java)
        p.setComponentEnabledSetting(
            cN,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    @Composable
    fun MainScreen(modifier: Modifier = Modifier) {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Button(onClick = { chooseLauncher() }) {
                Text(text = stringResource(id = R.string.set_custom_launcher))
            }
        }

    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        CustomLauncherDemoTheme {
            MainScreen()
        }
    }
}



