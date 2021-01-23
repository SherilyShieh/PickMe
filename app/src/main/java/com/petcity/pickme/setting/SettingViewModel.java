package com.petcity.pickme.setting;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.petcity.pickme.base.BaseViewModel;
import com.petcity.pickme.base.LiveDataWrapper;
import com.petcity.pickme.base.PickMeApp;
import com.petcity.pickme.common.utils.PreferenceManager;
import com.petcity.pickme.data.remote.ApiService;
import com.petcity.pickme.data.remote.ResultDataParse;
import com.petcity.pickme.data.remote.RxSchedulerTransformer;
import com.petcity.pickme.data.request.UpdateProfileRequest;
import com.petcity.pickme.data.response.User;

import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;

/**
 * @ClassName SettingViewModel
 * @Description TODO
 * @Author sherily
 * @Date 13/01/21 3:58 PM
 * @Version 1.0
 */
public class SettingViewModel extends BaseViewModel {

    @Inject
    ApiService apiService;

    @Inject
    PreferenceManager preferences;

    @Inject
    public SettingViewModel(@NonNull PickMeApp application) {
        super(application);
    }

    @Override
    public void onCreateViewModel() {

    }

    public LiveData<LiveDataWrapper<User>> updateProfile(String uid, String avatar, String firstName,
        String lastName, String email, String gender, String location, String password) {
        final MediatorLiveData<LiveDataWrapper<User>> updates = new MediatorLiveData<>();
        UpdateProfileRequest request = new UpdateProfileRequest(uid);
        request.setAvatar(avatar);
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setPassword(password);
        request.setEmail(email);
        request.setGender(gender);
        if (!TextUtils.isEmpty(location)) {
            String[] places = location.split(", ");
            if (places.length >= 4 ) {
                request.setRegion(places[places.length - 2].trim());
                request.setDistrict(places[places.length - 3].trim());
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < places.length - 3; i++) {
                    sb.append(places[i].trim());
                    if (i < places.length - 4) {
                        sb.append(", ");
                    }
                }
                if (sb.toString().length() > 0) {
                    request.setDetail_address(sb.toString().trim());
                }
            } else if (places.length >= 3) {
                    request.setRegion(places[places.length - 2].trim());
                    request.setDistrict("");
                    request.setDetail_address(places[places.length - 3].trim());
            } else if (places.length >= 2){
                request.setRegion(places[places.length - 2].trim());
                request.setDistrict("");
                request.setDetail_address("");
            } else {
                request.setRegion("");
                request.setDistrict("");
                request.setDetail_address("");
            }
        }

        updates.setValue(LiveDataWrapper.<User>loading(null));
        Disposable disposable = apiService.updateProfile(request)
                .flatMap(new ResultDataParse<User>())
                .compose(new RxSchedulerTransformer<User>())
                .subscribe(
                        new Consumer<User>() {
                            @Override
                            public void accept(User user) throws Throwable {
                                preferences.setCurrentUserInfo(user);
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


    @Override
    public void loadData(boolean isRefresh) {

    }
}
