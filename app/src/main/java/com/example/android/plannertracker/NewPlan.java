package com.example.android.plannertracker;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.example.android.plannertracker.BroadCastRecievers.AlarmReciever;
import com.example.android.plannertracker.BroadCastRecievers.NotificationReciever;
import com.example.android.plannertracker.SqltieDatabase.DbContract;
import com.example.android.plannertracker.SqltieDatabase.DbHelper;
import com.example.android.plannertracker.TripDetails.TrackerInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


public class NewPlan extends AppCompatActivity {
    private static final int AUTOCOMPLETE_REQUEST_CODE = 2;
    Calendar c;
    String TripType, TripName, start, destination, time, date;
    SQLiteDatabase sqLiteDatabase;
    TimePickerDialog mTimePicker;
    DatePickerDialog datePickerDialog;
    RadioGroup radioGroup;
    RadioButton radioButton;
    int hour, minute, mYear, mMonth, mDay;
    EditText startPosition, endPosition, tripName;
    DatabaseReference databaseReference;
    Button dateBtn, timeBtn, save;
    TextView dateText, timeText;
    FirebaseUser fu;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_plan);



        c = Calendar.getInstance();
      final String userId=initialize();
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseDate();
            }
        });
        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseTime();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToDatabase(userId);
                //showAlarmDialog();
                //  saveTointernal();
                setAlarm(false);
                finish();
            }
        });

    }

    private void saveTointernal() {
        DbHelper dbHelper = new DbHelper(getBaseContext());
        sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbContract.DbTripDetails.COLUMN_TRIP_NAME, TripName);
        contentValues.put(DbContract.DbTripDetails.COLUMN_START_POINT, start);
        contentValues.put(DbContract.DbTripDetails.COLUMN_END_POINT, destination);
        contentValues.put(DbContract.DbTripDetails.COLUMN_DATE_TRIP, date);
        contentValues.put(DbContract.DbTripDetails.COLUMN_TIME_TRIP, time);

        long newRowId = sqLiteDatabase.insert(DbContract.DbTripDetails.TABLE_NAME, null, contentValues);
        Toast.makeText(this, "Added internally succeffully", Toast.LENGTH_SHORT).show();
    }


    private void showAlarmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reminder To your Trip")
                .setMessage("Yout Trip is : ")
                .setPositiveButton("Ok Start", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(NewPlan.this, "Starting", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setNeutralButton("later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(NewPlan.this, "sending Notification", Toast.LENGTH_SHORT).show();
            }
        }).create().show();
    }

    private void saveToDatabase(String id) {

        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        Log.i("trace", radioButton.getText().toString());
        String Note = " ";
        TripType = radioButton.getText().toString();
        TripName = tripName.getText().toString();
        start = startPosition.getText().toString();
        destination = endPosition.getText().toString();
        date = dateText.getText().toString();
        time = timeText.getText().toString();

        if (!TextUtils.isEmpty(start) && !TextUtils.isEmpty(date) &&
                !TextUtils.isEmpty(time) && !TextUtils.isEmpty(TripName)
                && !TextUtils.isEmpty(TripType)) {
          //  String id = databaseReference.push().getKey();
            TrackerInformation trackerInformation = new TrackerInformation(id, start,
                    destination, TripName, time, date, TripType);
           databaseReference.child(id).child("trip data").setValue(trackerInformation);
            Toast.makeText(this, "Done Added", Toast.LENGTH_SHORT).show();
            tripName.setText("");
            startPosition.setText("");
            endPosition.setText("");
            dateText.setText("");
            timeText.setText("");
        } else {
            Toast.makeText(this, "Enter Valid bodies", Toast.LENGTH_SHORT).show();
        }
    }

    private void chooseTime() {
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        mTimePicker = new TimePickerDialog(NewPlan.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String status = "AM";
                hour = selectedHour;
                minute = selectedMinute;
                if (selectedHour > 11) {
                    // If the hour is greater than or equal to 12
                    // Then the current AM PM status is PM
                    status = "PM";
                }
                int hour_of_12_hour_format;

                if (selectedHour > 11) {
                    hour_of_12_hour_format = selectedHour - 12;
                } else {
                    hour_of_12_hour_format = selectedHour;
                }

                timeText.setText(hour_of_12_hour_format + " : " + selectedMinute + " :" + status);
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void chooseDate() {
        mYear = c.get(Calendar.YEAR); // current year
        mMonth = c.get(Calendar.MONTH); // current month
        mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        datePickerDialog = new DatePickerDialog(NewPlan.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                mYear = year;
                mMonth = month;
                mDay = day;
                dateText.setText(day + "/" + (month + 1) + "/" + year);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    private String initialize() {
        radioGroup = findViewById(R.id.radioGrp);
        tripName = findViewById(R.id.TripNameNew);
        startPosition = findViewById(R.id.startPosition);
        endPosition = findViewById(R.id.destination);
        save = findViewById(R.id.saveToDatabase);
        dateBtn = findViewById(R.id.chooseDate);
        timeBtn = findViewById(R.id.chooseTime);
        timeText = findViewById(R.id.timeText);
        dateText = findViewById(R.id.dateText);

       databaseReference = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();
        fu=mAuth.getCurrentUser();
      String id=  createAnewUser();
        return id;
    }

    private void setAlarm(boolean isNotification) {
        AlarmManager manager = (AlarmManager) getSystemService(this.ALARM_SERVICE);
        Intent intent;
        PendingIntent pendingIntent;
        if (isNotification) {
            intent = new Intent(this, NotificationReciever.class);
            pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        } else {
            intent = new Intent(this, AlarmReciever.class);
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        }

        c.set(mYear, mMonth, mDay, hour, minute, 0);
        manager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
                pendingIntent);
        Log.i("trace", String.valueOf(c));
        Log.i("trace", String.valueOf(c.getTimeInMillis()));
        Log.i("trace", String.valueOf(c.getTimeZone()));
        Log.i("trace", String.valueOf(c.getTime()));
    }
    private String createAnewUser() {

        Intent intent = getIntent();

        String id=fu.getUid();

        String Name = intent.getStringExtra("SEND_TEXT1");
        String Email=intent.getStringExtra("SEND_TEXT2");
        String Password=intent.getStringExtra("SEND_TEXT3");


        User user =new User(Name,Email,Password);

        databaseReference.child(id).setValue(user);
        return  id;

    }


}
