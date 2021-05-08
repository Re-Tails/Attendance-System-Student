package ses.attendance_system_student;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HelperAdaptor extends RecyclerView.Adapter {

    List<Pair<String, String>> infoCollectList;
    private Context mContext;
    private DatabaseReference mSubjectRef, mJoinRef, mStudentRef;
    InfoCollect studentJoin;
    private String currentstudentid, currentUserID, Joined;
    private ArrayList<InfoCollect> dlist = new ArrayList<>();
    private ArrayList<sessiondata> list = new ArrayList<>();
    private FirebaseAuth bAuth;



    public HelperAdaptor(List<Pair<String, String>> infoCollectList, Context mContext) {
        this.infoCollectList = infoCollectList;
        this.mContext= mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.subject_layout,parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_layout,parent, false);
        ViewHolderClass viewHolderClass = new ViewHolderClass(view);

        bAuth = FirebaseAuth.getInstance();
        currentUserID = bAuth.getCurrentUser().getUid();
        mStudentRef= FirebaseDatabase.getInstance().getReference().child("Student").child(currentUserID);
        mStudentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    currentstudentid = snapshot.child("student_id").getValue().toString();
                    Log.v("studentid", String.valueOf(currentstudentid));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return viewHolderClass;
    }

    private static class sessiondata{
        public String date, time, endTime;

        public sessiondata(String date, String time, String endTime){
            this.date = date;
            this.time = time;
            this.endTime = endTime;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderClass viewHolderClass=(ViewHolderClass)holder;
        Pair<String, String> infoCollect=infoCollectList.get(position);
        viewHolderClass.sub1.setText(infoCollect.second);
       // viewHolderClass.sub2.setText(infoCollect);
        Log.v("test4", String.valueOf(infoCollect));
        ((ViewHolderClass) holder).menuPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, v);
                popupMenu.getMenuInflater().inflate(R.menu.pop_menu,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.Join:
                                Toast.makeText(mContext, "Joining", Toast.LENGTH_SHORT).show();
                                mSubjectRef = FirebaseDatabase.getInstance().getReference().child("Session").child(infoCollect.first);
                                mSubjectRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            Iterator<DataSnapshot> items = snapshot.getChildren().iterator();
                                            list.clear();
                                            while (items.hasNext()) {
                                                DataSnapshot item = items.next();
                                                String date, time, endTime;
                                                date = item.child("session_date").getValue().toString();
                                                time = item.child("session_start_time").getValue().toString();
                                                endTime = item.child("session_end_time").getValue().toString();
                                                sessiondata entry = new sessiondata(date, time, endTime);
                                                list.add(entry);
                                                Log.v("entries", String.valueOf(list));
                                            }
                                            mJoinRef= FirebaseDatabase.getInstance().getReference().child("Join").child(infoCollect.first).child("student_id");
                                            Joined = snapshot.getValue().toString();
                                            if(Joined == currentstudentid) {
                                                Toast.makeText(mContext, "You have already joined", Toast.LENGTH_SHORT).show();
                                                //this currently doesnt work
                                            } else {
                                                mJoinRef.setValue(currentstudentid);
                                            }


                                        } else {
                                            Toast.makeText(mContext, "No sessions available", Toast.LENGTH_SHORT).show();
                                        }
                                     }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                break;
                            case R.id.delete:
                                Toast.makeText(mContext, "fake button pressed", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                });
                //mSubjectRef = FirebaseDatabase.getInstance().getReference().child("Session").child(infoCollect.first);
                //now set an onclick listener for the join button
                Log.v("code", String.valueOf(infoCollect.first));
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.v("count", String.valueOf(infoCollectList.size()));
        return infoCollectList.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder{
        TextView sub1, sub2;
        ImageView menuPopup;
        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            sub1=itemView.findViewById(R.id.sub1);
            menuPopup = itemView.findViewById(R.id.menuMore);
           // sub2=itemView.findViewById(R.id.sub2);
        }
    }

}
