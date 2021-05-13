package ses.attendance_system_student;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import ses.attendance_system_student.model.Session;

public class HelperAdaptor extends RecyclerView.Adapter {

    List<Pair<String, String>> infoCollectList;
    List<Pair<String, String>> dateList;
    List<Pair<String, String>> startTimeList;
    List<Pair<String, String>> endTimeList;
    List<Pair<String, String>> idcodeList;
    private Context mContext;
    private DatabaseReference mSubjectRef, mSubjectRef2, mJoinRef, mStudentRef;
    InfoCollect studentJoin;
    private String currentstudentid, currentUserID, Joined = "0", currentStudentname;
    private ArrayList<InfoCollect> dlist = new ArrayList<>();
    private String masterID;
    private int mark = 0;

    private FirebaseAuth bAuth;
    List<Session> session_list;



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
        session_list = new ArrayList<>();
        mStudentRef= FirebaseDatabase.getInstance().getReference().child("Student").child(currentUserID);
        mStudentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    currentstudentid = snapshot.child("student_id").getValue().toString();
                    currentStudentname = snapshot.child("student_name").getValue().toString();
                   // Log.v("studentid", String.valueOf(currentstudentid));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return viewHolderClass;
    }




    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderClass viewHolderClass=(ViewHolderClass)holder;
        Pair<String, String> infoCollect=infoCollectList.get(position);

        viewHolderClass.sub1.setText(infoCollect.second);
        // viewHolderClass.sub2.setText(infoCollect);
       // Log.v("test4", String.valueOf(infoCollect));
        mSubjectRef2 = FirebaseDatabase.getInstance().getReference().child("Session").child(infoCollect.first);
        mSubjectRef2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Session session = snapshot.getValue(Session.class);
                session_list.add(session);


               // Log.v("details", String.valueOf(session.getSession_date()));
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
        ((ViewHolderClass) holder).menuPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, v);
                popupMenu.getMenuInflater().inflate(R.menu.pop_menu,popupMenu.getMenu());
                popupMenu.show();
                dateList = new ArrayList<>();
                startTimeList = new ArrayList<>();
                endTimeList = new ArrayList<>();
                idcodeList = new ArrayList<>();
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





                                            if(Joined == currentstudentid) {
                                                Toast.makeText(mContext, "You have already joined", Toast.LENGTH_SHORT).show();
                                                //this currently doesnt work
                                            } else if (mark == 1) {
                                                // mJoinRef.setValue(currentstudentid);
                                               // Log.v("joining successful", "worked");
                                                //mJoinRef= FirebaseDatabase.getInstance().getReference().child("Session").child(infoCollect.first).child(masterID).child("session_students");
                                                //mJoinRef.child(currentstudentid).setValue(currentStudentname);
                                                mark = 0;
                                                Intent intent = new Intent(mContext, CodeAuthActivity.class);
                                                intent.putExtra("session_id", masterID);
                                                intent.putExtra("subject", infoCollect.first);
                                                intent.putExtra("student_id", currentstudentid);
                                                intent.putExtra("student_name", currentStudentname);

                                                mContext.startActivity(intent);
                                            }


                                        } else {
                                            Toast.makeText(mContext, "No sessions available for this subject", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });




                                break;

                        }
                        return true;
                    }
                });

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                SimpleDateFormat mf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String formattedDate = df.format(c);
                String formattedtime = mf.format(c);
                //Log.v("date", String.valueOf(formattedDate));
                //Log.v("time", String.valueOf(formattedtime));
                for( Session session: session_list){
                    dateList.add( new Pair<String, String>(session.getSession_id(), session.getSession_date()));

                    int dateCount = 0;
                    for( Pair pair: dateList){
                        String dates = (String) pair.second;

                        if(dates.compareTo(formattedDate) == 0){
                            //Log.v("dates checker", String.valueOf(dates));
                            startTimeList.add( new Pair<String, String>(session.getSession_id(), session.getSession_start_time()));
                           // Log.v("id checkers", String.valueOf(session.getSession_id()));
                            endTimeList.add( new Pair<String, String>(session.getSession_id(), session.getSession_end_time()));
                            for( int k = 0; k < startTimeList.size(); k++) {

                                String startTime = (String) startTimeList.get(k).second;
                                String endTime = (String) endTimeList.get(k).second;
                                //Log.v("start time comp", String.valueOf(startTime.compareTo(formattedtime)));
                               // Log.v("end time comp", String.valueOf(endTime.compareTo(formattedtime)));
                                if((startTime.compareTo(formattedtime) < 0 && endTime.compareTo(formattedtime) > 0) || (startTime.compareTo(formattedtime) == 0)) {
                                    masterID = startTimeList.get(k).first;
                                    idcodeList.add(new Pair<>(masterID, infoCollect.first));
                                    //Log.v("master subject code", String.valueOf((idcodeList.get(0).second)));
                                  //  Log.v("master subject id", String.valueOf((idcodeList.get(0).first)));
                                    mark = 1;
                                   // Log.v("id of class", String.valueOf(masterID));
                                    break;



                                }

                            }

                        }
                    }




                }
                //mSubjectRef = FirebaseDatabase.getInstance().getReference().child("Session").child(infoCollect.first);
                //now set an onclick listener for the join button
               // Log.v("code", String.valueOf(infoCollect.first));
            }
        });
    }

    @Override
    public int getItemCount() {
      //  Log.v("count", String.valueOf(infoCollectList.size()));
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
