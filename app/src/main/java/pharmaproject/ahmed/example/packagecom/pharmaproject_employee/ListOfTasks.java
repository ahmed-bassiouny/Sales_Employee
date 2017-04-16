package pharmaproject.ahmed.example.packagecom.pharmaproject_employee;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kyleduo.switchbutton.SwitchButton;

import java.lang.reflect.Array;

import pharmaproject.ahmed.example.packagecom.pharmaproject_employee.database.Information;
import pharmaproject.ahmed.example.packagecom.pharmaproject_employee.database.SortType;
import pharmaproject.ahmed.example.packagecom.pharmaproject_employee.database.Task;
import pharmaproject.ahmed.example.packagecom.pharmaproject_employee.helper.Utils;
import pharmaproject.ahmed.example.packagecom.pharmaproject_employee.helper.MyHelper;

/**
 * Created by shemoo on 3/28/2017.
 */

public class ListOfTasks extends Fragment implements AdapterView.OnItemSelectedListener{

    RecyclerView recyclerView;
    String Email;
    SwitchButton startStopService;
    Spinner filter ;
    TextView noTaskfound;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_employee_task, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        startStopService= (SwitchButton) view.findViewById(R.id.startStopservice);
       filter =(Spinner) view.findViewById(R.id.spinner);
        noTaskfound= (TextView) view.findViewById(R.id.notaskfound);

     //   filter.setAdapter();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
       R.array.sorttype  , android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        filter.setAdapter(adapter);
        filter.setOnItemSelectedListener(this);



        if( ! (runTimePermation()))
        {
            enableButton();

            if( getTarckingButtonStatus())
            {
                //startStopService.performClick();
                startStopService.setChecked(true);
            }

        }
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();



        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       // Email=(FirebaseAuth.getInstance().getCurrentUser().getEmail()).replace(".","*");
        Email=Utils.EmailAdress.replace(".","*");
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        // get tasks for recycler view
        new Task().getTasks(recyclerView,getActivity(),Email, MyHelper.getProgress(getActivity()),null,noTaskfound);

    }
    public boolean runTimePermation()
    {
        if (Build.VERSION.SDK_INT>=23 && ContextCompat.checkSelfPermission(getActivity(), android.Manifest.
                permission.ACCESS_FINE_LOCATION)!= PackageManager.
                PERMISSION_GRANTED&&ContextCompat.
                checkSelfPermission(getActivity(), android.Manifest.permission.
                        ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)

        {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION},100);
            return true;

        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==100)
        {
            if (grantResults[0]== PackageManager.PERMISSION_GRANTED &&grantResults[1]==
                    PackageManager.PERMISSION_GRANTED)
            {
                enableButton();
            }
            else
            {
                runTimePermation();
            }
        }
    }

    public void enableButton()
    {


        startStopService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {

                    FirebaseDatabase database ;
                    DatabaseReference myRef ;
                    database = FirebaseDatabase.getInstance();
                    myRef = database.getReference("PharmaProject");

                    myRef.child("Supervisor").child(Utils.parentName).child(Utils.EmailAdress.replace(".","*")).child("online")
                            .setValue(true);
                    //set tracking status on to overcome problem of closing app ;
                    settrackingbuttonStatus( true);
                    //*****************************************
                    startService();
                }
                else
                {
                    //set tracking status off to overcome problem of closing app ;
                    settrackingbuttonStatus( false);
                    //*****************************************
                    Intent i = new Intent(getActivity(),SendLatLong.class);
                    getActivity().stopService(i);

                    if (!Utils.parentName.equals("0"))
                    {
                        Log.e("****","iam off"+Utils.parentName +" "+Utils.EmailAdress);

                        FirebaseDatabase database ;
                        DatabaseReference myRef ;
                        database = FirebaseDatabase.getInstance();
                        myRef = database.getReference("PharmaProject");
                        myRef.child("Supervisor").child(Utils.parentName).child(Utils.EmailAdress.replace(".","*")).child("online")
                                .setValue(false);

                    }

                }
            }
        });

    }




    public void settrackingbuttonStatus(boolean b)
    {

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("trackingbutton", b);
        editor.commit();
    }

    public boolean getTarckingButtonStatus()
    {

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        return  sharedPref.getBoolean("trackingbutton",false);

    }

    private void startService(){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               int time= dataSnapshot.child("Supervisor").child(Utils.parentName).child(Utils.EmailAdress.replace(".","*")).child("timeTrack").getValue(Integer.class);
                Intent i = new Intent(getActivity(),SendLatLong.class);
                //i.set
                i.putExtra("EmailAdress",Utils.EmailAdress);
                i.putExtra("parentName",Utils.parentName);
                i.putExtra("time",time);
                getActivity().startService(i);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        Information.getDatabase().addListenerForSingleValueEvent(postListener);
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
        //adapterView.getItemAtPosition(i);
        Task task = new Task();
        switch (pos)
        {
            case 0:

            {
                task.getTasks(recyclerView,getActivity(),Email, MyHelper.getProgress(getActivity()),null,noTaskfound);
                break;
            }
            case 1:

            {
         task.getTasks(recyclerView,getActivity(),Email, MyHelper.getProgress(getActivity()),SortType.complete,noTaskfound);
                break;
            }
            case 2:
            {
                task.getTasks(recyclerView,getActivity(),Email, MyHelper.getProgress(getActivity()),SortType.incomplete,noTaskfound);
                break;
            }
            case 3:
            {

                task.getTasks(recyclerView,getActivity(),Email, MyHelper.getProgress(getActivity()),SortType.processing,noTaskfound);
                break;
            }
            case 4:
            {

                task.getTasks(recyclerView,getActivity(),Email, MyHelper.getProgress(getActivity()),SortType.on_the_way,noTaskfound);
                break;
            }


        }



    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
