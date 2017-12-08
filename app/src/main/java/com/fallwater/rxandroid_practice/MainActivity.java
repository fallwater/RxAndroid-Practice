package com.fallwater.rxandroid_practice;

import com.jakewharton.rxbinding2.widget.RxTextView;

import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observables.GroupedObservable;

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

//        debounce();
//        just();
//        range();
//        zipWith();
//        zipWith2();
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
        cast();


    }

    private void cast() {
//        Observable.interval(0,1)
    }

    private void window() {
        Observable.interval(0,1,TimeUnit.SECONDS)
                .window(2)
                .delay(2,TimeUnit.SECONDS)
                .subscribe(new Consumer<Observable<Long>>() {
                    @Override
                    public void accept(Observable<Long> longObservable) throws Exception {
                        longObservable.subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                Log.d(TAG,aLong.toString());
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
