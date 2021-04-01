package ses.attendance_system_student;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HelperAdaptor extends RecyclerView.Adapter {

    List<String> infoCollectList;

    public HelperAdaptor(List<String> infoCollectList) {
        this.infoCollectList = infoCollectList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_layout,parent, false);
        ViewHolderClass viewHolderClass = new ViewHolderClass(view);
        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderClass viewHolderClass=(ViewHolderClass)holder;
        String infoCollect=infoCollectList.get(position);
        viewHolderClass.sub1.setText(infoCollect);
       // viewHolderClass.sub2.setText(infoCollect);
        Log.v("test4", infoCollect);
    }

    @Override
    public int getItemCount() {
        Log.v("count", String.valueOf(infoCollectList.size()));
        return infoCollectList.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder{
        TextView sub1, sub2;
        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            sub1=itemView.findViewById(R.id.sub1);
           // sub2=itemView.findViewById(R.id.sub2);
        }
    }

}
