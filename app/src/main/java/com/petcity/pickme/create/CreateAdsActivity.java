package com.petcity.pickme.create;

import androidx.annotation.AttrRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.lifecycle.Observer;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.TimeFormat;
import com.google.gson.Gson;
import com.petcity.pickme.R;
import com.petcity.pickme.base.BaseActivity;
import com.petcity.pickme.base.LiveDataWrapper;
import com.petcity.pickme.common.utils.DogBreedUtil;
import com.petcity.pickme.common.utils.PreferenceManager;
import com.petcity.pickme.common.utils.TimeUtils;
import com.petcity.pickme.common.widget.CommonDialog;
import com.petcity.pickme.common.widget.LoadingDialog;
import com.petcity.pickme.data.request.CreateAdRequest;
import com.petcity.pickme.data.response.AdvertiseResponse;
import com.petcity.pickme.data.response.CommonResponse;
import com.petcity.pickme.data.response.User;
import com.petcity.pickme.databinding.ActivityCreateAdsBinding;
import com.petcity.pickme.setting.SettingActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CreateAdsActivity extends BaseActivity<ActivityCreateAdsBinding, CreateAdsViewModel> implements View.OnClickListener {


    private long today;
    private long nextMonth;
    private long janThisYear;
    private long decThisYear;
    private long oneYearForward;
    private Pair<Long, Long> todayPair;
    private Pair<Long, Long> nextMonthPair;
    private MaterialDatePicker<?> picker;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1001;
    public static int CREATE_OR_UPDATE_AD = 1002;

    private Long selectDate;
    private List<EditText> errorlist;
    private boolean isUpdate;
    private AdvertiseResponse ad;
    private LoadingDialog loadingDialog;
    private CommonDialog addressDialog;


    private final SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    @TimeFormat
    private int clockFormat;
    private int hour;
    private int minute;

    public static void satrtCreateForResult(Activity activity, boolean isUpdate, AdvertiseResponse adsResponse) {
        Intent intent = new Intent(activity, CreateAdsActivity.class);
        intent.putExtra("isUpdate", isUpdate);
        if (isUpdate)
            intent.putExtra("ad", new Gson().toJson(adsResponse));
        activity.startActivityForResult(intent, CREATE_OR_UPDATE_AD);
//        activity.overridePendingTransition(R.anim.bottom_popin_anim, R.anim.bottom_popout_anim);
    }

    // Date Picker
    private static Calendar getClearedUtc() {
        Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        utc.clear();
        return utc;
    }

    private static int resolveOrThrow(Context context, @AttrRes int attributeResId) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(attributeResId, typedValue, true)) {
            return typedValue.data;
        }
        throw new IllegalArgumentException(context.getResources().getResourceName(attributeResId));
    }

    private void initDatePicker() {

        today = MaterialDatePicker.todayInUtcMilliseconds();
        Calendar calendar = getClearedUtc();
        calendar.setTimeInMillis(today);
        calendar.roll(Calendar.MONTH, 1);
        nextMonth = calendar.getTimeInMillis();

        calendar.setTimeInMillis(today);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        janThisYear = calendar.getTimeInMillis();
        calendar.setTimeInMillis(today);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        decThisYear = calendar.getTimeInMillis();

        calendar.setTimeInMillis(today);
        calendar.roll(Calendar.YEAR, 1);
        oneYearForward = calendar.getTimeInMillis();

        todayPair = new Pair<>(today, today);
        nextMonthPair = new Pair<>(nextMonth, nextMonth);
    }

    private void createCalendar() {
        initDatePicker();
        int dialogTheme = resolveOrThrow(this, R.attr.materialCalendarTheme);
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setStart(janThisYear);
        constraintsBuilder.setEnd(decThisYear);
        constraintsBuilder.setOpenAt(today);
        constraintsBuilder.setValidator(DateValidatorPointForward.now());
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setSelection(today);
        builder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);
        builder.setTheme(dialogTheme);
        try {
            builder.setCalendarConstraints(constraintsBuilder.build());
            picker = builder.build();
            addCalendarListeners(picker);
            picker.show(getSupportFragmentManager(), picker.toString());
        } catch (IllegalArgumentException e) {
            Toast.makeText(CreateAdsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }

    private void addCalendarListeners(MaterialDatePicker<?> materialCalendarPicker) {
        materialCalendarPicker.addOnPositiveButtonClickListener(
                selection -> {
                    selectDate = (Long) selection;
                    mBinding.date.setText(materialCalendarPicker.getHeaderText());
                    mBinding.date.clearFocus();
                });
        materialCalendarPicker.addOnDismissListener(dialog -> {
            mBinding.date.clearFocus();
        });
    }


    private void showFrameworkTimepicker() {
        TimePickerDialog timePickerDialog =
                new TimePickerDialog(
                        CreateAdsActivity.this,
                        (view, hourOfDay, minute) ->
                                onTimeSet(hourOfDay, minute),
                        hour,
                        minute,
                        clockFormat == TimeFormat.CLOCK_24H);
        timePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                mBinding.startTime.clearFocus();
            }
        });
        timePickerDialog.show();
    }

    private void onTimeSet(int newHour, int newMinute) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, newHour);
        cal.set(Calendar.MINUTE, newMinute);
        cal.setLenient(false);

        String format = formatter.format(cal.getTime());
        mBinding.startTime.setText(format);
        mBinding.startTime.clearFocus();
        hour = newHour;
        minute = newMinute;

    }

    @Override
    protected boolean isHide() {
        return true;
    }

    @Override
    protected int getSysNavigationBarColor() {
        return R.color.darkBlue;
    }

    @Override
    protected Class<CreateAdsViewModel> getViewModel() {
        return CreateAdsViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_create_ads;
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) mBinding.date.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(mBinding.date.getWindowToken(), 0);
        }
    }

    // Google Places
    private void showGooglePlaces() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .setCountry("NZ")
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @LayoutRes
    public int getAdapterItemLayout() {
        return R.layout.cat_exposed_dropdown_popup_item;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        isUpdate = intent.getBooleanExtra("isUpdate", false);
        if (isUpdate) {
            ad = new Gson().fromJson(intent.getStringExtra("ad"), AdvertiseResponse.class);
        }
        mBinding.title.setText(isUpdate ? "Update Service" : "Create Service");
        mBinding.post.setText(isUpdate ? "UPDATE" : "POST");

        // Initialize the SDK
        Places.initialize(getApplicationContext(), "AIzaSyASJIP4JYSx0aYLKv3lw8Q-mllQE1JepA0");
        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);
        mBinding.breed.setDropDownBackgroundResource(R.drawable.white_bg);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        CreateAdsActivity.this,
                        getAdapterItemLayout(),
                        DogBreedUtil.CreateDogBreed());
        mBinding.breed.setAdapter(adapter);
        mBinding.date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    createCalendar();

                }
            }
        });

        mBinding.startTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showFrameworkTimepicker();
                }
            }
        });
        mBinding.location.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showGooglePlaces();
                }
            }
        });


        mBinding.post.setOnClickListener(this);
        mBinding.back.setOnClickListener(this);
        mBinding.breed.addTextChangedListener(txtWatcher(mBinding.breedTl));
        mBinding.date.addTextChangedListener(txtWatcher(mBinding.dateTl));
        mBinding.startTime.addTextChangedListener(txtWatcher(mBinding.startTimeTl));
        mBinding.durationTxt.addTextChangedListener(txtWatcher(mBinding.durationTl));
        mBinding.price.addTextChangedListener(txtWatcher(mBinding.priceTl));
        mBinding.location.addTextChangedListener(txtWatcher(mBinding.locationTl));
        mBinding.description.addTextChangedListener(txtWatcher(mBinding.descriptionTl));


        mViewModel.create.observe(CreateAdsActivity.this, new Observer<LiveDataWrapper<AdvertiseResponse>>() {
            @Override
            public void onChanged(LiveDataWrapper<AdvertiseResponse> advertiseResponseLiveDataWrapper) {
                switch (advertiseResponseLiveDataWrapper.status) {
                    case LOADING:
                        if (loadingDialog == null) {
                            loadingDialog = new LoadingDialog();
                        }
                        loadingDialog.show(getSupportFragmentManager(), null);
                        break;
                    case SUCCESS:
                        if (loadingDialog != null)
                            loadingDialog.dismiss();
                        setResult(RESULT_OK);
                        finish();
                        break;
                    case ERROR:
                        if (loadingDialog != null)
                            loadingDialog.dismiss();
                        finish();
                        break;
                }
            }
        });
        mViewModel.update.observe(CreateAdsActivity.this, new Observer<LiveDataWrapper<CommonResponse>>() {
            @Override
            public void onChanged(LiveDataWrapper<CommonResponse> commonResponseLiveDataWrapper) {
                switch (commonResponseLiveDataWrapper.status) {
                    case LOADING:
                        if (loadingDialog == null) {
                            loadingDialog = new LoadingDialog();
                        }
                        loadingDialog.show(getSupportFragmentManager(), null);
                        break;
                    case SUCCESS:
                        if (loadingDialog != null)
                            loadingDialog.dismiss();
                        setResult(RESULT_OK);
                        finish();
                        break;
                    case ERROR:
                        if (loadingDialog != null)
                            loadingDialog.dismiss();
                        finish();
                        break;
                }
            }
        });

    }

    private void showAddressDialog() {
        if (addressDialog == null) {
            addressDialog = new CommonDialog.Builder()
                    .setTitle("Enter Location")
                    .setContent("The beta version only allows beta testers to access the Google Places API, you are not a beta tester, " +
                            "please enter your address in the following format: Street, District, Region, Country.")
                    .setPlaceHolder("Street, District, Region, Country")
                    .setConfirmBtn("OK", new CommonDialog.OnClickListener() {
                        @Override
                        public void onClick(View v, String str) {
                            if (!TextUtils.isEmpty(str)) {
                                mBinding.location.setText(str);
                                addressDialog.dismiss();
                            } else {
                                Toast.makeText(CreateAdsActivity.this, "Please input your address!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setCancelBtn("Cancel", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addressDialog.dismiss();
                        }
                    })
                    .create();
        }
        addressDialog.show(getSupportFragmentManager(), "verify");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("TAG", "Place: " + place.getAddress() + ", " + place.getId());
                mBinding.location.setText(place.getAddress());
//                showAddressDialog();
            } else if (resultCode == 2) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("TAG", status.getStatusMessage());
                showAddressDialog();

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            mBinding.location.clearFocus();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private TextWatcher txtWatcher(TextInputLayout textLayout) {
        return new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                textLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                textLayout.setError(null);

            }
        };
    }

    private boolean validate(EditText text, TextInputLayout tl, String error) {
        if (TextUtils.isEmpty(text.getText().toString())) {
            if (!isUpdate) {
                tl.setError(error);
                errorlist.add(text);
            }
            return false;
        }
        return true;
    }

    private void process() {
        errorlist = new ArrayList();
        CreateAdRequest createRequest = new CreateAdRequest();
        User user = PreferenceManager.getInstance().getCurrentUserInfo();
        createRequest.setUserId(user.getUserId());
        createRequest.setType("dog_walking");
        if (isUpdate) {
            createRequest.setAdId(ad.getAd_id());
        }
        boolean isValidBreed;
        boolean isValidDate;
        boolean isValidST;
        boolean isValidDuration;
        boolean isValidPrice;
        boolean isValidLocation;
        boolean isValidDescription;

        if (isValidBreed = validate(mBinding.breed, mBinding.breedTl, "Dog breed can not be empty!")) {
            createRequest.setDogBreed(mBinding.breed.getText().toString().trim());
        }
        if (isValidDate = validate(mBinding.date, mBinding.dateTl, "Date can not be empty!")) {
            createRequest.setDate(TimeUtils.stampToDate(selectDate + ""));
        }
        if (isValidST = validate(mBinding.startTime, mBinding.startTimeTl, "Start time can not be empty!")) {
            createRequest.setStartTime(mBinding.startTime.getText().toString().trim());
        }
        if (isValidDuration = validate(mBinding.durationTxt, mBinding.durationTl, "Duration can not be empty!")) {
            createRequest.setDuration(mBinding.durationTxt.getText().toString().trim());
        }
        if (isValidPrice = validate(mBinding.price, mBinding.priceTl, "Price can not be empty!")) {
            createRequest.setPrice(mBinding.price.getText().toString().trim());
        }
        if (isValidLocation = validate(mBinding.location, mBinding.locationTl, "Location can not be empty!")) {
            String[] places = mBinding.location.getText().toString().trim().split(", ");
            if (places.length >= 4) {
                createRequest.setRegion(places[places.length - 2].trim());
                createRequest.setDistrict(places[places.length - 3].trim());
            } else if (places.length >= 3) {
                createRequest.setRegion(places[places.length - 2].trim());
                createRequest.setDistrict("");
            } else if (places.length >= 2) {
                createRequest.setRegion(places[places.length - 2].trim());
                createRequest.setDistrict("");
            } else {
                createRequest.setRegion("");
                createRequest.setDistrict("");
            }
        }
        if (isValidDescription = validate(mBinding.description, mBinding.descriptionTl, "Description can not be empty!")) {
            createRequest.setDescription(mBinding.description.getText().toString().trim());
        }
        if (isUpdate) {
            if (!isValidBreed && !isValidDate && !isValidST && !isValidDuration && !isValidPrice && !isValidDescription && !isValidLocation) {
                Toast.makeText(CreateAdsActivity.this, "Please enter at least one content with update！", Toast.LENGTH_SHORT).show();
                return;
            }
            mViewModel.updateAd(createRequest);

        } else if (isValidBreed && isValidDate && isValidST && isValidDuration && isValidPrice && isValidDescription && isValidLocation) {
            mViewModel.postAd(createRequest);
        } else if (!errorlist.isEmpty() && !errorlist.get(0).hasFocus()) {
            errorlist.get(0).requestFocus();
        }


    }

    @Override
    protected void onLogoutSuccess() {

    }

    @Override
    protected void onSendEmailSuccess() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.post:
                process();
                break;
        }
    }
}