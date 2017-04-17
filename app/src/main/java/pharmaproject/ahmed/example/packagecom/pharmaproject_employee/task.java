package pharmaproject.ahmed.example.packagecom.pharmaproject_employee;

import cn.pedant.SweetAlert.SweetAlertDialog;
import pharmaproject.ahmed.example.packagecom.pharmaproject_employee.database.Information;
import pharmaproject.ahmed.example.packagecom.pharmaproject_employee.database.Task;
import pharmaproject.ahmed.example.packagecom.pharmaproject_employee.database.TaskType;
import pharmaproject.ahmed.example.packagecom.pharmaproject_employee.helper.Utils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;


public class task extends Fragment implements OnMapReadyCallback {
    EditText doctorName;
    EditText address;
    EditText taskTime;
    EditText taskDescription;
    TextView typeTask;
    SeekBar seekbar;
    MapView mapview;
    ImageView feedback,prepare_endtask,canceltask;
    int id;
    Task taskEmployee=new Task();
    //map part

    MapView mapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_task, container, false);
        doctorName= (EditText) view.findViewById(R.id.Doc_name);
        address= (EditText) view.findViewById(R.id.Address);
        taskTime= (EditText) view.findViewById(R.id.Task_time);
        taskDescription=(EditText) view.findViewById(R.id.Task_Desc);
        typeTask= (TextView) view.findViewById(R.id.typetasktxt);
        seekbar= (SeekBar) view.findViewById(R.id.typetaskbar);
        mapview= (MapView) view.findViewById(R.id.scrollInfo);
        feedback= (ImageView) view.findViewById(R.id.feedback);
        prepare_endtask= (ImageView) view.findViewById(R.id.prepare_endtask);
        canceltask = (ImageView) view.findViewById(R.id.cancel_task);

        doctorName.setTypeface(pharmaproject.ahmed.example.packagecom.pharmaproject_employee.helper.Utils.getFont(getActivity()));
        address.setTypeface(pharmaproject.ahmed.example.packagecom.pharmaproject_employee.helper.Utils.getFont(getActivity()));
        taskTime.setTypeface(pharmaproject.ahmed.example.packagecom.pharmaproject_employee.helper.Utils.getFont(getActivity()));
        taskDescription.setTypeface(pharmaproject.ahmed.example.packagecom.pharmaproject_employee.helper.Utils.getFont(getActivity()));
        typeTask.setTypeface(pharmaproject.ahmed.example.packagecom.pharmaproject_employee.helper.Utils.getFont(getActivity()));

        id=getArguments().getInt("KEY");

        //map part

        mapView = (MapView) view.findViewById(R.id.scrollInfo);
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        seekbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        prepare_endtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(typeTask.getText().toString().equals("INCOMPLETE")){
                    taskEmployee.updateStatus(TaskType.On_The_Way,id,getActivity());
                    prepare_endtask.setImageResource(R.drawable.ph_processing);
                }else if(typeTask.getText().toString().equals("On The Way")){
                    taskEmployee.updateStatus(TaskType.PROCESSING,id,getActivity());
                    prepare_endtask.setImageResource(R.drawable.ph_endtask);
                }else if(typeTask.getText().toString().equals("PROCESSING")){
                    taskEmployee.updateStatus(TaskType.COMPLETE,id,getActivity());
                    prepare_endtask.setVisibility(View.INVISIBLE);
                }
            }
        });
        canceltask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createdialog();

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        taskEmployee.getTask
                (Utils.EmailAdress.replace(".","*"),id,doctorName,address,taskTime,taskDescription,seekbar,
                        typeTask,googleMap,getActivity(),feedback,prepare_endtask, canceltask);
        mapView.onResume();
    }

    public void createdialog() {


        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("You are going to cancel task!")
                .setCancelText("No !")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {


                        taskEmployee.updateStatus(TaskType.CANCEL,id,getActivity());
                        canceltask.setVisibility(View.GONE);
                        sweetAlertDialog.dismiss();


                    }
                })
                .show();

    }

}