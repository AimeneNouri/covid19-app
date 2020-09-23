package me.app.covid19.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import me.app.covid19.R;
import me.app.covid19.activities.SettingsItems.Change_password;
import me.app.covid19.activities.SettingsItems.Donate_me;
import me.app.covid19.activities.SettingsItems.EditProfile;
import me.app.covid19.activities.SettingsItems.Give_Feedback;

public class Settings extends AppCompatActivity {

    ImageView backButton;

    private TextView UserName, UserEmail;
    private CircleImageView UserPicture;

    private String currentUserId;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private FirebaseUser user;

    private TextView MyGithub;

    private RelativeLayout edit_profile, change_password, change_language, give_feedback, share_app, donate, logout, layout_1, layout1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_settings);

        Initialisation();

        RetrieveUserName_Email();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        share_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Stay Safe app (Covid-19 tracker)" + "\n\nDeveloped by Aimene Nouri");
                    String body = "Hey, download this app now :" + "\n\n" + "https://appsenjoy.com/files/f79e7fc23202a9bc45508c50f82b23b7.apk";
                    i.putExtra(Intent.EXTRA_TEXT, body);
                    startActivity(Intent.createChooser(i, "Share App via :"));

                }catch (Exception e){
                    Toast.makeText(Settings.this, "Sorry, \nThis Cannot be share", Toast.LENGTH_SHORT).show();
                }
            }
        });

        layout_1.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_transition));
        layout1.setAnimation(AnimationUtils.loadAnimation(this, R.anim.top_anim));
        backButton.setAnimation(AnimationUtils.loadAnimation(this, R.anim.top_anim));

        //edit_profile activity
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, EditProfile.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        //change_password activity
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, Change_password.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        //donate
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, Donate_me.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        //give_feedback
        give_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, Give_Feedback.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        MyGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://github.com/AimeneNouri";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
    }

    private void showDialog(){
        final Dialog dialog = new Dialog(Settings.this);
        dialog.setContentView(R.layout.logout_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button cancel = dialog.findViewById(R.id.cancel);
        Button logout = dialog.findViewById(R.id.logout_button);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                sendUserToLoginActivity();
            }
        });

        dialog.show();
    }

    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(Settings.this, Login.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void showChangeLanguageDialog() {

        final Dialog dialog = new Dialog(Settings.this);
        dialog.setContentView(R.layout.language_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final RadioButton english, french, arabic, germany;
        Button save;
        final TextView errorText;

        english = dialog.findViewById(R.id.radioEnglish);
        french = dialog.findViewById(R.id.radioFrench);
        arabic = dialog.findViewById(R.id.radioArabic);
        germany = dialog.findViewById(R.id.radioGerman);
        save = dialog.findViewById(R.id.save);
        errorText = dialog.findViewById(R.id.errorText);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (english.isChecked()){
                    setAppLocale("en");
                    dialog.dismiss();
                }
                else if (french.isChecked()){
                    setAppLocale("fr");
                    dialog.dismiss();
                }
                else if (arabic.isChecked()){
                    setAppLocale("ar");
                    dialog.dismiss();
                }
                else if (germany.isChecked()){
                    setAppLocale("de");
                    dialog.dismiss();
                }
                else {
                    errorText.setVisibility(View.VISIBLE);
                }
            }
        });

        dialog.show();
    }

    private void setAppLocale(String lang) {
        /*Resources res = getResources();
        DisplayMetrics displayMetrics = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(lang.toLowerCase()));
        res.updateConfiguration(conf, displayMetrics);*/
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    public void loadLocale() {
        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = preferences.getString("My_Lang", "");
        setAppLocale(language);
    }

    private void RetrieveUserName_Email() {
        RootRef.child("Users").child(currentUserId)
                .addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if((dataSnapshot.exists()) && (dataSnapshot.hasChild("UserPicture")))
                        {
                            String retrieveUserName = Objects.requireNonNull(dataSnapshot.child("userName").getValue()).toString();
                            String retrieveEmail = Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString();
                            String retrievePicture = Objects.requireNonNull(dataSnapshot.child("UserPicture").getValue()).toString();

                            UserName.setText(retrieveUserName);
                            UserEmail.setText(retrieveEmail);
                            Picasso.get().load(retrievePicture).into(UserPicture);
                        }
                        else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("userName")))
                        {
                            String retrieveUserName = Objects.requireNonNull(dataSnapshot.child("userName").getValue()).toString();
                            String retrieveEmail = Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString();

                            UserName.setText(retrieveUserName);
                            UserEmail.setText(retrieveEmail);
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
        backButton = findViewById(R.id.backButton);
        UserName = findViewById(R.id.UserName);
        UserPicture = findViewById(R.id.userPicture);
        UserEmail = findViewById(R.id.email);

        edit_profile = findViewById(R.id.edit_profile);
        change_password = findViewById(R.id.change_password);
        //change_language = findViewById(R.id.change_language);
        give_feedback = findViewById(R.id.feedback);
        share_app = findViewById(R.id.share_app);
        donate = findViewById(R.id.donate);
        logout = findViewById(R.id.exit);
        layout_1 = findViewById(R.id.layout_1);
        layout1 = findViewById(R.id.layout1);
        MyGithub = findViewById(R.id.AimeneNouri);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }
}
