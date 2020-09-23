package me.app.covid19.activities.SettingsItems;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import me.app.covid19.activities.profile_setting;

public class EditProfile extends AppCompatActivity{

    private ImageView cancel;
    private Button validate;
    private CircleImageView userImage, updatePicture;
    private EditText userName, userEmail, userCIN, userPhone, userBirth, userCountry;
    private RadioButton male, female;

    private String currentUserId, password;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private FirebaseUser user;

    private RelativeLayout relativeLayout;

    ProgressDialog progressDialog;
    private StorageTask uploadTask;

    Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Initialisation();
        RetrieveUserInfo();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate_Change();
            }
        });

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = EditProfile.this.getCurrentFocus();
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

        updatePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(EditProfile.this);
            }
        });

        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
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
                new DatePickerDialog(EditProfile.this, dateSetListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        userBirth.setText(sdf.format(myCalendar.getTime()));
    }

    private void showDialog(){
        progressDialog = new ProgressDialog(EditProfile.this);

        progressDialog.setCancelable(false);
        progressDialog.show();

        progressDialog.setContentView(R.layout.custom_dialog_update);

        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
    }

    private void validate_Change() {
        String setUserName = userName.getText().toString();
        final String setUserEmail = userEmail.getText().toString();
        String setUserCIN = userCIN.getText().toString();
        String setUserPhone = userPhone.getText().toString();
        String setUserBirth = userBirth.getText().toString();
        String genderMale = male.getText().toString();;
        String genderFemale = female.getText().toString();

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
        if (TextUtils.isEmpty(setUserBirth))
        {
            Toast.makeText(this, "Please Write your Country Name", Toast.LENGTH_SHORT).show();
        }

        else {
            showDialog();

            HashMap<String, Object> profileMap = new HashMap<>();
            profileMap.put("uid", currentUserId);
            profileMap.put("userName", setUserName);
            profileMap.put("email", setUserEmail);
            profileMap.put("cin", setUserCIN);
            profileMap.put("phone", setUserPhone);
            profileMap.put("birth", setUserBirth);

            if (male.isChecked()){
                profileMap.put("gender", genderMale);
            }
            else if (female.isChecked()){
                profileMap.put("gender", genderFemale);
            }

            RootRef.child("Users")
                    .child(currentUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        password = snapshot.child("password").getValue(String.class);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            RootRef.child("Users")
                    .child(currentUserId)
                    .updateChildren(profileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(EditProfile.this, "Profile updated Successfully", Toast.LENGTH_SHORT).show();
                                AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), password);
                                user.reauthenticate(authCredential)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                user.updateEmail(setUserEmail)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                progressDialog.dismiss();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                progressDialog.dismiss();
                                                                Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                            else
                            {
                                progressDialog.dismiss();
                                String message = task.getException().toString();
                                Toast.makeText(EditProfile.this, "Error : "+ message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void RetrieveUserInfo() {
        RootRef.child("Users").child(currentUserId)
                .addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if((dataSnapshot.exists()) && (dataSnapshot.hasChild("UserPicture")))
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
                            if (retrieveGender.equals("Male")){
                                male.setChecked(true);
                            }
                            else{
                                female.setChecked(true);
                            }
                        }

                        String retrieveUserName = dataSnapshot.child("userName").getValue().toString();
                        String retrieveEmail = dataSnapshot.child("email").getValue().toString();
                        String retrieveCIN= dataSnapshot.child("cin").getValue().toString();
                        String retrievePhone= dataSnapshot.child("phone").getValue().toString();
                        String retrieveBirth= dataSnapshot.child("birth").getValue().toString();
                        String retrieveGender= dataSnapshot.child("gender").getValue().toString();

                        userName.setText(retrieveUserName);
                        userEmail.setText(retrieveEmail);
                        userCIN.setText(retrieveCIN);
                        userPhone.setText(retrievePhone);
                        userBirth.setText(retrieveBirth);
                        if (retrieveGender.startsWith("m")){
                            male.setChecked(true);
                        }
                        else{
                            female.setChecked(true);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void Initialisation() {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        currentUserId = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();

        relativeLayout = findViewById(R.id.layout2);

        userName = findViewById(R.id.UserName);
        userCIN = findViewById(R.id.UserCIN);
        userPhone = findViewById(R.id.UserPhoneNumber);
        userEmail = findViewById(R.id.UserEmail);
        userBirth = findViewById(R.id.UserBirth);
        male = findViewById(R.id.radioMale);
        female = findViewById(R.id.radioFemale);

        userImage = findViewById(R.id.userPicture);
        updatePicture = findViewById(R.id.updatePicture);

        cancel = findViewById(R.id.cancel);
        validate = findViewById(R.id.validate);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
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
                        Toast.makeText(EditProfile.this, "Failed", Toast.LENGTH_SHORT).show();
                        String message = e.toString();
                        Toast.makeText(EditProfile.this, "ERROR: " + message, Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(EditProfile.this, "Uploaded", Toast.LENGTH_SHORT).show();

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
                                            Toast.makeText(EditProfile.this, "Photo updated Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(EditProfile.this, "Error Uploading", Toast.LENGTH_SHORT).show();
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
