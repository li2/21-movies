package me.li2.movies

import androidx.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import me.li2.movies.di.MainComponent.appModule
import me.li2.movies.di.networkModule
import me.li2.movies.util.Constants
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.conf.ConfigurableKodein
import org.kodein.di.generic.instance

class App : MultiDexApplication(), KodeinAware {
    override val kodein = ConfigurableKodein(mutable = true)
    private val constants: Constants by instance()
    private var overrideModule: Kodein.Module? = null

    override fun onCreate() {
        super.onCreate()
        context = this
        configDebugBuild()
        setupKodein()
        constants.init()
        setupFabric()
    }

    private fun setupKodein() {
        kodein.apply {
            clear()
            addImport(androidXModule(this@App))
            addImport(networkModule)
            addImport(appModule, allowOverride = true)
            overrideModule?.let {
                addImport(it, true)
            }
        }
    }

    private fun setupFabric() {
        Fabric.with(this, Crashlytics())
    }

    companion object {
        lateinit var context: App
    }
}
