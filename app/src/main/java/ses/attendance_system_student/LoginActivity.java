package ses.attendance_system_student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.animation.Animator;
import android.os.CountDownTimer;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowManager;
import androidx.core.content.ContextCompat;
import static android.view.View.VISIBLE;
public class LoginActivity extends AppCompatActivity {

    private ImageView bookIconImageView;
    private TextView bookITextView;
    private ProgressBar loadingProgressBar;
    private RelativeLayout rootView, afterAnimationView;

    TextInputEditText email, password;
    Button btn_login;
    FirebaseAuth auth;
    TextView forgot_password;
    FirebaseUser firebaseUser;


    //auto login
//    @Override
//    protected void onStart() {
//        super.onStart();
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (firebaseUser != null) {
//
//            Intent intent = new Intent(LoginActivity.this, FacialRecActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       //animation
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        //animation
        initViews();
        new CountDownTimer(5000, 1000) {
            //
//            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                bookITextView.setVisibility(View.GONE);
                loadingProgressBar.setVisibility(View.GONE);
                rootView.setBackgroundColor(ContextCompat.getColor(LoginActivity    .this, R.color.colorSplashText));
                bookIconImageView.setImageResource(R.drawable.utsblacklogo);
//                loginbtn.setBackgroundColor(R.drawable.button_drawable);
                startAnimation();
            }
        }.start();
        //authentication start
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        forgot_password = findViewById(R.id.forgot_password);

        auth = FirebaseAuth.getInstance();
        // move to forgotPassword Activity
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(  new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        //log in process
        //1. get the data from the input fields
        //2. check if the data is valid(empty, not email, etc)
        //3. check with firebase authentication method
        //4. login if the email and password are correct
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String txt_email = email.getText().toString() + "@student.uts.edu.au";
                String txt_email = email.getText().toString() ;
                String txt_password = password.getText().toString();
                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(LoginActivity.this, "All Fields are Required", Toast.LENGTH_SHORT).show();
                } else {


                    auth.signInWithEmailAndPassword(txt_email, txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(LoginActivity.this, FacialRecActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });

    }
    private void initViews() {
        bookIconImageView = findViewById(R.id.bookIconImageView);
        bookITextView = findViewById(R.id.bookITextView);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        rootView = findViewById(R.id.rootView);
        afterAnimationView = findViewById(R.id.afterAnimationView);

    }
    private void startAnimation() {
        ViewPropertyAnimator viewPropertyAnimator = bookIconImageView.animate();
        viewPropertyAnimator.x(20f);
        viewPropertyAnimator.y(20f);
        viewPropertyAnimator.setDuration(1000);
        viewPropertyAnimator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }
            //
            @Override
            public void onAnimationEnd(Animator animation) {
                afterAnimationView.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}