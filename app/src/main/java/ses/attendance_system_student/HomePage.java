package ses.attendance_system_student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
    //firebase
    private DatabaseReference mSubjectRef;
    private FirebaseAuth bAuth;
    String currentUserID;

    List<Pair<String, String>> infoCollect;
    RecyclerView recyclerView;
    HelperAdaptor helperAdaptor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        bAuth = FirebaseAuth.getInstance();


        if(bAuth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
        currentUserID = bAuth.getCurrentUser().getUid();
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
                helperAdaptor = new HelperAdaptor((infoCollect), getApplicationContext());
                recyclerView.setAdapter(helperAdaptor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}