package pharmaproject.ahmed.example.packagecom.pharmaproject_employee;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.fabric.sdk.android.Fabric;
import pharmaproject.ahmed.example.packagecom.pharmaproject_employee.database.Information;
import pharmaproject.ahmed.example.packagecom.pharmaproject_employee.database.SortType;
import pharmaproject.ahmed.example.packagecom.pharmaproject_employee.helper.Utils;

public class MainActivity extends AppCompatActivity {
    Utils utils = new Utils(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        if(!isConnected()){
            startActivity(new Intent(MainActivity.this,isConnected.class));
            finish();
        }else{
            utils.goToFragment(new LoginEmployee(),null,null);
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.signout:
            {
                createdialog();
            }
            case R.id.sortbycomplete:
            {
                SortType sortType = SortType.complete;
                //utils.goToFragment(new ListOfTasks(),null,null);
                Toast.makeText(this, ""+sortType, Toast.LENGTH_SHORT).show();
            }

        }

//        if(item.getItemId()==R.id.signout){
//            createdialog();
//
//        }
        return super.onOptionsItemSelected(item);
    }


    public void createdialog() {


        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("You are going to exit!")
                .setCancelText("No,cancel plx!")
                .setConfirmText("signout!")
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
                        logout();
                        utils.goToFragment(new LoginEmployee(),null,null);
                        settrackingbuttonStatus(false);

                        // Write a message to the database
                        Information.getDatabase().child(Utils.parentName).child(Utils.EmailAdress.replace(".","*")).child("online").setValue(false);


                        sweetAlertDialog.dismiss();


                    }
                })
                .show();

    }


    public void logout()
    {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email", "");
        editor.putString("parent", "");
        editor.commit();
    }

    public void settrackingbuttonStatus(boolean b)
    {

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("trackingbutton", b);
        editor.commit();
    }
    private boolean isConnected(){
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;

    }

}
