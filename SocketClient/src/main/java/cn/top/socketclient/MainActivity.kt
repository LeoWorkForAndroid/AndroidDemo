package cn.top.socketclient


import android.os.Build.HOST
import android.os.Build.VERSION_CODES.M
import android.os.Bundle
import android.provider.Telephony.Carriers.PORT
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.net.Socket
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.system.Os.socket

import android.system.Os.socket
import android.util.Log
import java.io.*



class MainActivity : AppCompatActivity() {


    lateinit var printWriter: PrintWriter
    lateinit var bufferreaderIn: BufferedReader
    private var receiveMsg: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mExecutorService = Executors.newCachedThreadPool()


        btn_connect.setOnClickListener {
            mExecutorService.execute(connectService())
        }

        btn_send.setOnClickListener {
            mExecutorService.execute(sendService(et_message.text.toString()))
        }

        btn_disconnect.setOnClickListener{
            mExecutorService.execute(sendService("0"))
        }

    }


    private inner class sendService internal constructor(private val msg: String) : Runnable {

        override fun run() {
            printWriter.println(this.msg)
        }
    }

    private fun receiveMsg() {
        try {
            while (true) {
                receiveMsg = bufferreaderIn.readLine();
                if (receiveMsg != null) {
                    Log.d("Socket", "receiveMsg:$receiveMsg")
                    runOnUiThread {
                        tv_result.text=tv_result.text.toString()+"\n"+receiveMsg
                    }
                }
            }
        } catch (e: IOException) {
            Log.e("Socket", "receiveMsg: ")
            e.printStackTrace()
        }

    }

    inner class connectService() : Runnable {
        private val Host = "192.168.0.106"
        private val Port = 9999
        override fun run() {
            val socket = Socket(Host, Port)
            socket.soTimeout = 6000
            printWriter = PrintWriter(BufferedWriter(OutputStreamWriter(socket.getOutputStream(), "UTF-8")), true)
            bufferreaderIn = BufferedReader(InputStreamReader(socket.getInputStream(), "UTF-8"))
            receiveMsg()
        }

    }
}
