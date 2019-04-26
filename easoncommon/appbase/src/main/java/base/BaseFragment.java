package base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.reactivex.subjects.PublishSubject;

/**
 * baseFra
 *
 * @author cyj
 * @create 2018-11-29 4:57 PM
 **/
public abstract class BaseFragment extends Fragment {
    //管理生命周期的Subject,便于在某些生命周期及时取消订阅
    protected final PublishSubject<rxsomthing.bus.LifeCycleEvent> mLifeCycleEventPublishSubject = PublishSubject.create();
    protected BaseActivity mBaseActivity;

    @Override
    public void onAttach(Context context) {
        mBaseActivity = (BaseActivity) context;
        initData(getArguments());
        super.onAttach(context);
    }


    protected void initData(Bundle arguments) {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getResContent(), container, false);
        initView(view);
        return view;
    }


    protected abstract void initView(View view);

    protected abstract int getResContent();

    @Override
    public void onDestroy() {
        mLifeCycleEventPublishSubject.onNext(rxsomthing.bus.LifeCycleEvent.DESTROY);
        super.onDestroy();
    }

}