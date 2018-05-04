package comhummeltronentity_task.httpsgithub.entitytask;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;

import java.util.ArrayList;

/**
* Hier werden die einzelnen Tasks erzeugt
 * TODO tasks löschen/back
 * TODO input-pretexte löschen bei touch
 *
 * vorallem:
 * TODO finde eine passende auswahlmöglichkeit für weekly
 * TODO datum/datums auslesen und in richtiger form an jeweilige subtasks übergeben
*
* */


public class TaskcreatorActivity extends AppCompatActivity {

    private ArrayList<Task> tasks = new ArrayList<>();

    //Die Radiobutton sind nicht nicht methodenintern, da sie sich gegenseitig reseten
    private RadioButton rbtnCustom;
    private RadioButton rbtnMonthly;
    private RadioButton rbtnWeekly;
    private Button btnTime;
    private Button btnDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskcreator);

        btnTime = findViewById(R.id.btnTime);
        btnDate = findViewById(R.id.btnDate);
        rbtnCustom = findViewById(R.id.rbtnCustom);
        rbtnMonthly = findViewById(R.id.rbtnMonthly);
        rbtnWeekly = findViewById(R.id.rbtnWeekly);

        Switch switchReminder = findViewById(R.id.switchReminder);
    }

    //Handler der Radiobutton -> Anzeige der Time/Date picker
    public void rbtnCustomHandler(View v){
        btnTime.setVisibility(View.VISIBLE);
        btnDate.setVisibility(View.VISIBLE);
    }

    public void rbtnMonthlyHandler(View v){
        btnTime.setVisibility(View.VISIBLE);
        btnDate.setVisibility(View.VISIBLE);
    }

    public void rbtnWeeklyHandler(View v){
        btnTime.setVisibility(View.GONE);
        btnDate.setVisibility(View.GONE);
    }



    //Erzeugung der Taskobjekte
    public void addTask(View v){
        EditText inputTitle = findViewById(R.id.inputTitle);
        EditText inputDescription = findViewById(R.id.inputDescription);

        String title = inputTitle.getText().toString();
        String description = inputDescription.getText().toString();

        //Task e = new Task(title,description);                             //TODO ersetzung durch spezifische tasks, je nach custom/month/week
       // tasks.add(e);
    }
}
