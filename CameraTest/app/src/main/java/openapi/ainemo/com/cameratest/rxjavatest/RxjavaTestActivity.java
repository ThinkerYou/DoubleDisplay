//package openapi.ainemo.com.cameratest.rxjavatest;
//
//import android.app.Activity;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//
//import io.reactivex.functions.Consumer;
//import io.reactivex.subjects.AsyncSubject;
//import io.reactivex.subjects.BehaviorSubject;
//import io.reactivex.subjects.PublishSubject;
//import io.reactivex.subjects.ReplaySubject;
//import openapi.ainemo.com.cameratest.R;
//
//public class RxjavaTestActivity extends Activity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_rxjava_test);
//        AsyncSubject asyncSubject = AsyncSubject.create();
//        asyncSubject.onNext(1);
//        asyncSubject.onNext(2);
//        asyncSubject.onNext(3);
//        asyncSubject.onComplete();
//        asyncSubject.subscribe(new Consumer() {
//            @Override
//            public void accept(Object o) throws Exception {
//                Log.i("subscribe", "accept: " + (Integer) o);
//            }
//        });
//
//        BehaviorSubject<Integer> behaviorSubject = BehaviorSubject.create();
//        behaviorSubject.onNext(1);
//        behaviorSubject.onNext(2);
//        behaviorSubject.onNext(3);
//        behaviorSubject.onComplete();
//        behaviorSubject.subscribe(new Consumer() {
//            @Override
//            public void accept(Object o) throws Exception {
//                Log.i("behaviorSubject", "accept: "+o);
//            }
//        });
//        PublishSubject publishSubject = PublishSubject.create();
//        publishSubject.subscribe(new Consumer() {
//            @Override
//            public void accept(Object o) throws Exception {
//                Log.i("publishSubject", "accept: " + o);
//            }
//        });
//        publishSubject.onNext(1);
//        publishSubject.onNext(2);
//        publishSubject.onNext(3);
//        publishSubject.onComplete();
//
//
////        ReplaySubject<Integer> objectReplaySubject = ReplaySubject.create();
////        objectReplaySubject.onNext(1);
////        objectReplaySubject.onNext(2);
////        objectReplaySubject.onNext(3);
////        objectReplaySubject.onComplete();
////        objectReplaySubject.subscribe(new Consumer<Integer>() {
////            @Override
////            public void accept(Integer integer) throws Exception {
////                Log.i("ReplaySubject", "accept: " + integer);
////            }
////        });
//
//
//    }
//}
