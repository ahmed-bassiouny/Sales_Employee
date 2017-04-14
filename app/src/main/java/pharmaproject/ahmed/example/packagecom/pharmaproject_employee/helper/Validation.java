package pharmaproject.ahmed.example.packagecom.pharmaproject_employee.helper;

import android.content.Context;

/**
 * Created by ahmed on 20/03/17.
 */

public class Validation {

    static String phonePattern = "[0]{1}+[1]{1}+[0-9]{9}";

    public static  boolean CheakDataSignup(String email,String phone,String name ,String password, String confirmpassword , Context context)
    {
        boolean validate= false ;
        if (email.isEmpty()|| phone.isEmpty() || password.isEmpty()||name.isEmpty()) {
            Debuger.Toast(context,"please fill All data");
        }else if (phone.startsWith("01")&& phone.length()==11)
        {
            if (password.equals(confirmpassword))
            {
                  validate = true;
            }
            else
            {
                Debuger.Toast(context,"Password mismatch  ");
            }


        }
        else {
            Debuger.Toast(context,"please  insert phone number");
        }



        return validate ;
    }
    public static  boolean CheakDataSignin(String phone,String password,Context context)
    {
        boolean validate= false ;
        if (phone.isEmpty() || password.isEmpty()) {
            Debuger.Toast(context,"please fill All data");
        }
        else {
            validate=true;
        }

        return validate ;
    }
}
