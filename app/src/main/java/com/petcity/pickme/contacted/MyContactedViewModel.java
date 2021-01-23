package com.petcity.pickme.contacted;

import androidx.annotation.NonNull;
import androidx.lifecycle.MediatorLiveData;

import com.petcity.pickme.base.BaseViewModel;
import com.petcity.pickme.base.LiveDataWrapper;
import com.petcity.pickme.base.PickMeApp;
import com.petcity.pickme.common.utils.PreferenceManager;
import com.petcity.pickme.data.remote.ApiService;
import com.petcity.pickme.data.remote.ResultDataParse;
import com.petcity.pickme.data.remote.RxSchedulerTransformer;
import com.petcity.pickme.data.request.CommonRequest;
import com.petcity.pickme.data.response.AdvertiseResponse;
import com.petcity.pickme.data.response.CommonResponse;
import com.petcity.pickme.data.response.ContactedResponse;
import com.petcity.pickme.data.response.ListAdvertiseResponse;
import com.petcity.pickme.data.response.ListContactedResponse;
import com.petcity.pickme.data.response.User;

import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;

/**
 * @ClassName MyContactedViewModel
 * @Description TODO
 * @Author sherily
 * @Date 13/01/21 4:03 PM
 * @Version 1.0
 */
public class MyContactedViewModel extends BaseViewModel {

    public MyContactedAdapter adapter = new MyContactedAdapter();
    public MediatorLiveData<LiveDataWrapper<ListContactedResponse>> data = new MediatorLiveData<>();
    public MediatorLiveData<ContactedResponse> delete = new MediatorLiveData<>();
    public MediatorLiveData<LiveDataWrapper<CommonResponse>> deleteInDB = new MediatorLiveData<>();
    public MediatorLiveData<ContactedResponse> resend = new MediatorLiveData<>();

    @Inject
    ApiService apiService;
    @Inject
    PreferenceManager preferenceManager;

    private int totalPage;
    private int currentPage = 1;
    private int pageSize = 10;

    @Inject
    public MyContactedViewModel(@NonNull PickMeApp application) {
        super(application);
        adapter.setmEventObject(this);
    }

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
        getAds(currentPage, pageSize, true);


    }

    private void loadMore() {
        currentPage = currentPage + 1;
        onComplete.set(false);
        enableRefresh.set(false);
        enableLoadMore.set(true);
        if (currentPage <= totalPage) {
            getAds(currentPage, pageSize, false);

        } else {
            // toast no more
            onComplete.set(true);
        }

    }

    public void getAds(int index, int pageSize,  boolean isRefresh) {

        data.setValue(LiveDataWrapper.<ListContactedResponse>loading(null));
        User user = preferenceManager.getCurrentUserInfo();
        Disposable disposable = apiService.getAllContacted(index, pageSize, user.getUserId() )
                .flatMap(new ResultDataParse<ListContactedResponse>())
                .compose(new RxSchedulerTransformer<ListContactedResponse>())
                .subscribe(
                        new Consumer<ListContactedResponse>() {
                            @Override
                            public void accept(ListContactedResponse listContactedResponse) throws Throwable {
                                data.setValue(LiveDataWrapper.success(listContactedResponse));
                                totalPage = listContactedResponse.getTotalCount();
                                if (totalPage > 0) {
                                    isEmpty.set(false);
                                    if (currentPage == 1) {//如果首页数据为空或者小于每页展现的条数，则禁用上拉加载功能
                                        if (listContactedResponse.getAdvertiseResponseList().size() < pageSize) {
                                            enableLoadMore.set(false);//禁用上拉加载功能
                                        } else {
                                            enableLoadMore.set(true);//启用上拉加载功能
                                        }
                                    }
                                    if (isRefresh) {
                                        adapter.refresh(listContactedResponse.getAdvertiseResponseList());
                                    } else {
                                        adapter.loadMore(listContactedResponse.getAdvertiseResponseList());
                                    }
                                    onComplete.set(true);

                                } else {
                                    isEmpty.set(true);
                                }
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                data.setValue(LiveDataWrapper.<ListContactedResponse>error(throwable));
                                isEmpty.set(true);
                            }
                        });
        addDisposable(disposable);
    }

    public void resendFuc(ContactedResponse aResponse) {
        resend.setValue(aResponse);

    }

    public void deleteFuc(ContactedResponse aResponse) {
        delete.setValue(aResponse);

    }

    public void deleteInDB(ContactedResponse response) {
        deleteInDB.setValue(LiveDataWrapper.<CommonResponse>loading(null));
        CommonRequest commonRequest = new CommonRequest();
        User user = preferenceManager.getCurrentUserInfo();
        commonRequest.setContactedId(response.getContactedId());
        commonRequest.setUserId(user.getUser_id());
        Disposable disposable = apiService.deleteContacted(commonRequest)
                .flatMap(new ResultDataParse<CommonResponse>())
                .compose(new RxSchedulerTransformer<CommonResponse>())
                .subscribe(
                        new Consumer<CommonResponse>() {
                            @Override
                            public void accept(CommonResponse commonResponse) throws Throwable {
                                deleteInDB.setValue(LiveDataWrapper.success(commonResponse));
                                loadData(true);
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                deleteInDB.setValue(LiveDataWrapper.<CommonResponse>error(throwable));
                            }
                        });
        addDisposable(disposable);
    }
}
