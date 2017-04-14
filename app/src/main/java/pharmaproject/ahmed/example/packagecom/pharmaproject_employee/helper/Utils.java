package pharmaproject.ahmed.example.packagecom.pharmaproject_employee.helper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.text.SimpleDateFormat;

import pharmaproject.ahmed.example.packagecom.pharmaproject_employee.R;

/**
 * Created by ahmed on 27/03/17.
 */

public class Utils {
    FragmentActivity fragmentActivity;
    public static String EmailAdress;
    public static int GREEN = Color.parseColor("#1AB110");
    public static int YELLOW =Color.parseColor("#E6D30A");
    public static SimpleDateFormat dateformate = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
    public static SimpleDateFormat datetimeformate = new SimpleDateFormat("hh:mm a");
    public static String parentName = "0";
    public Utils(FragmentActivity fragmentActivity){
        this.fragmentActivity=fragmentActivity;
    }
    public void goToFragment(Fragment fragment, @Nullable String tag, @Nullable Bundle bundle){

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_main,fragment);
        if (bundle !=null) {
            //Bundle args = new Bundle();
            //args.putString("KEY", value);
            fragment.setArguments(bundle);
        }
        if(tag !=null)
            fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }
    public static Typeface myTypeFace;

    public static Typeface getFont(Context ctx) {
        if (myTypeFace == null) {
            myTypeFace = Typeface.createFromAsset(ctx.getAssets(), "CaviarDreams.ttf");
        }
        return myTypeFace;
    }
}
