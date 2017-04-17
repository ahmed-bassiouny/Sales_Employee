package pharmaproject.ahmed.example.packagecom.pharmaproject_employee.database;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import pharmaproject.ahmed.example.packagecom.pharmaproject_employee.AddCommentDialog;
import pharmaproject.ahmed.example.packagecom.pharmaproject_employee.R;
import pharmaproject.ahmed.example.packagecom.pharmaproject_employee.helper.Adapter_Tasks;
import pharmaproject.ahmed.example.packagecom.pharmaproject_employee.helper.Debuger;
import pharmaproject.ahmed.example.packagecom.pharmaproject_employee.helper.LatLong;
import pharmaproject.ahmed.example.packagecom.pharmaproject_employee.helper.Utils;
import pharmaproject.ahmed.example.packagecom.pharmaproject_employee.helper.MyHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by ahmed on 19/03/17.
 */

public class Task {
    public int id;
    public String doctorName; //
    public String description;//
    // information about doctor and task
    public String locationDoctor; //
    public String time_task;
    // information about employee (preparing task)
    public String locationPreparing = "";
    public String time_prepareTask = "";
    // information about employee (end task)
    public String locationEmployee = "";
    public String time_endTask = "";
    public TaskType taskType;
    public String feedback = "No Feedback";
    public int timeTrack;
    public int rateforEmployee;
    public int rateforDoctor;

    private int numberOfTasks;
    private MyHelper helper;
    private LocationManager locationManager;



    public void updateStatus(final TaskType taskType, final int id_task, final FragmentActivity fragmentActivity) {
        if (taskType==TaskType.CANCEL) {
            getRoot().
                    child(id_task + "").child("taskType").setValue(taskType);
        }
        locationManager = (LocationManager) fragmentActivity.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                getRoot().
                        child(id_task + "").child("taskType").setValue(taskType);
                switch (taskType) {
                    case On_The_Way:
                        getRoot().
                                child(id_task+"").child("locationPreparing").setValue("lat/lng: (" + location.getLatitude() + "," + location.getLongitude() + ")");
                        getRoot().
                                child(id_task+"").child("time_prepareTask").setValue(MyHelper.dateformate.format(new Date()));
                        break;
                    case COMPLETE:
                        getRoot().
                                child(id_task+"").child("locationEmployee").setValue("lat/lng: (" + location.getLatitude() + "," + location.getLongitude() + ")");
                        getRoot().
                                child(id_task+"").child("time_endTask").setValue(MyHelper.dateformate.format(new Date()));
                        break;
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                fragmentActivity.startActivity(intent);
            }
        };
        if (ActivityCompat.checkSelfPermission(fragmentActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(fragmentActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener,null);
    }
    public void sendFeedback(int id_task,String feedback,int rateofdoctor){
        getRoot().child(id_task+"").child("feedback").setValue(feedback);
        getRoot().child(id_task+"").child("rateforDoctor").setValue(rateofdoctor);
    }

    public void getTasks(final RecyclerView recyclerView, final FragmentActivity fragmentActivity, final String email, final ProgressDialog progressDialog, @Nullable final TaskType fiteroftasks,final TextView noTeskfound){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<Task> tasks = new ArrayList<>();
                for(DataSnapshot dss:dataSnapshot.getChildren()){
                    if(dss.hasChildren()) {
                        Task task = dss.getValue(Task.class);

                        if(fiteroftasks==TaskType.All) {

                            tasks.add(task);
                        }else if(fiteroftasks==task.taskType){

                            tasks.add(task);
                        }


                    }
                }
                if(tasks.size()>0){

                        noTeskfound.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    Adapter_Tasks adapter_tasks = new Adapter_Tasks(tasks,fragmentActivity,email);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter_tasks);
                }else{
                    Debuger.Toast(fragmentActivity,"No Tasks Found");
                    //noTeskfound.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("mytag", databaseError.getMessage());
               // Debuger.Toast(fragmentActivity,databaseError.getMessage());
                progressDialog.dismiss();
            }
        };
        Information.getDatabase().child("Supervisor").child(Utils.parentName).child(email).orderByChild("time_task").addValueEventListener(postListener);
    }

    public void getTask(final String email, final int task_id, final EditText Doc_name,
                        final EditText Address, final EditText Task_time,
                        final EditText Task_Desc,
                        final SeekBar typetaskbar, final TextView typetasktxt,
                        final GoogleMap mapView, final FragmentActivity fragmentActivity,
                        final ImageView feedbackimg,final ImageView prepare_endtask,final ImageView canceltask){
        helper=new MyHelper(fragmentActivity);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Task task = dataSnapshot.child("Supervisor").child(Utils.parentName).child(email).child(task_id+"").getValue(Task.class);
                Doc_name.setText(task.doctorName);
                if(!task.locationDoctor.isEmpty())
                Address.setText(helper.getFullAddress(task.locationDoctor));
                Task_time.setText(task.time_task);
                Task_Desc.setText(task.description);
                switch (task.taskType){
                    case INCOMPLETE:
                        typetaskbar.setProgress(0);
                        typetasktxt.setText("INCOMPLETE");
                        prepare_endtask.setImageResource(R.drawable.ph_preparing);
                        break;
                    case On_The_Way:
                        typetaskbar.setProgress(1);
                        typetasktxt.setText("On The Way");
                        prepare_endtask.setImageResource(R.drawable.ph_processing);
                        break;
                    case PROCESSING:
                        typetaskbar.setProgress(2);
                        typetasktxt.setText("PROCESSING");
                        prepare_endtask.setImageResource(R.drawable.ph_endtask);
                        break;
                    case COMPLETE:
                        typetaskbar.setProgress(3);
                        typetasktxt.setText("COMPLETE");
                        prepare_endtask.setVisibility(View.INVISIBLE);
                        canceltask.setVisibility(View.GONE);
                        break;
                    case CANCEL:
                        typetaskbar.setProgress(0);
                        typetasktxt.setText("CANCEL");
                        prepare_endtask.setVisibility(View.INVISIBLE);
                        canceltask.setVisibility(View.GONE);
                    default:
                }
                feedbackimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fragmentManager =fragmentActivity.getSupportFragmentManager();
                        AddCommentDialog addCommentDialog = new AddCommentDialog();
                        Bundle bundle = new Bundle();
                        bundle.putInt("KEY",task.id);
                        bundle.putString("FEEDBACK",task.feedback);
                        bundle.putInt("RATEABOUTDOCTOR",task.rateforDoctor);

                        addCommentDialog.setArguments(bundle);
                        addCommentDialog.show(fragmentManager,"TAG");
                    }
                });
                updatemap(mapView,task);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("mytag", databaseError.getMessage());
            }
        };
        Information.getDatabase().addValueEventListener(postListener);

    }
    // Hossam Code
    public  void  updatemap(GoogleMap googleMap,Task task)
    {
        if(!task.locationDoctor.isEmpty()) {
            MarkerOptions m = null;
            try {
                m = new MarkerOptions()
                        .position(new LatLng(LatLong.getLat(task.locationDoctor), LatLong.getLongt(task.locationDoctor)))
                        .title(Utils.datetimeformate.format(Utils.dateformate.parse(task.time_task))).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_doctor_img));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            googleMap.addMarker(m);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(m.getPosition(), 15), 500, null);
        }



    }
    private DatabaseReference getRoot(){
        return Information.getDatabase().child("Supervisor").child(Utils.parentName).child(Utils.EmailAdress.replace(".", "*"));
    }

//    public ArrayList<Task>SortTaskbyDate(ArrayList<Task>task)
//    {
//
//
//
//        for (int j= 0 ;j<(task.size()-1);j++) {
//            for (int i = 0; i < task.size() - 1; i++)
//
//            {
//
//                SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy hh:mm ");
//                try {
//                    if (sdf.parse(task.get(i).time_task.toString()).after(sdf.parse(task.get(i + 1).time_task.toString())))
//
//                    {
//                        Task temp = new Task();
//
//                        temp = task.get(i + 1);
//                        task.set(i + 1, task.get(i));
//                        task.set(i, temp);
//                    }
//
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//
//
//            }
//        }
//
//
//        return task ;
//    }

}
