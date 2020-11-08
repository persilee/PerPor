package net.lishaoy.library.executor

import android.os.Handler
import android.os.Looper
import androidx.annotation.IntRange
import net.lishaoy.library.log.PerLog
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock

object PerExecutor {

    private var isPaused: Boolean = false
    private var perExecutor: ThreadPoolExecutor
    private var lock: ReentrantLock = ReentrantLock()
    private val pauseCondition: Condition
    private val mainHandler = Handler(Looper.getMainLooper())

    init {
        pauseCondition = lock.newCondition()
        val cupCount = Runtime.getRuntime().availableProcessors()
        val corePoolSize = cupCount + 1
        val maxPoolSize = cupCount * 2 + 1
        val blockingQueue: PriorityBlockingQueue<out Runnable> = PriorityBlockingQueue()
        val keepAliveTime = 30L
        val unit = TimeUnit.SECONDS
        val sequence = AtomicLong()
        val threadFactory = ThreadFactory {
            val thread = Thread(it)
            thread.name = "per-executor${sequence.getAndIncrement()}"
            return@ThreadFactory thread
        }
        perExecutor = object : ThreadPoolExecutor(
            corePoolSize,
            maxPoolSize,
            keepAliveTime,
            unit,
            blockingQueue as BlockingQueue<Runnable>,
            threadFactory
        ) {
            override fun beforeExecute(t: Thread?, r: Runnable?) {
                if (isPaused) {
                    lock.lock()
                    try {
                        pauseCondition.await()
                    } finally {
                        lock.unlock()
                    }
                }
            }

            override fun afterExecute(r: Runnable?, t: Throwable?) {

            }
        }
    }

    @JvmOverloads
    fun execute(@IntRange(from = 0, to = 10) priority: Int = 10, runnable: Runnable) {
        perExecutor.execute(PriorityRunnable(priority, runnable))
    }

    @JvmOverloads
    fun execute(@IntRange(from = 0, to = 10) priority: Int = 10, runnable: Callable<*>) {
        perExecutor.execute(PriorityRunnable(priority, runnable))
    }

    abstract class Callable<T> : Runnable {
        override fun run() {
            mainHandler.post { onPrepare() }
            val t = onBackground()
            mainHandler.post { onCompleted(t) }
        }
        open fun onPrepare(){}
        abstract fun onBackground() : T
        abstract fun onCompleted(t: T)
    }

    class PriorityRunnable(private val priority: Int, private val runnable: Runnable) : Runnable,
        Comparable<PriorityRunnable> {
        override fun run() {
            runnable.run()
        }

        override fun compareTo(other: PriorityRunnable): Int {
            return if (this.priority < other.priority) 1 else if (this.priority > other.priority) -1 else 0
        }

    }

    @Synchronized
    fun pause() {
        isPaused = true
        PerLog.e("executor is paused")
    }

    @Synchronized
    fun resume() {
        isPaused = false
        lock.lock()
        try {
            pauseCondition.signalAll()
        } finally {
            lock.unlock()
        }
        PerLog.e("executor is resumed")
    }

}