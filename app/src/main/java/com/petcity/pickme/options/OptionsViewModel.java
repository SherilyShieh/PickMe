package com.petcity.pickme.options;

import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.petcity.pickme.base.BaseViewModel;
import com.petcity.pickme.base.PickMeApp;
import com.petcity.pickme.data.remote.ApiService;
import com.petcity.pickme.data.remote.ResultDataParse;
import com.petcity.pickme.data.remote.RxSchedulerTransformer;
import com.petcity.pickme.data.request.TestInfo;
import com.petcity.pickme.data.response.ListTestInfo;
import com.petcity.pickme.data.response.SigninReponse;

import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;

/**
 * @ClassName OptionsViewModel
 * @Description Test for framework
 * @Author sherily
 * @Date 22/12/20 1:09 PM
 * @Version 1.0
 */
public class OptionsViewModel extends BaseViewModel {

    @Inject
    ApiService apiService;

    public OptionsAdapter adapter= new OptionsAdapter();

    @Inject
    public OptionsViewModel(@NonNull PickMeApp application) {
        super(application);
        adapter.setmEventObject(this);
    }

    @Override
    public void onCreateViewModel() {

    }

    @Override
    public void loadData(boolean isRefresh) {

    }

    public void update(String info, TestInfo testInfo) {
        TestInfo test = new TestInfo();
        test.setInfo(info);
        test.setId(testInfo.getId());
        Disposable disposable = apiService.update(test)
                .flatMap(new ResultDataParse<SigninReponse>())
                .compose(new RxSchedulerTransformer<SigninReponse>())
                .subscribe(
                        new Consumer<SigninReponse>() {
                            @Override
                            public void accept(SigninReponse signinReponse) throws Throwable {
                                get();
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                toast(throwable.getMessage());
                            }
                        });
        addDisposable(disposable);


    }
    public void delete(TestInfo testInfo) {
        Disposable disposable = apiService.delete(testInfo)
                .flatMap(new ResultDataParse<SigninReponse>())
                .compose(new RxSchedulerTransformer<SigninReponse>())
                .subscribe(
                        new Consumer<SigninReponse>() {
                            @Override
                            public void accept(SigninReponse signinReponse) throws Throwable {
                                get();
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                toast(throwable.getMessage());
                            }
                        });
        addDisposable(disposable);
    }
    public void get() {
        Disposable disposable = apiService.getInfo()
                .flatMap(new ResultDataParse<ListTestInfo>())
                .compose(new RxSchedulerTransformer<ListTestInfo>())
                .subscribe(
                        new Consumer<ListTestInfo>() {
                            @Override
                            public void accept(ListTestInfo listTestInfo) throws Throwable {
                                adapter.setData(listTestInfo.getList());

                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                toast(throwable.getMessage());
                            }
                        });
        addDisposable(disposable);
    }
    public void create(String info) {
        if (TextUtils.isEmpty(info)) {
            toast("Please input info");
            return;
        }
        TestInfo testInfo =  new TestInfo();
        testInfo.setInfo(info);
        Disposable disposable = apiService.create(testInfo)
                .flatMap(new ResultDataParse<TestInfo>())
                .compose(new RxSchedulerTransformer<TestInfo>())
                .subscribe(
                        new Consumer<TestInfo>() {
                            @Override
                            public void accept(TestInfo testInfo) throws Throwable {
                                get();
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                toast(throwable.getMessage());
                            }
                        });
        addDisposable(disposable);
    }

    private void toast(String str) {
        Toast.makeText(getApplication().getApplicationContext(),str, Toast.LENGTH_SHORT).show();
    }
}
