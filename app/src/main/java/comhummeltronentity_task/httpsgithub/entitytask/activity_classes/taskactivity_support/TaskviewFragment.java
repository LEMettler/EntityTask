package comhummeltronentity_task.httpsgithub.entitytask.activity_classes.taskactivity_support;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.time.LocalDate;

import comhummeltronentity_task.httpsgithub.entitytask.R;
import comhummeltronentity_task.httpsgithub.entitytask.task_classes.Task;
import comhummeltronentity_task.httpsgithub.entitytask.task_classes.TaskCustom;
import comhummeltronentity_task.httpsgithub.entitytask.task_classes.TaskMonthly;

/**
 * Created by Meerlu on 23.05.2018.
 *
 * das fragment das einen task anzeigt
 *
 * todo anzeigen aller daten in initialize()
 */

public class TaskviewFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "TaskviewFragment";
    private  TextView title;
    private  TextView description;
    private  TextView time;
    private  TextView dates;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tasks,container,false);
        title = view.findViewById(R.id.txtTitle);
        description = view.findViewById(R.id.txtDescription);
        time = view.findViewById(R.id.txtTime);
        dates = view.findViewById(R.id.txtDates);


        //Task task = getArguments().getParcelable("TASK");
        Task task = (Task) getArguments().get("TASK");
        initialize(task);

        return view;
    }

    public void initialize(Task task){
        if (task instanceof TaskCustom) {
            dates.setText("");
            for (LocalDate d : task.getDates()){
                dates.setText(dates.getText()  + d.toString() + ", ");
            }                                                            //todo, tage für weekly und komma richtig setzen
        } else if (task instanceof TaskMonthly) {
            dates.setText("");
            for (LocalDate d : task.getDates()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    dates.setText(dates.getText() + String.valueOf(d.getDayOfMonth()) + ", ");
                }
            }
        } else {
            //show days
        }

        title.setText(task.getTitle());
        description.setText(task.getDescription());
        time.setText(task.getTime().toString());
    }
}


