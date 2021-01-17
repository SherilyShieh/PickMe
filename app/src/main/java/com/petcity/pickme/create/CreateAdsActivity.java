package com.petcity.pickme.create;

import androidx.annotation.AttrRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
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
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.petcity.pickme.R;
import com.petcity.pickme.base.BaseActivity;
import com.petcity.pickme.common.utils.DogBreedUtil;
import com.petcity.pickme.databinding.ActivityCreateAdsBinding;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CreateAdsActivity extends BaseActivity<ActivityCreateAdsBinding, CreateAdsViewModel> {



    private long today;
    private long nextMonth;
    private long janThisYear;
    private long decThisYear;
    private long oneYearForward;
    private Pair<Long, Long> todayPair;
    private Pair<Long, Long> nextMonthPair;
    private MaterialDatePicker<?> picker;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1001;


    private final SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    @TimeFormat
    private int clockFormat;
    private int hour;
    private int minute;


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
        // todo
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
            imm.hideSoftInputFromWindow(mBinding.date.getWindowToken(),0);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("TAG", "Place: " + place.getAddress() + ", " + place.getId());
                mBinding.location.setText(place.getAddress());
            } else if (resultCode == 2) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("TAG", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            mBinding.location.clearFocus();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}