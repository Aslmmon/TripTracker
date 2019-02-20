package com.example.android.plannertracker.TripDetails;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.example.android.plannertracker.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class EditActtivity extends AppCompatActivity {
    EditText startPosition, endPosition,tripName;
    RadioGroup radioGroup;
    int tripTypeChooser;
    RadioButton radioButton,single,round;
    DatabaseReference databaseReference;
    Button date, time, update;
    TextView dateText, timeText;
    DatePickerDialog datePickerDialog;
    TrackerInformation trackerInformation;
    String tripNameChosen,startLocation,endLocation,dateChosen,timeChosen,IDTaken,TripType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        getExtras();
        initialize();
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseDate();
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseTime();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateToDatabase();
            }
        });

    }

    private void getExtras() {
        tripNameChosen = getIntent().getStringExtra("tripName");
        startLocation = getIntent().getStringExtra("start");
        endLocation = getIntent().getStringExtra("end");
        dateChosen = getIntent().getStringExtra("date");
        timeChosen = getIntent().getStringExtra("time");
        IDTaken = getIntent().getStringExtra("id");
        TripType = getIntent().getStringExtra("tripType");

        tripName.setText(tripNameChosen);
        startPosition.setText(startLocation);
        endPosition.setText(endLocation);
        dateText.setText(dateChosen);
        timeText.setText(timeChosen);
    }

    private void updateToDatabase() {
        String Note = "";
        String TripType = radioButton.getText().toString();
        String theTripName = tripName.getText().toString();
        String start = startPosition.getText().toString();
        String destination = endPosition.getText().toString();
        String date = dateText.getText().toString();
        String time = timeText.getText().toString();

        String ID = IDTaken;
        Log.i("trace", "updateToDatabase:  "+ ID);
        if (!TextUtils.isEmpty(start) && !TextUtils.isEmpty(date) &&
                !TextUtils.isEmpty(time) && !TextUtils.isEmpty(theTripName)
                && !TextUtils.isEmpty(TripType)) {
            TrackerInformation trackerInformation = new TrackerInformation(ID, start,
                    destination, theTripName, time, date,TripType);
            databaseReference.child(ID).setValue(trackerInformation);
            Toast.makeText(this, "Done Updated", Toast.LENGTH_SHORT).show();

            tripName.setText("");
            startPosition.setText("");
            endPosition.setText("");
            dateText.setText("");
            timeText.setText("");
        }else{
            Toast.makeText(this, "Enter Valid bodies", Toast.LENGTH_SHORT).show();
        }
    }

    private void chooseTime() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(EditActtivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String status = "AM";
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
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        datePickerDialog = new DatePickerDialog(EditActtivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                dateText.setText(day + "/" + (month + 1) + "/" + year);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void initialize() {

        radioGroup = findViewById(R.id.radioGrp);
        tripTypeChooser = radioGroup.getCheckedRadioButtonId();
        radioButton.findViewById(tripTypeChooser);
        single = findViewById(R.id.single);
        round = findViewById(R.id.round);

        trackerInformation = new TrackerInformation();
        tripName = findViewById(R.id.TripNameEdit);
        startPosition = findViewById(R.id.startPosition);
        endPosition = findViewById(R.id.destination);
        update = findViewById(R.id.updateInDatabase);
        date = findViewById(R.id.chooseDate);
        time = findViewById(R.id.chooseTime);
        timeText = findViewById(R.id.timeText);
        dateText = findViewById(R.id.dateText);
        databaseReference = FirebaseDatabase.getInstance().getReference("Trip Data");

    }
}
