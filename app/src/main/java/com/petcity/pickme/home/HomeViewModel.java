package com.petcity.pickme.home;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

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
import com.petcity.pickme.data.request.SigninRequest;
import com.petcity.pickme.data.response.AdvertiseResponse;
import com.petcity.pickme.data.response.SigninReponse;
import com.petcity.pickme.data.response.User;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;

/**
 * @ClassName HomeViewModel
 * @Description TODO
 * @Author sherily
 * @Date 11/01/21 4:13 PM
 * @Version 1.0
 */
public class HomeViewModel extends BaseViewModel {

    @Inject
    ApiService apiService;
    @Inject
    PreferenceManager preferenceManager;


    @Inject
    public HomeViewModel(@NonNull PickMeApp application) {
        super(application);
        adapter.setmEventObject(this);
    }

    public MediatorLiveData<AdvertiseResponse> show = new MediatorLiveData<>();
    public AdvertiseAdapter adapter = new AdvertiseAdapter();
    public MediatorLiveData<LiveDataWrapper<AdvertiseResponse>> data = new MediatorLiveData<>();
    private int totalPage;
    private int currentPage = 1;
    private int pageSize = 10;
    private String breed;
    private String district;
    private String region;


    @Override
    public void onCreateViewModel() {

    }

    @Override
    public void loadData(boolean isRefresh) {
        if (isRefresh) {
            refresh();
        } else {
            loadMore();
        }

    }

    private void refresh() {
        enableRefresh.set(true);
        enableLoadMore.set(false);
        onComplete.set(false);
        currentPage = 1;
        data = new MediatorLiveData<>();
        adapter.refresh(getAds(currentPage, pageSize, breed, district, region));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onComplete.set(true);
            }
        },2000);


    }
    private void loadMore() {
        currentPage = currentPage + 1;
        onComplete.set(false);
        enableRefresh.set(false);
        enableLoadMore.set(true);
        if (currentPage <= totalPage) {
            adapter.loadMore(getAds(currentPage, pageSize, breed, district, region));


        } else {
            // toast no more
            onComplete.set(true);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                onComplete.set(true);
            }
        },2000);

    }

    public LiveData<LiveDataWrapper<User>> getCurrentUser(String uid) {
        final MediatorLiveData<LiveDataWrapper<User>> currentUser = new MediatorLiveData<>();
        currentUser.setValue(LiveDataWrapper.<User>loading(null));
        Disposable disposable = apiService.getUser(uid)
                .flatMap(new ResultDataParse<User>())
                .compose(new RxSchedulerTransformer<User>())
                .subscribe(
                        new Consumer<User>() {
                            @Override
                            public void accept(User user) throws Throwable {
                                currentUser.setValue(LiveDataWrapper.success(user));
                                preferenceManager.setCurrentUserInfo(user);

                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                currentUser.setValue(LiveDataWrapper.<User>error(throwable));
                                preferenceManager.setCurrentUserInfo(null);
                            }
                        });
        addDisposable(disposable);
        return currentUser;
    }


    public  List<AdvertiseResponse> getAds(int index, int pageSize, String breed, String district, String Region) {

        totalPage = 4;
        List<AdvertiseResponse> list = new ArrayList();
        for (int i = index; i < index * 10 + 1; i++) {
            User user = new User(i, i+"@", "S" + i, "S" + i, "ss"+i+"@gmail.com", "", "Wellinton",  "Tawa", "18 Bell Street","Female", "https://picjumbo.com/wp-content/themes/picjumbofree/run.php?download&d=iconic-painted-ladies-in-san-francisco-california.jpg&n=iconic-painted-ladies-in-san-francisco-california");
            AdvertiseResponse response =  new AdvertiseResponse(i,user.getUserId(), "Dog", "Other", "60","02/03/2021", "11:00", "2", user.getRegion(),user.getDistrict(), "", "", "This is test content",user);
            list.add(response);
        }
        if(currentPage == 1){//如果首页数据为空或者小于每页展现的条数，则禁用上拉加载功能
            if(list.size() < pageSize){
                enableLoadMore.set(false);//禁用上拉加载功能
            }else{
                enableLoadMore.set(true);//启用上拉加载功能
            }
        }
        return list;


    }


    public void showDialog(AdvertiseResponse response) {
        // show copy email
        Log.d("home", "showDialog");
        show.setValue(response);


    }
}
