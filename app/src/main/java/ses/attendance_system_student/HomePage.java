package ses.attendance_system_student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {
    //ini list
    private ListView subjectLists;
    TextView welcome;
    //firebase
    private DatabaseReference mSubjectRef;
    private FirebaseAuth bAuth;
    FirebaseDatabase database ;
    DatabaseReference myRef ;
    String currentUserID;
    Intent intent;
    List<Pair<String, String>> infoCollect;
    RecyclerView recyclerView;
    HelperAdaptor helperAdaptor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_home_page);

        bAuth = FirebaseAuth.getInstance();

        if(bAuth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
        welcome = findViewById(R.id.welcome);

        currentUserID = bAuth.getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef =  FirebaseDatabase.getInstance().getReference().child("Student").child(currentUserID).child("student_name");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                welcome.setText("Welcome "+ value);
                Log.d("TAG", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
        mSubjectRef = FirebaseDatabase.getInstance().getReference().child("Student").child(currentUserID).child("student_subjects");

        recyclerView= findViewById(R.id.subjectList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        infoCollect= new ArrayList<>();
        mSubjectRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()) {
                    Pair data = new Pair(ds.getKey(), ds.getValue());
                    infoCollect.add(data);
                    //  Log.v("test add", String.valueOf(infoCollect.add(data)));
                }
                helperAdaptor = new HelperAdaptor((infoCollect), HomePage.this);
                recyclerView.setAdapter(helperAdaptor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}