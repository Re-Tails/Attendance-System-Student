package ses.attendance_system_student;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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

    // List<InfoCollect> infoCollect;
    ArrayList<InfoCollect> infoCollectList;
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
        infoCollectList= new ArrayList<>();


        mSubjectRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                InfoCollect infoCollect = snapshot.getValue(InfoCollect.class);
                infoCollectList.add(infoCollect);
                helperAdaptor.notifyItemInserted(infoCollectList.size());
                Log.v("Subject", "loaded");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
}