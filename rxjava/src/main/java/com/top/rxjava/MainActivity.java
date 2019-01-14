package com.top.rxjava;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者：李阳
 * 时间：2018/12/10
 * 描述：
 *
 * @author Administrator
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    private Observable<String> getStringObservable() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                if (!e.isDisposed()) {
                    e.onNext("A");
                    //mRxOperatorsText.append("String emit : A \n");
                    Log.e(TAG, "String emit : A \n");
                    e.onNext("B");
                    //mRxOperatorsText.append("String emit : B \n");
                    Log.e(TAG, "String emit : B \n");
                    e.onNext("C");
                    //mRxOperatorsText.append("String emit : C \n");
                    Log.e(TAG, "String emit : C \n");
                }
            }
        });
    }

    private Observable<Integer> getIntegerObservable() {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                if (!e.isDisposed()) {
                    e.onNext(1);
                    //mRxOperatorsText.append("Integer emit : 1 \n");
                    Log.e(TAG, "Integer emit : 1 \n");
                    e.onNext(2);
                    //mRxOperatorsText.append("Integer emit : 2 \n");
                    Log.e(TAG, "Integer emit : 2 \n");
                    e.onNext(3);
                    //mRxOperatorsText.append("Integer emit : 3 \n");
                    Log.e(TAG, "Integer emit : 3 \n");
                    e.onNext(4);
                    //mRxOperatorsText.append("Integer emit : 4 \n");
                    Log.e(TAG, "Integer emit : 4 \n");
                    e.onNext(5);
                    //mRxOperatorsText.append("Integer emit : 5 \n");
                    Log.e(TAG, "Integer emit : 5 \n");
                }
            }
        });
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        new Thread(new Runnable() {
            @Override
            public void run() {
                Observable.just("one", "two", "three")
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                Log.e("TAG2", "accept : " + s + "\n");

                            }
                        });

            }
        }).start();


        //被观察者 Observable:发射器（上游事件）
        // 观察者 Observer 称为接收器（下游事件）
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
            }
        }).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return "This is result " + integer;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.e("TAG", "accept : " + s + "\n");
            }
        });


        Observable.zip(getStringObservable(), getIntegerObservable(), new BiFunction<String, Integer, String>() {
            @Override
            public String apply(String s, Integer integer) throws Exception {
                return s + integer;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.e(TAG, "zip : accept :" + s + "\n");
            }
        });


        Observable.concat(Observable.just(1, 2, 3), Observable.just(4, 5, 6, 7))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG + "concat", "concat : " + integer + "\n");
                    }
                });


        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);

            }
        }).flatMap(new Function<Integer, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Integer integer) throws Exception {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("I am value " + integer);
                }
                int delayTime = (int) (1 + Math.random() * 10);
                return Observable.fromIterable(list).delay(delayTime, TimeUnit.MILLISECONDS);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Log.e(TAG + "flatMap", "flatMap : accept : " + (String) o + "\n");

                    }
                });


        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).concatMap(new Function<Integer, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Integer integer) throws Exception {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("I am value " + integer);
                }
                int delayTime = (int) (1 + Math.random() * 10);
                return Observable.fromIterable(list).delay(delayTime, TimeUnit.MILLISECONDS);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Log.e(TAG + "concatMap", "flatMap : accept : " + o + "\n");
                    }
                });

        Observable.just(1, 1, 2, 2)
                .distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG + "distinct", "distinct : " + integer + "\n");

                    }
                });


        Observable.just(1, 20, 65, -5, 7, 19)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {

                        return integer >= 10;
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.e(TAG + "filter", "filter : " + integer + "\n");

            }
        });


        Observable.just(1, 2, 3, 4, 5)
                .buffer(3, 2)
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {
                        Log.e(TAG + "buffer", "buffer size : " + integers.size() + "\n");

                        Log.e(TAG + "buffer", "buffer value : ");
                        for (Integer i : integers) {
                            Log.e(TAG, i + "");
                        }
                        Log.e(TAG, "\n");

                    }
                });


        Log.e(TAG + "timer", "timer start : " + getNowStrTime() + "\n");

        Observable.timer(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.e(TAG + "timer", "timer : " + aLong + " at " + getNowStrTime() + "\n");

                    }
                });

        Log.e(TAG + "interval", "interval start : " + getNowStrTime() + "\n");


        Disposable subscribe = Observable.interval(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.e(TAG + "interval", "interval :" + aLong + " at " + getNowStrTime() + "\n");

                    }
                });

        //销毁上方的循环
        //subscribe.dispose();
    }


    public static String getNowStrTime() {
        long time = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(time));
    }
}
