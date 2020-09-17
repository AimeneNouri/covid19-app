package me.app.covid19.activities.SettingsItems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import me.app.covid19.R;

public class Give_Feedback extends AppCompatActivity {

    RatingBar ratting;
    Button sendFeedback;
    EditText feedback;
    TextView thanks, feedback_advice;
    ImageView backButton;

    private String currentUserId;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give__feedback);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();

        feedback = findViewById(R.id.feedback_editText);
        sendFeedback = findViewById(R.id.leave_feedback);
        backButton = findViewById(R.id.backButton);
        ratting = findViewById(R.id.ratting);
        thanks = findViewById(R.id.thanks);
        feedback_advice = findViewById(R.id.feedback_advice);
        progressBar = findViewById(R.id.progressBar);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        sendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeaveFeedback();
            }
        });
    }

    private void LeaveFeedback() {
        String Feedback = feedback.getText().toString();
        String rattingScore = String.valueOf(ratting.getRating());

        if (Feedback.isEmpty()){
            Toast.makeText(this, "Please fill in the blank", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(Feedback)) {
            Toast.makeText(this, "Please write your feedback", Toast.LENGTH_SHORT).show();
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            feedback.setVisibility(View.GONE);
            sendFeedback.setVisibility(View.GONE);
            ratting.setVisibility(View.GONE);

            HashMap<String, Object> profileMap = new HashMap<>();
            profileMap.put("uid", currentUserId);
            profileMap.put("feedback", Feedback);
            profileMap.put("rattingScore", rattingScore);

            RootRef.child("Users")
                    .child(currentUserId)
                    .updateChildren(profileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                //Toast.makeText(Give_Feedback.this, "Profile updated Successfully", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                thanks.setVisibility(View.VISIBLE);
                                feedback_advice.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                String message = task.getException().toString();
                                Toast.makeText(Give_Feedback.this, "Error : "+ message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}
