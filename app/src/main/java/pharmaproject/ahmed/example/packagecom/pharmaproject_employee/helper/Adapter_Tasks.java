package pharmaproject.ahmed.example.packagecom.pharmaproject_employee.helper;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import pharmaproject.ahmed.example.packagecom.pharmaproject_employee.R;

import java.util.ArrayList;

import pharmaproject.ahmed.example.packagecom.pharmaproject_employee.database.Task;
import pharmaproject.ahmed.example.packagecom.pharmaproject_employee.task;

/**
 * Created by shemoo on 3/10/2017.
 */

public class Adapter_Tasks extends RecyclerView.Adapter<Adapter_Tasks.CustomViewHolder> {

    private ArrayList <Task> tasks ;
    FragmentActivity fragmentActivity;
    String email;
    MyHelper helper;


    public Adapter_Tasks(ArrayList <Task> tasks, FragmentActivity fragmentActivity,String email){
        this.tasks=tasks;
        this.fragmentActivity=fragmentActivity;
        this.email=email;
        this.helper=new MyHelper(fragmentActivity);
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemtask, parent, false);
        return new CustomViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        Task task=tasks.get(position);
        holder.namedoctor.setText(task.doctorName);
        holder.date.setText(task.time_task);
        switch (task.taskType){
            case CANCEL:
                holder.tasktype.setText("CANCEL");
                holder.tasktype.setTextColor(Color.RED);
                break;
            case INCOMPLETE:
                holder.tasktype.setText("INCOMPLETE");
                holder.tasktype.setTextColor(Color.BLACK);
                break;
            case PROCESSING:
                holder.tasktype.setText("PROCESSING");
                holder.tasktype.setTextColor(Utils.YELLOW);
                break;
            case On_The_Way:
                holder.tasktype.setText("On The Way");
                holder.tasktype.setTextColor(Utils.YELLOW);
                break;
            case COMPLETE:
                holder.tasktype.setText("COMPLETE");
                holder.tasktype.setTextColor(Utils.GREEN);
                break;
        }
    }

    @Override
    public int getItemCount() {

        return tasks.size();
    }



    public class CustomViewHolder extends RecyclerView.ViewHolder {

       TextView namedoctor,tasktype,date;
        LinearLayout linearItem;
        public CustomViewHolder(final View itemView) {
            super(itemView);
            this.namedoctor =(TextView)itemView.findViewById(R.id.namedoctor);
            this.tasktype =(TextView)itemView.findViewById(R.id.tasktype);
            this.date=(TextView)itemView.findViewById(R.id.date);
            this.linearItem=(LinearLayout)itemView.findViewById(R.id.linearItem);

            namedoctor.setTypeface(Utils.getFont(fragmentActivity));
            tasktype.setTypeface(Utils.getFont(fragmentActivity));
            date.setTypeface(Utils.getFont(fragmentActivity));


            linearItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("KEY", tasks.get(getAdapterPosition()).id);
                    helper.goToFragment(new task(),"shemoo",bundle);
                   // bundle.putString("email",email);
                    //helper.goToFragment(new ShowTask(),"Back To Tasks",bundle);
                }
            });
        }

    }



}


