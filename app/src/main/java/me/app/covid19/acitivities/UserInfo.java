package me.app.covid19.acitivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import me.app.covid19.R;

public class UserInfo extends AppCompatActivity {

    EditText userName, userEmail, userCIN, userBirth, userPhone;
    Calendar myCalendar = Calendar.getInstance();
    ImageView backButton, logout;
    Button Save;
    CheckBox men, women;
    CircleImageView userImage;

    private String currentUserId;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private FirebaseUser user;

    private RelativeLayout relativeLayout;
    private ProgressDialog loadingBar;
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        Initialisation();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        userBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(UserInfo.this,R.style.AlertDialogTheme, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        relativeLayout = findViewById(R.id.layoutSettings);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = UserInfo.this.getCurrentFocus();
                if (view != null)
                {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    userName.clearFocus();
                    userEmail.clearFocus();
                    userCIN.clearFocus();
                    userPhone.clearFocus();
                }
            }
        });

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserInfo.this, R.style.AlertDialogTheme);
                builder.setTitle(getResources().getString(R.string.confirmLogout));
                builder.setIcon(R.drawable.ic_exit_to_app_black_24dp);
                builder.setMessage(getResources().getString(R.string.areYouSure));
                builder.setCancelable(false);

                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        sendUserToLoginActivity();
                    }
                });

                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                builder.show();
            }
        });

        RetrieveUserInfo();
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(UserInfo.this);
            }
        });
    }

    private void setAppLocate(String localeCode){
        Resources res = getResources();
        DisplayMetrics displayMetrics = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            conf.setLocale(new Locale(localeCode.toLowerCase()));
        }
        else {
            conf.locale = new Locale(localeCode.toLowerCase());
        }
        res.updateConfiguration(conf, displayMetrics);
    }

    private void Initialisation() {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        currentUserId = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();

        userName = findViewById(R.id.UserName);
        userEmail = findViewById(R.id.UserEmail);
        userCIN = findViewById(R.id.UserCIN);
        userBirth = findViewById(R.id.UserBirth);
        backButton = findViewById(R.id.backButton);
        Save = findViewById(R.id.save_button);
        logout = findViewById(R.id.logoutButton);
        userPhone = findViewById(R.id.UserPhoneNumber);
        men = findViewById(R.id.checkboxMen);
        women = findViewById(R.id.checkboxWomen);
        userImage = findViewById(R.id.userPicture);
        loadingBar = new ProgressDialog(this);
    }

    /*public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioEnglish:
                if (checked)
                    // English language
                    setAppLocate("en");
                break;
            case R.id.radioArab:
                if (checked)
                    // Arabic language
                    setAppLocate("ar");
                break;
        }
    }*/

    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(UserInfo.this, Login.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }



    /*public void onCheckboxClicked(View view) {
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
    }*/

    private void saveInfo() {
        String setUserName = userName.getText().toString();
        String setUserEmail = userEmail.getText().toString();
        String setUserCIN = userCIN.getText().toString();
        String setUserPhone = userPhone.getText().toString();
        String setUserBirth = userBirth.getText().toString();
        String genderMale = "";
        String genderFemale = "";

        if (TextUtils.isEmpty(setUserName))
        {
            Toast.makeText(this, "Please Write your Name", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(setUserEmail))
        {
            Toast.makeText(this, "Please Write your Email", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(setUserCIN))
        {
            Toast.makeText(this, "Please Write your CIN", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(setUserPhone))
        {
            Toast.makeText(this, "Please Write your Phone", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(setUserBirth))
        {
            Toast.makeText(this, "Please Write your birth day", Toast.LENGTH_SHORT).show();
        }
        if (men.isChecked()){
            genderMale = men.getText().toString();
        }
        if (women.isChecked()){
            genderFemale = women.getText().toString();
        }

        else {
            HashMap<String, Object> profileMap = new HashMap<>();
            profileMap.put("uid", currentUserId);
            profileMap.put("userName", setUserName);
            profileMap.put("email", setUserEmail);
            profileMap.put("cin", setUserCIN);
            profileMap.put("phone", setUserPhone);
            profileMap.put("birth", setUserBirth);
            if (men.isChecked()){
                profileMap.put("gender", genderMale);
            }
            else if (women.isChecked()){
                profileMap.put("gender", genderFemale);
            }

            RootRef.child("Users")
                    .child(currentUserId)
                    .updateChildren(profileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(UserInfo.this, "Profile updated Successfully", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                String message = task.getException().toString();
                                Toast.makeText(UserInfo.this, "Error : "+ message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        userBirth.setText(sdf.format(myCalendar.getTime()));
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    private void RetrieveUserInfo() {
        RootRef.child("Users").child(currentUserId)
                .addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if((dataSnapshot.exists()) && (dataSnapshot.hasChild("cin")) && (dataSnapshot.hasChild("phone")) && (dataSnapshot.hasChild("birth")) && (dataSnapshot.hasChild("userName")) && (dataSnapshot.hasChild("gender")) && (dataSnapshot.hasChild("UserPicture")))
                        {
                            String retrieveUserName = dataSnapshot.child("userName").getValue().toString();
                            String retrieveEmail = dataSnapshot.child("email").getValue().toString();
                            String retrieveCIN= dataSnapshot.child("cin").getValue().toString();
                            String retrievePhone= dataSnapshot.child("phone").getValue().toString();
                            String retrieveBirth= dataSnapshot.child("birth").getValue().toString();
                            String retrieveGender= dataSnapshot.child("gender").getValue().toString();
                            String retrieveUserPicture= dataSnapshot.child("UserPicture").getValue().toString();

                            userName.setText(retrieveUserName);
                            userEmail.setText(retrieveEmail);
                            userCIN.setText(retrieveCIN);
                            userPhone.setText(retrievePhone);
                            userBirth.setText(retrieveBirth);
                            Picasso.get().load(retrieveUserPicture).placeholder(R.drawable.person).into(userImage);
                            if (retrieveGender.equals("Men")){
                                men.setChecked(true);
                            }
                            else{
                                women.setChecked(true);
                            }
                        }
                        else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("userName")))
                        {
                            String retrieveUserName = Objects.requireNonNull(dataSnapshot.child("userName").getValue()).toString();
                            String retrieveEmail = Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString();

                            userName.setText(retrieveUserName);
                            userEmail.setText(retrieveEmail);
                        }
                        else
                        {
                            Toast.makeText(UserInfo.this, "Please set & update your profile information", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                loadingBar.setTitle("Updating User Profile");
                loadingBar.setMessage("Please wait, while your picture is updating... ");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                Uri resultUri = result.getUri();

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();

                final StorageReference UserImageRef = storageRef.child("Covid19" + "/PROFILES/" + "/PICTURES/" + resultUri.toString().split("/")[resultUri.toString().split("/").length - 1]);

                InputStream stream = null;
                try {
                    stream = new FileInputStream(new File(resultUri.getPath()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                uploadTask = UserImageRef.putStream(stream);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserInfo.this, "Failed", Toast.LENGTH_SHORT).show();
                        String message = e.toString();
                        Toast.makeText(UserInfo.this, "ERROR: " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(UserInfo.this, "Uploaded", Toast.LENGTH_SHORT).show();

                        UserImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String link = uri.toString();
                                Picasso.get().load(link).into(userImage);

                                RootRef.child("Users").child(currentUserId).child("UserPicture")
                                        .setValue(link).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            loadingBar.dismiss();
                                            Toast.makeText(UserInfo.this, "Photo updated Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            loadingBar.dismiss();
                                            Toast.makeText(UserInfo.this, "Error Uploading", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
            }
        }
    }
}
