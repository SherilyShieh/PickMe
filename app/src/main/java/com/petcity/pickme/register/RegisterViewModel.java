package com.petcity.pickme.register;

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
 * @ClassName RegisterViewModel
 * @Description RegisterViewModel
 * @Author sherily
 * @Date 10/01/21 1:15 AM
 * @Version 1.0
 */
public class RegisterViewModel extends BaseViewModel {

    @Inject
    ApiService apiService;

    @Inject
    public RegisterViewModel(@NonNull PickMeApp application) {
        super(application);
    }

    @Override
    public void onCreateViewModel() {

    }

    @Override
    public void loadData(boolean isRefresh) {

    }

    public LiveData<LiveDataWrapper<SigninReponse>> register(String uid, String channel, String firstName, String lastName, String email, String password) {
        final MediatorLiveData<LiveDataWrapper<SigninReponse>> registerLiveData = new MediatorLiveData<>();
        SigninRequest request = new SigninRequest(uid, channel, firstName, lastName, email, password);
        registerLiveData.setValue(LiveDataWrapper.<SigninReponse>loading(null));
        Disposable disposable = apiService.register(request)
                .flatMap(new ResultDataParse<SigninReponse>())
                .compose(new RxSchedulerTransformer<SigninReponse>())
                .subscribe(
                        new Consumer<SigninReponse>() {
                            @Override
                            public void accept(SigninReponse signinReponse) throws Throwable {
                                registerLiveData.setValue(LiveDataWrapper.success(signinReponse));

                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                registerLiveData.setValue(LiveDataWrapper.<SigninReponse>error(throwable));
                            }
                        });
        addDisposable(disposable);
        return registerLiveData;
    }


}
