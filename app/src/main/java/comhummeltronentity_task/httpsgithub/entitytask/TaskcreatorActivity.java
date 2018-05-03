package comhummeltronentity_task.httpsgithub.entitytask;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

/**
* Hier werden die einzelnen Tasks erzeugt
 * TODO tasks löschen/back
 * TODO input-pretexte löschen bei touch
*
* */


public class TaskcreatorActivity extends AppCompatActivity {

    private ArrayList<Task> tasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskcreator);
    }

    //Erzeugung der Taskobjekte
    public void addTask(View v){
        EditText inputTitle = findViewById(R.id.inputTitle);
        EditText inputDescription = findViewById(R.id.inputDescription);

        String title = inputTitle.getText().toString();
        String description = inputDescription.getText().toString();

        Task e = new Task(title,description);
        tasks.add(e);
        System.out.println("xx");
    }
}
