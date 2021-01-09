package com.petcity.pickme.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * @ClassName BaseViewModel
 * @Description TODO
 * @Author sherily
 * @Date 6/01/21 3:31 PM
 * @Version 1.0
 */
public abstract class BaseViewModel extends AndroidViewModel {

    private CompositeDisposable mCompositeDisposable;

    public BaseViewModel(@NonNull PickMeApp application) {
        super(application);
        onCreate();
    }

    private void onCreate() {
        this.mCompositeDisposable = new CompositeDisposable();
        onCreateViewModel();
    }

    public abstract void onCreateViewModel();

    public void onDestroy() {
        if (null != mCompositeDisposable) {
            this.mCompositeDisposable.clear();
            this.mCompositeDisposable = null;
        }
    }

    protected void addDisposable(Disposable disposable) {
        if (null != mCompositeDisposable) {
            mCompositeDisposable.add(disposable);
        }
    }
}
