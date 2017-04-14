package pharmaproject.ahmed.example.packagecom.pharmaproject_employee;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import pharmaproject.ahmed.example.packagecom.pharmaproject_employee.database.Task;
import pharmaproject.ahmed.example.packagecom.pharmaproject_employee.helper.Utils;


public class AddCommentDialog extends DialogFragment  implements View.OnClickListener{
    RatingBar ratingdoctor;
    EditText commentText;
    int idOftask;
    String feedback="";
    Button sendfeedback;
    Task task = new Task();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_comment_dialog, null, false);
        //comment = (TextView)view.findViewById(R.id.sendfeedback);
        sendfeedback=(Button)view.findViewById(R.id.sendfeedback1) ;
        commentText= (EditText)view.findViewById(R.id.feedback);
        ratingdoctor=(RatingBar)view.findViewById(R.id.ratingdoctor);

        sendfeedback.setTypeface(Utils.getFont(getActivity()));
        commentText.setTypeface(Utils.getFont(getActivity()));
        sendfeedback.setOnClickListener(this);

        idOftask=getArguments().getInt("KEY");
        feedback=getArguments().getString("FEEDBACK");
        ratingdoctor.setProgress(getArguments().getInt("RATEABOUTDOCTOR"));
        return  view ;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!feedback.equals("No Feedback"))
        commentText.setText(feedback);
    }

    @Override
    public void onClick(View view) {
        dismiss();
        if(!commentText.getText().toString().isEmpty())
        task.sendFeedback(idOftask,commentText.getText().toString(),ratingdoctor.getProgress());
    }


}
