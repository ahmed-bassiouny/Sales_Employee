package pharmaproject.ahmed.example.packagecom.pharmaproject_employee.database;

import android.app.ProgressDialog;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import pharmaproject.ahmed.example.packagecom.pharmaproject_employee.helper.Adapter_Employees;
import pharmaproject.ahmed.example.packagecom.pharmaproject_employee.helper.MyHelper;
import java.util.ArrayList;

/**
 * Created by ahmed on 19/03/17.
 */

public class Employee {

    public String phone;
    public String name;
    public String password;
    public String lastLocation="";
    public String email;
    public int rate;
    public boolean online=false;
    public String CountOfTasks="0";
    private MyHelper helper;

}
