package com.example.lital.autotext.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.*;
import com.example.lital.autotext.R;
import com.example.lital.autotext.alarm.AlarmSetter;
import com.example.lital.autotext.db.DbMethods;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //Date Picker
        final EditText eDatePicker = findViewById(R.id.eDate);
        eDatePicker.setInputType(InputType.TYPE_NULL);
        eDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
        eDatePicker.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickerDialog(v);
                }
            }
        });

        //Time Picker
        final EditText eTimePicker = findViewById(R.id.eTime);
        eTimePicker.setInputType(InputType.TYPE_NULL);
        eTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });
        eTimePicker.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showTimePickerDialog(v);
                }
            }
        });

        //Save Button
        final Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int res = saveMessage();
                String status = res == -1 ? "Error" : "Success";
                int duration = Toast.LENGTH_SHORT;
                //show toast with saving status
                Toast toast = Toast.makeText(getApplicationContext(), status, duration);
                toast.show();
                //go the previous screen
                onBackPressed();
            }
        });
    }

    /**
     * Presents a time picker dialog
     * @param v an EditText view to store the selected time
     */
    private void showTimePickerDialog(final View v) {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(AddMessageActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                ((EditText) (v)).setText(String.format(Locale.US, "%02d:%02d", selectedHour, selectedMinute));
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    /**
     * Presents a date picker dialog
     * @param v an EditText view to store the selected date
     */
    private void showDatePickerDialog(final View v) {
        Calendar currentDate = Calendar.getInstance();
        int mYear = currentDate.get(Calendar.YEAR);
        int mMonth = currentDate.get(Calendar.MONTH);
        int mDay = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker;
        mDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                selectedMonth = selectedMonth + 1;
                ((EditText) v).setText(String.format(Locale.US, "%d-%02d-%02d", selectedYear, selectedMonth, selectedDay));
            }
        }, mYear, mMonth, mDay);
        mDatePicker.setTitle("Select Date");
        mDatePicker.show();
    }

    /**
     * Return the text the EditText with the specified id is displaying
     * @param id the id of the EditText view
     * @return the text of the corresponding EditText
     */
    private String getEditTextVal(int id) {
        EditText eField = findViewById(id);
        return eField.getText().toString();
    }

    /**
     * Saves the message to DB and sets an alarm for sending it
     * @return -1 on error, 0 on success
     */
    private int saveMessage() {
        String phoneNum = getEditTextVal(R.id.ePhone);
        String message = getEditTextVal(R.id.eMessage);
        String date = getEditTextVal(R.id.eDate);
        String time = getEditTextVal(R.id.eTime);

        //parse date and time
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(date + " " + time));

        } catch (ParseException e) { //parsing error
            e.printStackTrace();
            return -1;
        }

        //insert to db
        long id = DbMethods.insertMessage(message, phoneNum, cal);
        if (id == -1) { //error
            return -1;
        }

        //set alarm for sending the message
        AlarmSetter.setAlarm(AddMessageActivity.this, id, cal);

        return 0;
    }

}
