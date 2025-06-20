package com.kalentire.test.listener

import com.ravingarinc.api.module.RavinPlugin
import com.ravingarinc.api.module.SuspendingModuleListener
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent

/**
 * Please read TestModule before this class.
 *
 * A SuspendingModuleListener has the Listener interface implemented and will automatically
 */
class TestListener(plugin: RavinPlugin) : SuspendingModuleListener(TestListener::class.java, plugin, isRequired = true) {
    // You can access other modules like so; plugin.getModule(TestModule::class.java)

    override suspend fun suspendLoad() {
        // read suspendLoad comments in TestModule

        super.suspendLoad() // since this is a listener, this method must be called otherwise the listener won't be registered.
    }

    override suspend fun suspendCancel() {
        super.suspendCancel()
        // Since this is a listener, the super method must be called otherwise this listener may not be unregistered
    }

    /**
     * Priority is at what point does this function execute in relation to other handlers (possibly implemented by this plugin
     * or by another plugin). EventPriority.HIGHEST means it will be executed LAST (relatively), whilst EventPriority.LOWEST means it will
     * be executed first.
     *
     * Some events (not all) can be cancelled. If the `ignoreCancelled` option is true, this means that if a preceding
     * event handler function cancels the event (by calling event.isCancelled = true), then THIS event handler WILL NOT
     * execute. By default, ignoreCancelled will be equal to false and the given event handler will always execute.
     *
     * It's important to consider how another plugin may modify the event beforehand.
     *
     * Plugins can also create and call their own events too, you can do so by calling using the function;
     * `Bukkit.getPluginManager().callEvent(event)`
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    fun handleDamageEvent(event: EntityDamageEvent) {
        // through the magic of annotations and such, this is how you create a handler which is executed when an
        // EntityDamageEvent is called. This handler is executed on the main minecraft server thread (always single threaded and synchronous)
    }

    @EventHandler // default priority of normal, ignoreCancelled = false
    fun handleEntityDeath(event: EntityDeathEvent) {

    }
}