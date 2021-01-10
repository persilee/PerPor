package net.lishaoy.library.util

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.os.Process
import android.os.StatFs
import android.text.format.Formatter
import androidx.annotation.RequiresApi
import net.lishaoy.library.BuildConfig
import net.lishaoy.library.log.PerLog
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

object CrashHandler {

    private var CRASH_DIR: String = "crash_dir"

    fun init() {
        Thread.setDefaultUncaughtExceptionHandler(CaughtExceptionHandler())
    }

    private class CaughtExceptionHandler : Thread.UncaughtExceptionHandler {

        private val formatter = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA)
        private val launchTime = formatter.format(Date())
        private val defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        private var context = AppGlobals.get()!!

        @RequiresApi(Build.VERSION_CODES.P)
        override fun uncaughtException(t: Thread, e: Throwable) {
            if (!handlerException(e) && defaultExceptionHandler != null) {
                defaultExceptionHandler.uncaughtException(t, e)
            }
            restartApp()
        }

        private fun restartApp() {
            val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
            intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(intent)

            Process.killProcess(Process.myPid())
            System.exit(10)
        }

        @RequiresApi(Build.VERSION_CODES.P)
        private fun handlerException(e: Throwable?): Boolean {
            if (e == null) return false
            val log = collectDeviceInfo(e)
            if (BuildConfig.DEBUG) {
                PerLog.e(log)
            }
            saveCrashInfoToFile(log)
            return true
        }

        private fun saveCrashInfoToFile(log: String) {
            val crashDir = File(context.cacheDir, CRASH_DIR)
            if (!crashDir.exists()) {
                crashDir.mkdir()
            }
            val crashFile = File(crashDir, "${formatter.format(Date())}-crash.txt")
            crashFile.createNewFile()
            val fos = FileOutputStream(crashFile)
            try {
                fos.write(log.toByteArray())
                fos.flush()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                fos.close()
            }
        }

        /**
         * 手机设备信息
         * 如： 系统版本、线程名、前后台、使用时长、App版本、升级渠道、CPU架构、内存信息、permission权限
         */
        @RequiresApi(Build.VERSION_CODES.P)
        private fun collectDeviceInfo(e: Throwable): String {
            val sb = StringBuffer()
            sb.append("brand = ${Build.BRAND}\n")
            sb.append("rom = ${Build.MODEL}\n")
            sb.append("os = ${Build.VERSION.RELEASE}\n")
            sb.append("sdk = ${Build.VERSION.SDK_INT}\n")
            sb.append("launch_time = ${launchTime}\n")
            sb.append("crash_time = ${formatter.format(Date())}\n")
            sb.append("foreground = ${PerActivityManager.instance.front}\n")
            sb.append("thread = ${Thread.currentThread().name}\n")
            sb.append("cpu_arch = ${Build.CPU_ABI}\n")

            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            sb.append("version_code = ${packageInfo.longVersionCode}\n")
            sb.append("version_name = ${packageInfo.versionName}\n")
            sb.append("package_name = ${packageInfo.packageName}\n")
            sb.append("requested_permission = ${Arrays.toString(packageInfo.requestedPermissions)}\n")

            val memoryInfo = ActivityManager.MemoryInfo()
            val ams = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            ams.getMemoryInfo(memoryInfo)
            sb.append("avail_memory = ${Formatter.formatFileSize(context, memoryInfo.availMem)}\n")
            sb.append("total_memory = ${Formatter.formatFileSize(context, memoryInfo.totalMem)}\n")

            val file = Environment.getExternalStorageDirectory()
            val statFs = StatFs(file.path)
            val availableSize = statFs.availableBlocks * statFs.blockSize
            sb.append(
                "avail_storage=${Formatter.formatFileSize(
                    context,
                    availableSize.toLong()
                )}\n"
            )

            val write = StringWriter()
            val printWriter = PrintWriter(write)
            e.printStackTrace(printWriter)
            var cause = e.cause
            while (cause != null) {
                cause.printStackTrace(printWriter)
                cause = cause.cause
            }
            printWriter.close()
            sb.append(write.toString())

            return sb.toString()
        }

    }

    fun crashFiles(): Array<File>? {
        return File(AppGlobals.get()?.cacheDir, CRASH_DIR).listFiles()
    }

}