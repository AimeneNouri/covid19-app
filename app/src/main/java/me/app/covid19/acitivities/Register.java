package me.app.covid19.acitivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import me.app.covid19.MainActivity;
import me.app.covid19.R;
import me.app.covid19.models.Users;

public class Register extends AppCompatActivity {

    private EditText UserName, UserEmail, UserPassword, ConfirmPassword;
    private Button SignUpButton;
    private TextView togglePassword, toggleConfirmPassword, AlreadyHaveAccount;

    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference();

        UserName = findViewById(R.id.Username);
        UserEmail = findViewById(R.id.UserEmail);
        UserPassword = findViewById(R.id.PasswordUser);
        ConfirmPassword = findViewById(R.id.ConfirmPasswordUser);
        SignUpButton = findViewById(R.id.login_button);
        togglePassword = findViewById(R.id.showHidePassword);
        toggleConfirmPassword = findViewById(R.id.showHideConfirmPassword);
        AlreadyHaveAccount = findViewById(R.id.alreadyHaveAccount);

        UserPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        ConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        UserPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (UserPassword.getText().length() > 0)
                {
                    togglePassword.setVisibility(View.VISIBLE);
                }
                else
                {
                    togglePassword.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (ConfirmPassword.getText().length() > 0)
                {
                    toggleConfirmPassword.setVisibility(View.VISIBLE);
                }
                else
                {
                    toggleConfirmPassword.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //password
        togglePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (togglePassword.getText() == "SHOW")
                {
                    togglePassword.setText("HIDE");
                    UserPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    UserPassword.setSelection(UserPassword.length());
                }
                else
                {
                    togglePassword.setText("SHOW");
                    UserPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    UserPassword.setSelection(UserPassword.length());
                }
            }
        });

        //confirmPassword
        toggleConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleConfirmPassword.getText() == "SHOW")
                {
                    toggleConfirmPassword.setText("HIDE");
                    ConfirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ConfirmPassword.setSelection(ConfirmPassword.length());
                }
                else
                {
                    toggleConfirmPassword.setText("SHOW");
                    ConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ConfirmPassword.setSelection(ConfirmPassword.length());
                }
            }
        });

        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });

        loadingBar = new ProgressDialog(this);
        AlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(Register.this, Login.class);
                startActivity(homeIntent);
            }
        });
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.checkbox:
                if (checked){
                    SignUpButton.setEnabled(true);
                }
                else {
                    SignUpButton.setEnabled(false);
                }

        }
    }


    public void createNewAccount(){
        final String email = UserEmail.getText().toString();
        final String password = UserPassword.getText().toString();
        final String userName = UserName.getText().toString();
        final String confirmPassword = ConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password) && TextUtils.isEmpty(confirmPassword) && TextUtils.isEmpty(userName)){
            Toast.makeText(this, "Please fill in the blank", Toast.LENGTH_SHORT).show();
            SignUpButton.setBackgroundResource(R.drawable.login_button);
        }
        else if(TextUtils.isEmpty(userName))
        {
            Toast.makeText(this, "Please Enter your Name!!", Toast.LENGTH_SHORT).show();
        }
        else if (userName.length() > 15)
        {
            Toast.makeText(this, "your name is too big", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please Enter your email!!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please Enter your password!!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(confirmPassword))
        {
            Toast.makeText(this, "Please Confirm your password!!", Toast.LENGTH_SHORT).show();
        }
        else if (!password.equals(confirmPassword))
        {
            Toast.makeText(this, "password and confirm password are not identical", Toast.LENGTH_SHORT).show();
        }

        else {
            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please wait, while we were creating new account for you");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();


            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        String deviceToken = FirebaseInstanceId.getInstance().getToken();
                        String currentUserID = mAuth.getCurrentUser().getUid();

                        Users userInfo = new Users();
                        userInfo.setEmail(email);
                        userInfo.setUserName(userName);
                        userInfo.setPassword(password);
                        userInfo.setConfirmPassword(confirmPassword);
                        userInfo.setDevice_token(deviceToken);

                        RootRef.child("Users").child(currentUserID).setValue(userInfo);

                        sendUserToHome();
                        Toast.makeText(Register.this, "Account Created Successfully!", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                    else {
                        String message = task.getException().toString();
                        Toast.makeText(Register.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }

    private void sendUserToHome() {
        Intent homeIntent = new Intent(Register.this, MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();
    }
}
