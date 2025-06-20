package com.kalentire.test

import com.kalentire.test.command.TestCommand
import com.kalentire.test.listener.TestListener
import com.kalentire.test.module.TestModule
import com.ravingarinc.api.module.RavinPluginKotlin

/**
 * You'll be referencing these java docs for available methods and classes
 *
 * https://helpch.at/docs/1.20.1/overview-summary.html
 *
 * For 1.20.1. This means this plugin will be forward compatible with all versions 1.20.1 and above
 */
class TestPlugin : RavinPluginKotlin() {

    override fun loadModules() {
        addModule(TestModule::class.java)
        addModule(TestListener::class.java)

        // when loading, test module will have suspend load call first, then test listener.
        // when reloading, those above modules will be cancelled in reverse order, then suspendLoad() is called in
        // normal order.
    }

    override fun loadCommands() {
        // for every command you register, you must add it to the top level build.gradle.kts at the bottom.
        TestCommand(this).register() // this is all that is needed to create a command.
        // this code is executed after loadmodules()
    }

    // todo and when you're all done you can assemble the
    //  plugin jar with the gradle task `build`
}