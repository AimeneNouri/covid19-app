package me.app.covid19.acitivities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import me.app.covid19.R;

public class UserInfo extends AppCompatActivity {

    EditText userName, userEmail, userCIN, userBirth;
    Calendar myCalendar = Calendar.getInstance();
    ImageView backButton;
    Button Save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);


        userName = findViewById(R.id.UserName);
        userEmail = findViewById(R.id.UserEmail);
        userCIN = findViewById(R.id.UserCIN);
        userBirth = findViewById(R.id.UserBirth);
        backButton = findViewById(R.id.backButton);
        Save = findViewById(R.id.save_button);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        userBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                new DatePickerDialog(UserInfo.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        userBirth.setText(sdf.format(myCalendar.getTime()));
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.checkboxMen:
                if (checked){
                    Save.setEnabled(true);
                }
                else {
                    Save.setEnabled(false);
                }
            case R.id.checkboxWomen:
                if (checked){
                    Save.setEnabled(true);
                }
                else {
                    Save.setEnabled(false);
                }
        }
    }
}
