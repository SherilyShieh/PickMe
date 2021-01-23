package com.petcity.pickme.login;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.petcity.pickme.base.BaseViewModel;
import com.petcity.pickme.base.LiveDataWrapper;
import com.petcity.pickme.base.PickMeApp;
import com.petcity.pickme.data.remote.ApiService;
import com.petcity.pickme.data.remote.ResultDataParse;
import com.petcity.pickme.data.remote.RxSchedulerTransformer;
import com.petcity.pickme.data.request.SigninRequest;
import com.petcity.pickme.data.response.SigninReponse;

import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;


/**
 * @ClassName LoginViewModel
 * @Description LoginViewModel
 * @Author sherily
 * @Date 6/01/21 5:53 PM
 * @Version 1.0
 */
public class LoginViewModel extends BaseViewModel {

    @Inject
    ApiService apiService;


    @Inject
    public LoginViewModel(@NonNull PickMeApp application) {
        super(application);
    }

    @Override
    public void onCreateViewModel() {
    }

    @Override
    public void loadData(boolean isRefresh) {

    }

    public LiveData<LiveDataWrapper<SigninReponse>> signinWithThird(String uid, String channel, String firstName, String lastName, String email) {
        final MediatorLiveData<LiveDataWrapper<SigninReponse>> signLiveData = new MediatorLiveData<>();
        SigninRequest request = new SigninRequest(uid, channel, firstName, lastName, email, null);
        signLiveData.setValue(LiveDataWrapper.<SigninReponse>loading(null));
        Disposable disposable = apiService.signinWithThird(request)
                .flatMap(new ResultDataParse<SigninReponse>())
                .compose(new RxSchedulerTransformer<SigninReponse>())
                .subscribe(
                        new Consumer<SigninReponse>() {
                            @Override
                            public void accept(SigninReponse signinReponse) throws Throwable {
                                signLiveData.setValue(LiveDataWrapper.success(signinReponse));

                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                signLiveData.setValue(LiveDataWrapper.<SigninReponse>error(throwable));
                            }
                        });
        addDisposable(disposable);
        return signLiveData;
    }
}
