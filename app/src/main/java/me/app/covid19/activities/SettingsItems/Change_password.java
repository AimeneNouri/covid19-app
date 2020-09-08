package me.app.covid19.activities.SettingsItems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import java.util.HashMap;

import me.app.covid19.R;

public class Change_password extends AppCompatActivity {

    private ImageView backButton, back, hideCurrentPassword, showCurrentPassword, hideNewPassword, showNewPassword, hideConfirmPassword, showConfirmPassword;
    private EditText currentPassword, newPassword, confirmNewPassword;
    private RelativeLayout relativeLayout, password_successfully, change_password_layout;
    private Button update;

    private String currentUserId, exist_password;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private FirebaseUser user;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Initialisation();

        //show hide functionality
        currentPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        newPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        confirmNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        currentPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (currentPassword.getText().length() > 0)
                {
                    showCurrentPassword.setVisibility(View.VISIBLE);
                }
                else
                {
                    showCurrentPassword.setVisibility(View.GONE);
                    hideCurrentPassword.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (newPassword.getText().length() > 0)
                {
                    showNewPassword.setVisibility(View.VISIBLE);
                }
                else
                {
                    showNewPassword.setVisibility(View.GONE);
                    hideNewPassword.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirmNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (confirmNewPassword.getText().length() > 0)
                {
                    showConfirmPassword.setVisibility(View.VISIBLE);
                }
                else
                {
                    showConfirmPassword.setVisibility(View.GONE);
                    hideConfirmPassword.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //show password
        showCurrentPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCurrentPassword.setVisibility(View.GONE);
                hideCurrentPassword.setVisibility(View.VISIBLE);
                currentPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                currentPassword.setSelection(currentPassword.length());
            }
        });

        showNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewPassword.setVisibility(View.GONE);
                hideNewPassword.setVisibility(View.VISIBLE);
                newPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                newPassword.setSelection(newPassword.length());
            }
        });

        showConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmPassword.setVisibility(View.GONE);
                hideConfirmPassword.setVisibility(View.VISIBLE);
                confirmNewPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                confirmNewPassword.setSelection(confirmNewPassword.length());
            }
        });

        //hide password

        hideCurrentPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCurrentPassword.setVisibility(View.VISIBLE);
                hideCurrentPassword.setVisibility(View.GONE);
                currentPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                currentPassword.setSelection(currentPassword.length());
            }
        });

        hideNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewPassword.setVisibility(View.VISIBLE);
                hideNewPassword.setVisibility(View.GONE);
                newPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                newPassword.setSelection(currentPassword.length());
            }
        });

        hideConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmPassword.setVisibility(View.VISIBLE);
                hideConfirmPassword.setVisibility(View.GONE);
                confirmNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                confirmNewPassword.setSelection(confirmNewPassword.length());
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = Change_password.this.getCurrentFocus();
                if (view != null)
                {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    currentPassword.clearFocus();
                    newPassword.clearFocus();
                    confirmNewPassword.clearFocus();
                }
            }
        });

    }

    private void showDialog(){
        progressDialog = new ProgressDialog(Change_password.this);

        progressDialog.setCancelable(false);
        progressDialog.show();

        progressDialog.setContentView(R.layout.password_custom_dialog);

        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
    }

    private void Initialisation(){
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        currentUserId = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        backButton = findViewById(R.id.backButton);
        currentPassword = findViewById(R.id.currentPassword);
        newPassword = findViewById(R.id.new_password);
        confirmNewPassword = findViewById(R.id.confirm_new_password);
        relativeLayout = findViewById(R.id.layout);
        update = findViewById(R.id.update_button);
        password_successfully = findViewById(R.id.password_successfully);
        change_password_layout = findViewById(R.id.change_password_layout);
        back = findViewById(R.id.back);

        showCurrentPassword = findViewById(R.id.showCurrentPassword);
        hideCurrentPassword = findViewById(R.id.HideCurrentPassword);
        showNewPassword = findViewById(R.id.showHideNewPassword);
        hideNewPassword = findViewById(R.id.HideNewPassword);
        showConfirmPassword = findViewById(R.id.showConfirmPassword);
        hideConfirmPassword = findViewById(R.id.HideConfirmPassword);
    }

    private void updatePassword() {
        String current_password = currentPassword.getText().toString();
        final String new_password = newPassword.getText().toString();
        String confirm_new_password = confirmNewPassword.getText().toString();

        RootRef.child("Users")
                .child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    exist_password = snapshot.child("password").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (current_password.isEmpty() && new_password.isEmpty() && confirm_new_password.isEmpty()){
            Toast.makeText(this, "please fill in the blank", Toast.LENGTH_SHORT).show();
        }else {
            if (current_password.equals(exist_password)){
                if (new_password.equals(confirm_new_password)){
                    showDialog();
                    HashMap<String, Object> passwordMap = new HashMap<>();
                    passwordMap.put("password", new_password);
                    passwordMap.put("confirmPassword", confirm_new_password);

                    RootRef.child("Users")
                            .child(currentUserId)
                            .updateChildren(passwordMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){

                                    }
                                    else
                                    {
                                        String message = task.getException().toString();
                                        Toast.makeText(Change_password.this, "Error : "+ message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), current_password);
                    user.reauthenticate(authCredential)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    user.updatePassword(new_password)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    //password updated successfully
                                                    progressDialog.dismiss();
                                                    change_password_layout.setVisibility(View.GONE);
                                                    password_successfully.setVisibility(View.VISIBLE);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(Change_password.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(Change_password.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }else {
                    Toast.makeText(this, "new password is not with the confirm password", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "current password is not right", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}
