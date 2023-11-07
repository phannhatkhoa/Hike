package com.example.hike.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.room.Room;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hike.R;
import com.example.hike.database.AppDatabase;
import com.example.hike.models.Hike;

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {
    private AppDatabase appDatabase;
    private TextView dobControl;
    String selectedOption;
    RadioButton  radioButton;
    RadioGroup radioGroup;
    Spinner sp;
    EditText descriptionTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "hike_db")
                .allowMainThreadQueries() // For simplicity, don't use this in production
                .build();

        //button is used to move to home activity
        Button homeButton = findViewById(R.id.home_button);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to go back to the MainActivity
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                startActivity(intent);
            }
        });

        radioGroup=findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            radioButton = findViewById(checkedId);
            selectedOption = radioButton.getText().toString();
            Toast.makeText(this, "Parking available: " + selectedOption, Toast.LENGTH_SHORT).show();
        });

        //select difficulty level
        String[] difLevel = {"High", "Medium","Low"};
        sp = findViewById(R.id.spinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, difLevel);
        // Connect adapter to spinner
        sp.setAdapter(dataAdapter);



        Button saveDetailsButton = findViewById(R.id.saveDetailsButton);
        saveDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDetails();
            }
        });
        dobControl = findViewById(R.id.dob_control2);
        dobControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

    }
    private void saveDetails() {
        EditText nameTxt = findViewById(R.id.namehike_input);
        EditText locationTxt = findViewById(R.id.location_input);
        TextView dobTxt = findViewById(R.id.dob_control2);
        EditText lengthTxt = findViewById(R.id.length_input);
        Spinner sp = findViewById(R.id.spinner);
        descriptionTxt = findViewById(R.id.description_input);

        String name = nameTxt.getText().toString();
        String dob = dobTxt.getText().toString();
        String location = locationTxt.getText().toString();
        String length = lengthTxt.getText().toString();
        String level = sp.getSelectedItem().toString();
        String description = descriptionTxt.getText().toString();

        displayAlert(name, location, dob,selectedOption,length,level);
    }

    public void displayAlert(String name, String location, String dob, String parking, String length, String level) {
        if (name.isEmpty() || location.isEmpty() || dob.isEmpty() || length.isEmpty() || level.isEmpty()) {
          AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Error");
                    builder.setMessage("All required fields must be filled")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
        }else {
            new AlertDialog.Builder(this)
                    .setTitle("Confirmation")
                    .setMessage(
                            "New hike will be added" + "\n" +
                                    "Name: " + name + "\n" +
                                    "Location: " + location + "\n" +
                                    "Date: " + dob + "\n" +
                                    "Parking available: " + parking + "\n" +
                                    "Length of the hike: " + length + "\n" +
                                    "Difficult level: " + level + "\n\n" +
                                    "Are you sure?"
                    )
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Hike hike = new Hike();
                            hike.name = name;
                            hike.location = location;
                            hike.dob = dob;
                            hike.parking = selectedOption;
                            hike.length = length;
                            hike.difficulty = level;
                            hike.description = descriptionTxt.getText().toString();
                            long hikeId = appDatabase.hikeDao().insertHike(hike);
                            Toast.makeText(MainActivity.this, "Hike has been created with id: " + hikeId, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })
                    .show();
        }
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            LocalDate day1 = LocalDate.now();
            int year = day1.getYear();
            int month = day1.getMonthValue();
            int day = day1.getDayOfMonth();
            return new DatePickerDialog(getActivity(), this, year, month - 1, day);
        }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            LocalDate dob = LocalDate.of(year, month + 1, day);
            ((MainActivity) getActivity()).updateDOB(dob);
        }
    }
    public void updateDOB(LocalDate dob) {
        dobControl.setText(dob.toString());
    }
}