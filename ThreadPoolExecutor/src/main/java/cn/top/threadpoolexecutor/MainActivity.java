package cn.top.threadpoolexecutor;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    private static final String TAG ="MainActivity";

    Executor executor;

    ThreadPoolExecutor poolExecutor;

    //核心池大小
    int corePoolSize;
    //线程池最大能创建的线程数目大小
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
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5,
                10,
                200,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(10));
        for (int i = 0; i < 15; i++) {
            MyTaskDemo myTaskDemo = new MyTaskDemo(i);
            executor.execute(myTaskDemo);
            Log.e(TAG,"线程池中线程数目：" + executor.getPoolSize() + "，队列中等待执行的任务数目：" +
                    executor.getQueue().size() + "，已执行玩别的任务数目：" + executor.getCompletedTaskCount());
        }
    }

    class MyTaskDemo implements Runnable {

        private int taskNum;

        public MyTaskDemo(int num) {
            this.taskNum = num;
        }

        @Override
        public void run() {
            //Log.e(TAG,System.currentTimeMillis() + "Thrad ID:" + taskNum);
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.e(TAG,"task " + taskNum + " 执行完毕!");
        }
    }
}