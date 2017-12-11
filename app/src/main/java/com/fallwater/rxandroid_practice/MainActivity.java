package com.fallwater.rxandroid_practice;

import com.jakewharton.rxbinding2.widget.RxTextView;

import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.observables.GroupedObservable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.Timed;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "tag";

    Unbinder mUnbinder;

    @BindView(R.id.text01)
    EditText mTextView01;

    List<String> words = Arrays.asList(
            "the",
            "quick",
            "brown",
            "fox",
            "jumped",
            "over",
            "the",
            "lazy",
            "dog"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);
        initListener();
        rxjava2();
    }

    private void rxjava2() {
        //创建
//        just();
//        range();
//        zipWith();
//        zipWith2();

        //变换
//        flatMap();
//        flatMap2();
//        flatMap3();
//        flatMap4();
//        testAmb();
//        testDefaultEmpty();
//        switchEmpty();
//        skipUntil();
//        skipWhile();
//        takeUntil();
//        takeWhile();
//        map();
//        buffer();
//        scan();
//        groupBy();
//        window();
//        cast();
//        skip();

        //过滤
//        debounce();
//        debounce2();
//        filter();
//        take();
//        takeLast();

        //组合
//        startWith();
//        merge();
//        combineLastest();
//        zip();
//        switchOnNext();

        //错误处理
//        onErrorReturn();
//        onErrorResumeNext();
//        onExceptionResumeReturn();
//        retry();

        //辅助操作
//        delay();
//        delaySubcription();
//        do2();
//        materialize();
//        dematerialize();
//        timeInterval();
//        timeStamp();

        //条件操作符
//        amb();
//        defaultEmpty();
//        switchIfEmpty();

        //布尔操作符列表
//        all();
//        contains();
//        isEmpty();
//        sequenceEqual();

        //算术和聚合
//        concat();
//        count();
//        reduce();
//        collect();
//        toList_();
//        toSortedList();
//        toMap();

        //连接操作符
//        connectOrPublish();
//        replay();
//        refCount();

        //阻塞操作符
//        forEach();
//        blockIterable();
//        blockingSingle();
//        blockingSubscribe();
    }

    private void blockingSubscribe() {
        Observable.just(1, 2, 3)
                .blockingSubscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, integer.toString());
                    }
                });
    }

    private void blockingSingle() {
        /**
         * 如果只发送一条数据，则把这条数据返回
         */
        Log.d(TAG, Observable.just("1").blockingSingle("3"));
    }

    private void blockIterable() {
        Iterable<String> integers = Observable.just("1", "2", "3")
                .blockingIterable();

        for (String i : integers) {
            Log.d(TAG, i);
        }
    }

    private void forEach() {
        Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        for (int i = 0; i < 5; i++) {
                            if (i == 2) {
                                e.onError(new Throwable("error"));
                            }
                            e.onNext(i);
                        }
                        e.onComplete();
                    }
                })
//                .just(1,2,3)
                /**
                 * 减配版subscribe，去除onComplete,OnError
                 */
                .forEach(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, integer.toString());
                    }
                });


    }

    private void refCount() {
        ConnectableObservable<Long> connectableObservable = Observable.just(1L, 2L, 3L)
                .publish();
        connectableObservable.subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.d(TAG, aLong.toString());
            }
        });

        Log.d(TAG, "connect");
        connectableObservable.connect();

        /**
         * 重新转换成Observable
         */
        Observable<Long> observable = connectableObservable.refCount();
        observable.subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.d(TAG, aLong.toString());
            }
        });
    }

    private void replay() {

        ConnectableObservable<Long> connectableObservable = Observable.just(1L, 2L, 3L)
                /**
                 * 缓存数据源产生的所有数据
                 */
                .replay();
        /**
         * publish订阅者收不到消息
         */
//                .publish();
        connectableObservable.delaySubscription(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, aLong.toString());
                    }
                });

        connectableObservable.connect();
    }

    private void connectOrPublish() {
        /**
         * connectObservable只有在connect时开始发送数据
         */
        Observable observable = Observable.just(1L, 2L, 3L);
        final ConnectableObservable<Long> connectableObservable = observable.publish();
        Log.d(TAG, "subscribe");
        connectableObservable
//                .delay(2, TimeUnit.SECONDS)
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "accept:" + aLong);
                    }
                });

        mTextView01.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "begin to connect");
                connectableObservable.connect();
            }
        }, 2000);
    }

    private void toMap() {
        Observable.just(1, 2, 3, 4)
                /**
                 * 自定义key，合成map
                 */
                .toMap(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return "key" + integer;
                    }
                }).subscribe(new Consumer<Map<String, Integer>>() {
            @Override
            public void accept(Map<String, Integer> stringIntegerMap) throws Exception {
                Log.d(TAG, stringIntegerMap.toString());
            }
        });
    }

    private void toSortedList() {
        Observable.just("1", "10", "100", "2")
                /**
                 * 返回排序后的List，需要传入Comparator
                 */
                .toSortedList(new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return Integer.valueOf(o1) - Integer.valueOf(o2);
                    }
                }).subscribe(new Consumer<List<String>>() {
            @Override
            public void accept(List<String> strings) throws Exception {
                Log.d(TAG, strings.toString());
            }
        });
    }

    private void toList_() {
        Observable.just(1, 2, 3, 4)
                /**
                 * 转换成List
                 */
                .toList()
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {
                        Log.d(TAG, integers.toString());
                    }
                });
    }

    private void collect() {
        Observable.just("1", "2", "3", "4")
                /**
                 * 插入类型并返回
                 */
                .collect(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return 10 + "string";
                    }
                }, new BiConsumer<String, String>() {
                    @Override
                    public void accept(String integer, String s) throws Exception {
                        Log.d(TAG, "call string:" + integer + ",resource:" + s);
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String integer) throws Exception {

                        Log.d(TAG, integer.toString());
                    }
                });
    }

    private void reduce() {
        Observable.just(1L, 2L, 3L)
                /**
                 * 先拿前两条数据做处理，数据全部处理完后，没有新的数据
                 * 再返回给订阅者
                 */
                .reduce(new BiFunction<Long, Long, Long>() {
                    @Override
                    public Long apply(Long aLong, Long aLong2) throws Exception {
//                        Log.d(TAG, "1:" + aLong.toString() + ",2:" + aLong2);
                        return aLong + aLong2;
                    }
                }).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.d(TAG, aLong.toString());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d(TAG, throwable.toString());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                Log.d(TAG, "onComplete");
            }
        });

//        Observable.just("1","2","3","4")
//                .reduce(new BiFunction<String, String, String>() {
//                    @Override
//                    public String apply(@NonNull String s, @NonNull String s2) throws Exception {
//                        Log.e(TAG, "apply: "+s +" "+ s2);
//
//                        return "返回值";
//                    }
//                }).subscribe(new Consumer<String>() {
//            @Override
//            public void accept(@NonNull String s) throws Exception {
//                Log.e(TAG, "accept: "+s);
//
//            }
//        });
    }

    private void count() {
        Observable.just(1, 2, 3)
                /**
                 * 事件数目
                 */
                .count()
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, aLong.toString());
                    }
                });
    }

    private void concat() {
        Observable
                /**
                 * 直接顺序连接事件
                 */
                .concat(Observable.just(1, 2), Observable.just(3, 4))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, integer.toString());
                    }
                });

        /**
         * zipWith函数组合事件
         */
//        Observable.just(1, 2)
//                .zipWith(Observable.just(3, 4), new BiFunction<Integer, Integer, String>() {
//                    @Override
//                    public String apply(Integer integer, Integer integer2) throws Exception {
//                        return integer + integer2 + "";
//                    }
//                })
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//                        Log.d(TAG, s);
//                    }
//                });
    }

    private void sequenceEqual() {
        Observable
                /**
                 * 事件序列是否相等
                 */
                .sequenceEqual(Observable.just(1, 2), Observable.just(1, 2))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        Log.d(TAG, aBoolean.toString());
                    }
                });
    }

    private void isEmpty() {
        Observable.just(1, 2, 3)
                /**
                 * 是否为空
                 */
                .isEmpty()
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        Log.d(TAG, aBoolean.toString());
                    }
                });
    }

    private void contains() {
        Observable.just("1", "2", "abdc")
                /**
                 * 是否包含该元素
                 */
                .contains("1")
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        Log.d(TAG, aBoolean.toString());
                    }
                });
    }

    private void all() {
        Observable.just(1, 2, 3)
                /**
                 * 条件为假时终止发送事件
                 */
                .all(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        Log.d(TAG, integer.toString());
                        return integer != 2;
                    }
                }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                Log.d(TAG, aBoolean.toString());
            }
        });
    }

    private void switchIfEmpty() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onComplete();
            }
        })
                /**
                 * 数据源未发射OnNext，切换至该流程
                 */
                .switchIfEmpty(Observable.just(1))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, integer.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "throw");
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(TAG, "onComplete");
                    }
                });
    }

    private void defaultEmpty() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onComplete();
            }
        })
                /**
                 * onNext未发送时，发送Empty时间替换，正常走onComplete
                 */
                .defaultIfEmpty("empty")
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, throwable.toString());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(TAG, "onComplete");
                    }
                });
    }

    private void amb() {
        Observable o1 = Observable.just(1, 2, 3)
                .delay(1, TimeUnit.SECONDS);
        Observable o2 = Observable.just(4, 5, 6);
        /**
         * 发送最先发送数据的Observable
         */
        Observable.ambArray(o1, o2)
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Log.d(TAG, o.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(TAG, "onComplete");
                    }
                });

    }

    /**
     * 加上时间戳
     */
    private void timeStamp() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .timestamp()
                .subscribe(new Consumer<Timed<Long>>() {
                    @Override
                    public void accept(Timed<Long> longTimed) throws Exception {
                        Log.d(TAG, "time:" + longTimed.time()
                                + ",value:" + longTimed.value());
                    }
                });
    }

    private void timeInterval() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; i < 10; i++) {
                    e.onNext(i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        })
                /**
                 * 包装value，加上间隔时间
                 */
                .timeInterval(TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<Timed<Integer>>() {
                    @Override
                    public void accept(Timed<Integer> integerTimed) throws Exception {
                        /**
                         * Timed.value获取value
                         */
                        Log.d(TAG, "time:" + integerTimed.time(TimeUnit.SECONDS)
                                + ",value:" + integerTimed.value());

                    }
                });
    }

    private void dematerialize() {
        /**
         * 有问题
         */
//        Observable.just(1, 2)
//                .materialize()
//                .dematerialize()
//                .subscribe(new Consumer<Integer>() {
//                    @Override
//                    public void accept(Integer integer) throws Exception {
//                        Log.d(TAG, integer.toString());
//                    }
//                });
    }

    private void materialize() {
        Observable.just(1, 2)
                .materialize()
                .subscribe(new Consumer<Notification<Integer>>() {
                    @Override
                    public void accept(Notification<Integer> integerNotification) throws Exception {
                        Log.d(TAG, (integerNotification.isOnNext() ? "onNext"
                                : integerNotification.isOnComplete() ? "onComplete" : "onError"));
                    }
                });
    }

    private void do2() {
        Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        for (int i = 0; i < 3; i++) {
                            if (i == 1) {
                                e.onError(new Throwable("error"));
                            }
                            e.onNext(i);
                        }
                        e.onComplete();
                    }
                })
//                .just(1, 2)
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "doOnNext:" + integer.toString());
                    }
                }).

                doAfterNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "doAfterNext:" + integer.toString());
                    }
                }).

                doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(TAG, "onComplete");
                    }
                }).

                doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(TAG, "doFinally");
                    }
                }).

                doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(TAG, "doAfterTerminate");
                    }
                }).

                doOnEach(new Consumer<Notification<Integer>>() {
                    @Override
                    public void accept(Notification<Integer> integerNotification) throws Exception {
                        Log.d(TAG, "doOnEach:" + (integerNotification.isOnNext() ? "onNext" :
                                (integerNotification.isOnComplete() ? "onComplete" : "onError")));
                    }
                }).

                doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        Log.d(TAG, "doOnSubscribe");
                    }
                }).

                doOnLifecycle(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        Log.d(TAG, "doOnLifecycle" + disposable.isDisposed());
                    }
                }, new

                                      Action() {
                                          @Override
                                          public void run() throws Exception {
                                              Log.d(TAG, "doOnLifecycle run:");
                                          }
                                      }).

                subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "consumer:" + integer);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, throwable.toString());
                    }
                });
    }

    private void delaySubcription() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .delaySubscription(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long integer) throws Exception {
                        Log.d(TAG, integer.toString());
                    }
                });
    }

    /**
     * delay的核心是事件产生后的处理，此时已经订阅
     */
    private void delay() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; i < 5; i++) {
                    if (i == 1) {
                        e.onError(new Throwable("error"));
                    }
                    e.onNext(i);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }

                e.onComplete();
            }
        })
                /**
                 * delayError为true直接抛出onError，false照常delay
                 */
                .delay(3, TimeUnit.SECONDS, false)
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, integer.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, throwable.toString());
                    }
                });
    }

    private void retry() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; i < 5; i++) {
                    if (i == 3) {
                        e.onError(new Throwable("error"));
                    }
                    e.onNext(i);
                }
                e.onComplete();
            }
        })
//                /**
//                 * 一直重试
//                 */
//                .retry()
//                /**
//                 * 重试3次
//                 */
//                .retry(3)
//                .retry(new Predicate<Throwable>() {
//                    @Override
//                    public boolean test(Throwable throwable) throws Exception {
//                        /**
//                         * true表示继续重新订阅
//                         * false表示取消订阅
//                         */
//                        return true;
//                    }
//                })
//                .retry(new BiPredicate<Integer, Throwable>() {
//                    /**
//                     *
//                     * @param integer 第几次重新订阅
//                     * @param throwable
//                     * @return
//                     * @throws Exception
//                     */
//                    @Override
//                    public boolean test(Integer integer, Throwable throwable) throws Exception {
//                        Log.d(TAG, "BiPredicate:" + integer.toString());
//                        return integer < 10;
//                    }
//                })
//                .retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
//                    @Override
//                    public ObservableSource<?> apply(Observable<Throwable> throwableObservable)
//                            throws Exception {
//                        return throwableObservable.flatMap(
//                                new Function<Throwable, ObservableSource<?>>() {
//                                    @Override
//                                    public ObservableSource<?> apply(Throwable throwable)
//                                            throws Exception {
//                                        /**
//                                         * 返回的onError则取消订阅
//                                         */
//                                        return Observable.error(throwable);
//                                        /**
//                                         * 返回onNext事件则重新订阅
//                                         */
////                                        return Observable.just(1);
//                                    }
//                                });
//                    }
//                })
                /**
                 * 返回true 取消重试
                 * 返回false 继续重试
                 */
                .retryUntil(new BooleanSupplier() {
                    @Override
                    public boolean getAsBoolean() throws Exception {
                        return true;
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, integer.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, throwable.toString());
                    }
                });
    }

    private void onExceptionResumeReturn() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; i < 5; i++) {
                    if (i == 3) {
                        e.onError(new Exception("exception occurs..."));
                    }
                    e.onNext(i);
                }
                e.onComplete();
            }
            /**
             * 这里把生命周期完整交给了onExceptionResumeNext，
             * 也就是后续onNext,onComplete,onError的触发都被接管了
             */
        }).onExceptionResumeNext(new Observable<Integer>() {
            @Override
            protected void subscribeActual(Observer<? super Integer> observer) {
                observer.onNext(100);
                observer.onComplete();
//                observer.onError(new Throwable("onExceptionResumeNext throwable..."));
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, integer.toString());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d(TAG, throwable.toString());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                Log.d(TAG, "onComplete");
            }
        });
    }

    private void onErrorResumeNext() {
        Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> e) throws Exception {
                for (long i = 0; i < 5L; i++) {
                    if (i == 4) {
                        e.onError(new Throwable("oh,error occurs..."));
                    }
                    e.onNext(i);
                }
                e.onComplete();
            }
        })
                .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends Long>>() {
                    @Override
                    public ObservableSource<? extends Long> apply(Throwable throwable)
                            throws Exception {
                        return Observable.just(100L, 101L);
                    }
                }).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.d(TAG, aLong.toString());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d(TAG, throwable.toString());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                Log.d(TAG, "onComplete");
            }
        });
    }

    private void onErrorReturn() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                for (int i = 0; i < 5; i++) {
                    if (i == 2) {
                        e.onError(new Throwable("oh,my god,error occurs..."));
                    }
                    e.onNext(i + "");
                }
                e.onComplete();
            }
            /**
             * 出现错误后，正常处理，但是剩下的事件不发送
             */
        }).onErrorReturn(new Function<Throwable, String>() {
            @Override
            public String apply(Throwable throwable) throws Exception {
                return "error handled...";
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d(TAG, throwable.toString());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                Log.d(TAG, "onComplete");
            }
        });
    }

    private void skip() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                /**
                 * 跳过前面数据
                 */
                .skip(1)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, aLong.toString());
                    }
                });
    }

    private void switchOnNext() {
        Observable.switchOnNext(
                Observable.create(new ObservableOnSubscribe<ObservableSource<? extends String>>() {
                    @Override
                    public void subscribe(ObservableEmitter<ObservableSource<? extends String>> e)
                            throws Exception {
                        for (int i = 0; i < 3; i++) {
                            final int finalI = i;
                            e.onNext(Observable.just("1-2", "1-1"));
                            e.onNext(Observable.just("2").delay(1, TimeUnit.SECONDS));
                        }
                    }
                })).subscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, s);
                    }
                });

    }

    private void zip() {
        Observable<Integer> o1 = Observable.just(1, 2, 3)
                .delay(2, TimeUnit.SECONDS);
        Observable<Integer> o2 = Observable.just(3, 4, 5);

        /**
         * 忽略时间上的发射的绝对顺序，只是简单按照事件顺序组合
         */
        Observable.zip(o1, o2, new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) throws Exception {
                return integer + integer2;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, integer.toString());
            }
        });
    }

    private void combineLastest() {
        Observable<Integer> o1 = Observable.just(1, 2, 3);
        Observable<Integer> o2 = Observable.just(4, 5, 6)
                .delay(2, TimeUnit.SECONDS);
        /**
         * 考虑每个事件数据发送的绝对时间，组合最近的数据再发送
         */
        Observable.combineLatest(o1, o2, new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) throws Exception {
                Log.d(TAG, "inter1:" + integer + ",inter2:" + integer2);
                return integer + integer2;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, integer.toString());
            }
        });
    }

    private void merge() {
        Observable<Integer> o1 = Observable.just(1, 2, 3);
        Observable<Integer> o2 = Observable.just(4, 5, 6);

        /**
         * 先发送完o1，再发送o2
         */
        Observable.merge(o1, o2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer o) throws Exception {
                        Log.d(TAG, o.toString());
                    }
                });
    }

    private void startWith() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                /**
                 * 在发射数据前，插入指定的数据
                 */
                .startWith(100L)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, aLong.toString());
                    }
                });

    }

    private void initListener() {
        ObservableOnSubscribe observableOnSubscribe = new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                mTextView01.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        e.onNext("");
                    }
                });
            }
        };
        Observable.create(observableOnSubscribe)
                .debounce(5, TimeUnit.SECONDS)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String o) throws Exception {
                        Log.d(TAG, "click");
                    }
                });
    }

    private void takeLast() {
        Observable.just(1, 2, 3, 4)
                .delay(3, TimeUnit.SECONDS)
//                .takeLast(3)
//                .take(2)
                /**
                 * 取该时间内的数据
                 */
                .take(4, TimeUnit.SECONDS)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, integer.toString());
                    }
                });
    }

    private void take() {
        Observable.just(1, 2, 3, 4)
                /**
                 * 取前面3个数据
                 */
                .take(3)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, integer.toString());
                    }
                });
    }

    private void filter() {
//        Observable.interval(0, 1, TimeUnit.SECONDS)
//                .filter(new Predicate<Long>() {
//                    @Override
//                    public boolean test(Long aLong) throws Exception {
//                        return aLong > 5;
//                    }
//                }).subscribe(new Consumer<Long>() {
//            @Override
//            public void accept(Long aLong) throws Exception {
//                Log.d(TAG, aLong.toString());
//            }
//        });

        Observable.just("a1", "ab", "sdf", "a2", "dd")
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return s.startsWith("a");
                    }
                }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
            }
        });
    }

    private void debounce2() {
//        Observable.interval(0, 4, TimeUnit.SECONDS)
//                .debounce(2, TimeUnit.SECONDS)
//                .debounce(5, TimeUnit.SECONDS)
//                .subscribe(new Consumer<Long>() {
//                    @Override
//                    public void accept(Long aLong) throws Exception {
//                        Log.d(TAG, aLong.toString());
//                    }
//                });

        Observable.interval(0, 1, TimeUnit.SECONDS)
                .debounce(new Function<Long, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Long aLong) throws Exception {
                        return Observable.just("1");
                    }
                }).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.d(TAG, aLong.toString());
            }
        });
    }

    private void cast() {
//        Observable.interval(0, 1, TimeUnit.SECONDS)
//                .cast(String.class)
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//                        Log.d(TAG, s);
//                    }
//                });
        Observable.just(1, 2, "string")
                .cast(Integer.class)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, integer.toString());
                    }
                });
    }

    private void window() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .window(2)
                .delay(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Observable<Long>>() {
                    @Override
                    public void accept(Observable<Long> longObservable) throws Exception {
                        longObservable.subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                Log.d(TAG, aLong.toString());
                            }
                        });
                    }
                });
    }

    private void groupBy() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .groupBy(new Function<Long, Boolean>() {
                    @Override
                    public Boolean apply(Long aLong) throws Exception {
                        return aLong % 2 == 0;
                    }
                }).subscribe(new Consumer<GroupedObservable<Boolean, Long>>() {
            @Override
            public void accept(GroupedObservable<Boolean, Long> booleanLongGroupedObservable)
                    throws Exception {
                if (booleanLongGroupedObservable.getKey()) {
                    booleanLongGroupedObservable.subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            Log.d(TAG, aLong.toString());
                        }
                    });
                }
            }
        });
    }

    private void scan() {
        Observable.just("1", "2", "3", "4")
                .scan(new BiFunction<String, String, String>() {
                    @Override
                    public String apply(String s, String s2) throws Exception {
                        Log.d(TAG, "s:" + s + ",s2:" + s2);
                        return s + s2;
                    }
                }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
            }
        });
    }

    private void buffer() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                for (int i = 0; i < 10; i++) {
                    e.onNext(String.valueOf(i));
                }
                e.onComplete();
            }
        }).buffer(4)
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> strings) throws Exception {
                        Log.d(TAG, strings.toString());
                    }
                });
    }

    private void map() {
        Observable.just("user")
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        return "result";
                    }
                }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });
    }

    private void takeWhile() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .takeWhile(new Predicate<Long>() {
                    @Override
                    public boolean test(Long aLong) throws Exception {
                        return aLong < 4;
                    }
                }).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.d(TAG, aLong.toString());
            }
        });
    }

    private void takeUntil() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                /**
                 * 一直订阅知道takeUntil中事件产生
                 */
                .takeUntil(Observable.just("condition").delay(3, TimeUnit.SECONDS))
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, aLong.toString());
                    }
                });
    }

    private void skipWhile() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .skipWhile(new Predicate<Long>() {
                    @Override
                    public boolean test(Long aLong) throws Exception {
                        return aLong < 10;
                    }
                })
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, aLong.toString());
                    }
                });
    }

    private void skipUntil() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .skipUntil(Observable.just("1").delay(2000, TimeUnit.MILLISECONDS))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, aLong.toString());
                        if (Looper.myLooper() == Looper.getMainLooper()) {
                            Log.d(TAG, "stays on main thread");
                        }
                    }
                });
    }

    private void switchEmpty() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("onNext");
                e.onComplete();
            }
        })
                .switchIfEmpty(Observable.just("empty"))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, s);
                    }
                });
    }

    private void testDefaultEmpty() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("on next");
                e.onComplete();
            }
        }).defaultIfEmpty("default")
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, s);
                    }
                });
    }

    private void testAmb() {
        Observable o1 = Observable.just("a", "b", "c");
        Observable o2 = Observable.just("b", "d", "z")
                .delay(1000, TimeUnit.MILLISECONDS);
        Observable.ambArray(o1, o2)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String o) throws Exception {
                        Log.d(TAG, o);
                    }
                });
    }

    private void flatMap4() {
        Observable.fromIterable(words)
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) throws Exception {
                        return Observable.fromArray(s.split(""));
                    }
                })
                .distinct()
                .sorted()
                .zipWith(Observable.range(1, Integer.MAX_VALUE),
                        new BiFunction<String, Integer, Object>() {
                            @Override
                            public Object apply(String s, Integer integer) throws Exception {
                                return String.format(Locale.CHINA, "%2d.%s", integer, s);
                            }
                        })
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Log.d(TAG, o.toString());
                    }
                });
    }

    private void flatMap3() {
        Observable.fromIterable(words)
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) throws Exception {
                        return Observable.fromArray(s.split(""));
                    }
                })
                .distinct()
                .zipWith(Observable.range(1, Integer.MAX_VALUE),
                        new BiFunction<String, Integer, String>() {
                            @Override
                            public String apply(String s, Integer integer) throws Exception {
                                return String.format(Locale.CHINA, "%2d,%s", integer, s);
                            }
                        })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, s);
                    }
                });
    }

    private void flatMap2() {
        Observable.fromIterable(words)
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) throws Exception {
                        return Observable.fromArray(s.split(""));
                    }
                })
                .zipWith(Observable.range(1, Integer.MAX_VALUE),
                        new BiFunction<String, Integer, String>() {
                            @Override
                            public String apply(String s, Integer integer) throws Exception {
                                return String.format(Locale.CHINA, "%2d.%s", integer, s);
                            }
                        })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, s);
                    }
                });
    }

    private void flatMap() {
        Observable.fromIterable(words)
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) throws Exception {
                        return Observable.fromArray(s.split(""));
                    }
                })
                .zipWith(Observable.range(1, Integer.MAX_VALUE),
                        new BiFunction<String, Integer, String>() {
                            @Override
                            public String apply(String o, Integer integer) throws Exception {
                                return String.format(Locale.CHINA, "%2d.%s", integer, o);
                            }
                        })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, s);
                    }
                });

    }

    private void zipWith() {
        Observable.fromIterable(words)
                .zipWith(Observable.range(1, Integer.MAX_VALUE),
                        new BiFunction<String, Integer, String>() {
                            @Override
                            public String apply(String s, Integer integer) throws Exception {
                                return String.format(Locale.CHINA, "%2d.%s", integer, s);
                            }
                        })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, s);
                    }
                });
    }

    private void zipWith2() {
        Observable.fromIterable(words)
                .zipWith(Observable.range(1, Integer.MAX_VALUE),
                        new BiFunction<String, Integer, String>() {
                            @Override
                            public String apply(String s, Integer integer) throws Exception {
                                return integer.toString();
                            }
                        })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, s);
                    }
                });
    }

    private void range() {
        Observable.range(1, 5)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, integer.toString());
                    }
                });
    }

    private void just() {
//        Observable.just(words)
//                .subscribe(new Consumer<List<String>>() {
//                    @Override
//                    public void accept(List<String> strings) throws Exception {
//                        Log.d(TAG, strings.toString());
//                    }
//                });
        Observable.fromIterable(words)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, s);
                    }
                });
    }

    private void debounce() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onComplete();
            }
        });

        RxTextView.textChanges(mTextView01)
                .debounce(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<CharSequence, ObservableSource<CharSequence>>() {
                    @Override
                    public ObservableSource<CharSequence> apply(CharSequence charSequence)
                            throws Exception {
                        return null;
                    }
                })
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        Log.d("fall", charSequence.toString());
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
