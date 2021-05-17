package ses.attendance_system_student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.github.ybq.android.spinkit.animation.AnimationUtils.start;

public class CodeAuthActivity extends AppCompatActivity {
    Intent intent;
    DatabaseReference sessionRef;
    EditText et_code;
    FrameLayout btn_submit;
    String subject;
    String session_id;
    String student_id;
    String student_name;
    ImageView arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_code_auth);
        intent = getIntent();
        arrow = findViewById(R.id.arrow);
        et_code = findViewById(R.id.et_code);
        btn_submit = findViewById(R.id.btn_code_submit);
        subject = intent.getStringExtra("subject");
        session_id = intent.getStringExtra("session_id");
        student_id = intent.getStringExtra("student_id");
        student_name = intent.getStringExtra("student_name");
        sessionRef = FirebaseDatabase.getInstance().getReference(
                "Session"
                        + "/"
                        + subject
                        + "/"
                        + session_id
        );
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionRef.child("session_code").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(et_code.getText().toString().equals(snapshot.getValue(String.class))){
                            Toast.makeText(getApplicationContext(),"Code matches",Toast.LENGTH_SHORT).show();
                            sessionRef.child("session_students").child(student_id).setValue(student_name);
                            arrow.setImageResource(R.drawable.animated_vector_check);
                            ((Animatable) arrow.getDrawable()).start();
                            new CountDownTimer(3000, 1000) {
                                //
//            @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {
                            startActivity(new Intent(getApplicationContext(), HomePage.class));
                                }
                            }.start();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Code does not match",Toast.LENGTH_SHORT).show();
                            arrow.setImageResource(R.drawable.animated_vector_cross);
                            ((Animatable) arrow.getDrawable()).start();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });


    }
}