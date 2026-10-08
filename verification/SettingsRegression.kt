import android.content.FakePreferences
import org.lyaaz.fuckssaid.Settings as IdSettings

fun main() {
    val idPrefs = FakePreferences()
    val ids = IdSettings.getInstance(idPrefs)
    val gate = java.util.concurrent.CountDownLatch(1)
    val results = java.util.concurrent.ConcurrentLinkedQueue<String>()
    val threads = (1..64).map { Thread { gate.await(); results.add(ids.getOrCreateId()) } }
    threads.forEach { it.start() }; gate.countDown(); threads.forEach { it.join() }
    check(results.toSet().size == 1)
    check(ids.idForPackage == results.first())
    java.util.UUID.fromString(results.first())
    ids.idForPackage = "existing-user-value"
    check(ids.getOrCreateId() == "existing-user-value")
    println("Settings regressions: 64-thread ID initialization; existing ID preserved")
}
