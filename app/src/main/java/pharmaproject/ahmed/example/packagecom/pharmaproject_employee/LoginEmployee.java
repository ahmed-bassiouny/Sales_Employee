package pharmaproject.ahmed.example.packagecom.pharmaproject_employee;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;
import pharmaproject.ahmed.example.packagecom.pharmaproject_employee.database.Information;
import pharmaproject.ahmed.example.packagecom.pharmaproject_employee.helper.Utils;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;


public class LoginEmployee extends Fragment implements View.OnClickListener {

    EditText email, password;
    Button signin, ip;
    Utils utils;

//hello new project

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_employee, container, false);
        utils = new Utils(getActivity());

        email = (EditText) view.findViewById(R.id.email);
        //password = (EditText) view.findViewById(R.id.password);
        signin = (Button) view.findViewById(R.id.signin);
        ip= (Button) view.findViewById(R.id.ip);

        email.setTypeface(Utils.getFont(getActivity()));
      //  password.setTypeface(Utils.getFont(getActivity()));
        signin.setTypeface(Utils.getFont(getActivity()));
        ip.setTypeface(Utils.getFont(getActivity()));

        signin.setOnClickListener(this);
        if(!isLoged()[0].isEmpty()) {
            utils.goToFragment(new ListOfTasks(), null, null);
            Utils.EmailAdress=isLoged()[0];
            Utils.parentName=isLoged()[1];
        }

        ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText(getIPAndroid())
                        .setConfirmText("Ok")

                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();

                            }
                        })
                        .show();
            }
        });

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }


    @Override
    public void onClick(View view) {

        signinfakeaccount();

    }


    public void signinfakeaccount() {

        final String emailDa = email.getText().toString();

      //  final String passwordDa = password.getText().toString().trim();

        if (emailDa.isEmpty() /*|| passwordDa.isEmpty()*/) {
            Toast.makeText(getActivity(), "please enter valid mail", Toast.LENGTH_SHORT).show();
        } else {
            final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Please Wait", "", true, false);
            Information.getDatabase().addListenerForSingleValueEvent((new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot childDataSnapshot : dataSnapshot.child("Supervisor").getChildren()) {

                        String parent = childDataSnapshot.getKey().toString();

                        if (/*childDataSnapshot.hasChild(emailDa.replace(".", "*"))*/childDataSnapshot.hasChild(getIPAndroid())) {
                            String parentname = parent;
                            if (/*dataSnapshot.child("Supervisor").child(parentname).child(emailDa.replace(".", "*")).child("password").getValue().toString().equals(passwordDa)&&*/
                                  /*  dataSnapshot.child("Supervisor").child(parentname).child(emailDa.replace(".", "*")).child("IMEI").getValue().equals(getIPAndroid())*/
                                    dataSnapshot.child("Supervisor").child(parentname).child(getIPAndroid()).child("email").getValue().equals(emailDa)  )
                            {
                                Utils.parentName = parentname;
                                Utils.EmailAdress =getIPAndroid() /*emailDa*/;

                                utils.goToFragment(new ListOfTasks(), null, null);
                                loged(Utils.EmailAdress,Utils.parentName);
                                progressDialog.dismiss();
                            } else {
                                Toast.makeText(getActivity(), "invalid input", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getActivity(), "incorrent password", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }));

        }
    }

    public void loged(String email,String Parent)
    {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email", email);
        editor.putString("parent", Parent);
        editor.commit();
    }

    public String[] isLoged()
    {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String[] arr = {sharedPref.getString("email",""),sharedPref.getString("parent","")};
        return  arr;
    }
    private String getIPAndroid(){
        return Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }
}
