package comhummeltronentity_task.httpsgithub.entitytask.activity_classes.taskactivity_support;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import comhummeltronentity_task.httpsgithub.entitytask.R;

/**
 * Created by Meerlu on 23.05.2018.
 *
 *
 * ***************************************************************************************
 * ***************************************************************************************
 * ***************************************************************************************
 *
 * momentan nicht mehr benötigt, trotzdem einfach mal da lassen
 *
 * ***************************************************************************************
 * ***************************************************************************************
 * ***************************************************************************************
 *
 * das fragment das in der TaskActivity angezeigt wird, wenn noch keine tasks created wurden
 * ist so wie es heist: placeholder und fragment
 */


public class PlaceholderFragment extends Fragment {

    private static final String TAG = "PlaceholderFragment";
    private TextView title;
    private  TextView description;
    private  TextView time;
    private  TextView dates;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        title = view.findViewById(R.id.txtTitle);
        description = view.findViewById(R.id.txtDescription);
        time = view.findViewById(R.id.txtState);
        dates = view.findViewById(R.id.txtDates);

        title.setText("Placeholder");
        description.setText("fragment");
        return view;
    }


}
