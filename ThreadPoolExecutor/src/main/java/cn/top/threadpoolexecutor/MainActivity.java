package cn.top.threadpoolexecutor;


import android.os.Bundle;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    Executor executor;

    ThreadPoolExecutor poolExecutor;

    int corePoolSize;
    int maximumPoolSize;
    long keepAliveTime;
    TimeUnit unit;
    BlockingQueue<Runnable> workQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * NCPU = CPU的数量
         * UCPU = 期望对CPU的使用率 0 ≤ UCPU ≤ 1
         * W/C = 等待时间与计算时间的比率
         * 如果希望处理器达到理想的使用率，那么线程池的最优大小为：
         * 线程池大小=NCPU *UCPU(1+W/C)
         */
        int ncpus = Runtime.getRuntime().availableProcessors();

        corePoolSize = (int) ((ncpus * 0.1) * (1 + 0.6));


        //poolExecutor = new ThreadPoolExecutor();

        ExecutorService es = new ThreadPoolExecutor(
                5,
                5,
                0L,
                TimeUnit.MICROSECONDS,
                new SynchronousQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return null;
            }
        });


        class MyTask1 implements Runnable {

            @Override
            public void run() {
                System.out.println(System.currentTimeMillis() + "Thrad ID:" + Thread.currentThread().getId());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }


}