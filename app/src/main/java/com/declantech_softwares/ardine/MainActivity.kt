package com.declantech_softwares.ardine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

typealias MainActivityCoroutineScope = CoroutineScope
class MainActivity : AppCompatActivity(), CoroutineScope {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as App).appModule.single {
            this@MainActivity as MainActivityCoroutineScope
        }
        setContentView(R.layout.main_activity)
        val navController = findNavController(R.id.nav_host_fragment)
        nav_view?.setupWithNavController(navController)
    }

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

}
