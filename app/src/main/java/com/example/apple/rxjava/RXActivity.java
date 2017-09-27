package com.example.apple.rxjava;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.apple.pullzoom.R;
import com.example.apple.rxjava.bean.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;

public class RXActivity extends AppCompatActivity {

    final String TAG = "RXActivity";


    CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx);



    }

    public void range(View v) {
        Observable.range(2,4)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "accept: " + integer );
                    }
                });
    }
    public void repeat(View v) {
        Observable.just(1,3).repeat(3)
                .subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.e(TAG, "accept: " + integer );
            }
        });

    }
    public void delay(View v) {

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1000);
            }
        }).delay(1000, TimeUnit.MILLISECONDS).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.e(TAG, integer + "  accept:  " + Thread.currentThread().getName());
            }
        });
    }

    public void window(View v) {

        Observable.just(1l, 6l, 8l).interval(2, TimeUnit.SECONDS).take(10)
                .window(2, TimeUnit.SECONDS).subscribe(new Consumer<Observable<Long>>() {
            @Override
            public void accept(Observable<Long> integerObservable) throws Exception {
                integerObservable.forEach(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.e(TAG, aLong + "  accept:  " + Thread.currentThread().getName());
                    }
                });
                Log.e(TAG, integerObservable + "  accept:  " + Thread.currentThread().getName());
            }
        });
    }

    public void debounce(View v) {

        //延时操作，
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("rxjava");
            }
        }).debounce(500, TimeUnit.MILLISECONDS).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.e(TAG, s + "  accept:  " + Thread.currentThread().getName());
            }
        });
    }

    public void throttleFirst(View v) {

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(22);
                Thread.sleep(502);
                e.onNext(22);
            }
        }).throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, integer + "  accept:  " + Thread.currentThread().getName());
                    }
                });

    }

    public void ReplaySubject(View v) {

//        ReplaySubject 使得无论订阅者在何时订阅，它们都能收到被订阅者发射序列中的所有数据。
        ReplaySubject<Integer> source = ReplaySubject.create();
        source.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.e(TAG, integer + "  accept:  " + Thread.currentThread().getName());
            }
        });

        source.onNext(1);
        source.onNext(2);
        source.onNext(3);
        source.onNext(4);
        source.onComplete();
    }


    public void last(View v) {

        Observable.just("a", "b", "c", "a")
                .last("e").subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.e(TAG, s + "  accept:  " + Thread.currentThread().getName());
            }
        });

    }

    public void distinct(View v) {
        //发被观察者列当中之前没有发射过的数据，也就是去除重复的数据
        Observable.just(1, 3, 4, 2, 1, 3)
                .distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, integer + "  accept:  " + Thread.currentThread().getName());
                    }
                });
    }

    String s = "abc";
    public void defer(View view) {
        //使用defer可以延迟被订阅者的生成，也就是被订阅者是在订阅发生时才生成
        Observable mObservable =

                Observable.defer(new Callable<ObservableSource<? extends String>>() {
                    @Override
                    public ObservableSource<? extends String> call() throws Exception {
                        return Observable.just(s);
                    }

                });
        s = "bbbb";
        mObservable.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.e(TAG, s + "  accept:  " + Thread.currentThread().getName());
            }
        });
    }

    public void merge(View view) {
        //merge和concat类似，也是用来连接两个被订阅者，但是它不保证两个被订阅发射数据的顺序。
            Observable.merge(Observable.just(11, 22, 44, 33), Observable.just(50, 60, 90))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {
                            Log.e(TAG, integer + "  onNext:  " + Thread.currentThread().getName());
                        }
                    });
    }

    public void concat(View view) {
        //连接两个被订阅者，订阅者将会按照a->b的顺序收到两个被订阅者所发射的消息。
        Observable.concat(Observable.just(11, 22, 44, 33), Observable.just(50, 60, 90))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, integer + "  onNext:  " + Thread.currentThread().getName());
                    }
                });


    }

    public void replay(View view) {

        /**
         * 使得即使在未订阅时，被订阅者已经发射了数据，订阅者也可以收到被订阅者在订阅之前最多n个数据。
         */
        PublishSubject<Integer> source = PublishSubject.create();
        ConnectableObservable<Integer> connectableObservable = source.replay(1);
        connectableObservable.connect();

        connectableObservable.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer value) {
                Log.e(TAG, value + "  onNext:  " + Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
        source.onNext(11);
        source.onNext(12);
        source.onNext(13);
        source.onNext(14);
        connectableObservable.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.e(TAG, integer + "  onNext:  two  " + Thread.currentThread().getName());
            }
        });

    }

    public void scan(View view) {
        /*
        * sacn操作符是遍历源Observable产生的结果，再按照自定义规则进行运算，依次输出每次计算后的结果给订阅者:
           call 回掉第一个参数是上次的结算结果，第二个参数是当此的源observable的输入值
        */
        Observable.just(1, 2, 3, 5).scan(new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) throws Exception {
                Log.e(TAG, integer + "  " + integer2 + "  apply:  " + Thread.currentThread().getName());
                return integer + integer2;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer value) {
                        Log.e(TAG, value + "  onNext:  " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                })
        ;

    }

    public void skip(View view) {
        // 剔除订阅的个数
        Observable.just(1, 2, 3, 5, 6, 11).skip(2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, integer + "  test:  " + Thread.currentThread().getName());
                    }
                });
    }

    public void filter(View view) {
        Observable.just(1, 2, 4, 5, 6, 11).filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                Log.e(TAG, integer + "  test:  " + Thread.currentThread().getName());

                return integer % 2 == 0;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, integer + "  accept:  " + Thread.currentThread().getName());
                    }
                });

    }

    public void buffer(View view) {
        // 能一次性集齐多个结果到列表中，订阅后自动清空相应结果,直到完全清除   uffer(3),每次订阅3个
        //也可以周期性的集齐多个结果到列表中，uffer 传的数据比订阅列表少
        // buffer(3,1) 每次订阅跳过一个
        Observable.just("one", "two", "three", "four", "five").buffer(3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> strings) throws Exception {
                        Log.e(TAG, strings + "  accept:  " + Thread.currentThread().getName());
                    }
                });
    }

    public void Flowable(View view) {
        Flowable.just(1, 2, 3, 4)// 所有数 之和
                .reduce(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        Log.e(TAG, integer + "  apply:  " + Thread.currentThread().getName());
                        return integer2 + integer;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {

                        Log.e(TAG, integer + "  subscribe:  " + Thread.currentThread().getName());
                    }
                });
    }

    public void Completable(View view) {

        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter e) throws Exception {
                Log.e(TAG, "  subscribe:  " + Thread.currentThread().getName());
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(TAG, "  onSubscribe:  " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "  accept:  " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });


    }


    /**
     * 观察者
     *
     * @param view
     */
    public void SingleObserver(View view) {
        Single.create(new SingleOnSubscribe<Integer>() {
            @Override
            public void subscribe(SingleEmitter<Integer> e) throws Exception {
                e.onSuccess(100);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, integer + "  accept:  " + Thread.currentThread().getName());
                    }
                });


    }

    public void timer(View view) {


        /**
         * timer操作符既可以延迟执行一段逻辑，也可以间隔执行一段逻辑，但是已经过时了，而是由interval操作符来间隔执行.
         * Observable.timer(5, TimeUnit.SECONDS). 延迟
         * Observable.interval(2,1,TimeUnit.SECONDS) 间隔
         *
         */

        Observable.interval(2, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.e(TAG, aLong + "  accept:  " + Thread.currentThread().getName());

                    }
                });

//        Observable.timer(2,TimeUnit.SECONDS)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Long>() {
//                    @Override
//                    public void accept(Long aLong) throws Exception {
//                        Log.e(TAG, aLong + "accept:  " + Thread.currentThread().getName());
//
//                    }
//                });
    }

    public void take(View view) {
        /**
         * take 用于指定订阅者最多收到多少数据。
         */
        Observable
                .just(2, 4, 6, 22, 1, 45)
                .take(2)
                .map(new Function<Integer, Integer>() {

                    @Override
                    public Integer apply(Integer integer) throws Exception {
                        Log.e(TAG, "accept:  " + Thread.currentThread().getName());

                        return integer + 100;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "accept: " + integer + "    " + Thread.currentThread().getName());
                    }
                });

    }


    /**
     * 调度控制后台任务队列,可以清空队列，也就没有观察者回调了,如离开页面，清空队列
     *
     * @param view
     */
    public void CompositeDisposable(View view) {
        mCompositeDisposable.add(Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        e.onNext(10);
                        Log.e(TAG, "subscribe: " + Thread.currentThread().getName());
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, integer + "  subscribe: " + Thread.currentThread().getName());
                    }
                })
        );

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

    private Observable<List<User>> one() {
        return Observable.create(new ObservableOnSubscribe<List<User>>() {
            @Override
            public void subscribe(ObservableEmitter<List<User>> e) throws Exception {
                if (!e.isDisposed()) {
                    e.onNext(Utils.getOne());
                    e.onComplete();
                }
            }
        });
    }

    private Observable<List<User>> two() {
        return Observable.create(new ObservableOnSubscribe<List<User>>() {
            @Override
            public void subscribe(ObservableEmitter<List<User>> e) throws Exception {
                if (!e.isDisposed()) {
                    e.onNext(Utils.getTwo());
                    e.onComplete();
                }
            }
        });
    }


    public void ZIP(View view) {
        //zip操作符其实就是通过Observable.zip()方法把多个Observable组合成新的Observable，
        // 这个新的Observable对应的数据流由call方法决定：
        Observable.zip(one(), two(), new BiFunction<List<User>, List<User>, List<User>>() {

            @Override
            public List<User> apply(List<User> o, List<User> o2) throws Exception {
                o.addAll(o2);
                return o ;
            }
        }).subscribeOn(Schedulers.io()).forEach(new Consumer<List<User>>() {
            @Override
            public void accept(List<User> users) throws Exception {
                Log.e(TAG, "onNext: " +users);
            }
        });
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List<User>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(List<User> value) {
//                        Log.e(TAG, "onNext: " +value);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });


    }

    /**
     * Map一般用于对原始的参数进行加工处理，返回值还是基本的类型，可以在subscribe中使用(适用)的类型。
     * flatMap一般用于输出一个Observable，而其随后的subscribe中的参数也跟Observable中的参数一样，注意不是Observable，一般用于对原始数据返回一个Observable,这个Observable中数据类型可以是原来的，也可以是其他的
     *
     * @param view
     */

    public void flatMap(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
            }//flatMap
        }).concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < 10; i++) {
                    list.add("我是变换过的" + integer);
                }
                return Observable.fromIterable(list);
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e(TAG, s);
                    }
                });


    }


    public void map(View view) {

        //变换 : 所谓变换，就是将事件序列中的对象或整个序列进行加工处理，转换成不同的事件或事件序列
        // 将id 转换成了  Drawable
        Observable.just(R.drawable.cast_ic_notification_1)//fromArray
                .map(new Function<Integer, Drawable>() {
                    @Override
                    public Drawable apply(Integer integer) throws Exception {
                        Drawable drawable = getResources().getDrawable(integer);
                        return drawable;
                    }
                })
                .subscribe(new Consumer<Drawable>() {
                    @Override
                    public void accept(Drawable bitmap) throws Exception {
                        findViewById(R.id.bt1).setBackground(bitmap);
                    }
                });


    }

    public void from(View view) {


        Observable.fromArray(3, 6, 7).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())


                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, integer + "   accept: " + Thread.currentThread().getName());
                    }
                });

    }

    public void just(View view) {

        Observable.just(3, 6, 7).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())


                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, integer + "   accept: " + Thread.currentThread().getName());
                    }
                });

    }

    public void create(View view) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                Log.e(TAG, "subscribe: " + Thread.currentThread().getName());

                e.onNext(100 + "");
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).


                subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(TAG, "onSubscribe: " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(String value) {
                        Log.e(TAG, value + " onNext: " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete: " + Thread.currentThread().getName());
                    }
                });
    }

}
