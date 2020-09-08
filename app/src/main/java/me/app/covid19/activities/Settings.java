package me.app.covid19.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import me.app.covid19.R;
import me.app.covid19.activities.SettingsItems.Change_password;
import me.app.covid19.activities.SettingsItems.EditProfile;

public class Settings extends AppCompatActivity {

    ImageView backButton;

    private TextView UserName, UserEmail;
    private CircleImageView UserPicture;

    private String currentUserId;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private FirebaseUser user;

    private TextView MyGithub;

    private RelativeLayout edit_profile, change_password, change_location, give_feedback, share_app, rate_app, exit_app, layout_1, layout1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Initialisation();

        RetrieveUserName_Email();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        exit_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this, R.style.AlertDialogTheme);
                builder.setTitle("Confirm exit");
                builder.setIcon(R.drawable.ic_exit_to_app_black_24dp);
                builder.setMessage("Are you sure you want to exit the app");
                builder.setCancelable(false);

                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                });

                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        share_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationInfo api = getApplicationContext().getApplicationInfo();
                String apkpath = api.sourceDir;
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("application/vnd.android.package-archive");
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(apkpath)));
                startActivity(Intent.createChooser(shareIntent, "Share App via"));
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

        MyGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://github.com/AimeneNouri";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
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
        give_feedback = findViewById(R.id.feedback);
        share_app = findViewById(R.id.share_app);
        rate_app = findViewById(R.id.rate_app);
        exit_app = findViewById(R.id.exit);
        layout_1 = findViewById(R.id.layout_1);
        layout1 = findViewById(R.id.layout1);
        MyGithub = findViewById(R.id.AimeneNouri);

        MyGithub.setText(Html.fromHtml("<u>"+ MyGithub.getText() +"</u>"));

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }
}
