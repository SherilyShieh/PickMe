package com.petcity.pickme.signin;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.petcity.pickme.base.BaseViewModel;
import com.petcity.pickme.base.LiveDataWrapper;
import com.petcity.pickme.base.PickMeApp;
import com.petcity.pickme.data.remote.ApiService;
import com.petcity.pickme.data.remote.ResultDataParse;
import com.petcity.pickme.data.remote.RxSchedulerTransformer;
import com.petcity.pickme.data.request.UpdateProfileRequest;
import com.petcity.pickme.data.response.User;


import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;

/**
 * @ClassName SigninWithAccountViewModel
 * @Description TODO
 * @Author sherily
 * @Date 12/01/21 8:37 PM
 * @Version 1.0
 */
public class SigninWithAccountViewModel extends BaseViewModel {

    @Inject
    ApiService apiService;

    @Inject
    public SigninWithAccountViewModel(@NonNull PickMeApp application) {
        super(application);
    }

    @Override
    public void onCreateViewModel() {

    }

    @Override
    public void loadData(boolean isRefresh) {

    }

    public LiveData<LiveDataWrapper<User>> updatePassword(String uid, String password) {
        final MediatorLiveData<LiveDataWrapper<User>> updates = new MediatorLiveData<>();
        UpdateProfileRequest request = new UpdateProfileRequest(uid);
        request.setPassword(password);
        updates.setValue(LiveDataWrapper.<User>loading(null));
        Disposable disposable = apiService.updateProfile(request)
                .flatMap(new ResultDataParse<User>())
                .compose(new RxSchedulerTransformer<User>())
                .subscribe(
                        new Consumer<User>() {
                            @Override
                            public void accept(User user) throws Throwable {
                                updates.setValue(LiveDataWrapper.success(user));

                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                updates.setValue(LiveDataWrapper.<User>error(throwable));
                            }
                        });
        addDisposable(disposable);
        return updates;
    }

    public LiveData<LiveDataWrapper<User>> verifyEmial(String email) {
        final MediatorLiveData<LiveDataWrapper<User>> verifyEmial = new MediatorLiveData<>();
        verifyEmial.setValue(LiveDataWrapper.<User>loading(null));
        Disposable disposable = apiService.verifyEmail(email)
                .flatMap(new ResultDataParse<User>())
                .compose(new RxSchedulerTransformer<User>())
                .subscribe(
                        new Consumer<User>() {
                            @Override
                            public void accept(User user) throws Throwable {
                                verifyEmial.setValue(LiveDataWrapper.success(user));

                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                verifyEmial.setValue(LiveDataWrapper.<User>error(throwable));
                            }
                        });
        addDisposable(disposable);
        return verifyEmial;
    }
}
