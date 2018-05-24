package comhummeltronentity_task.httpsgithub.entitytask.activity_classes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import comhummeltronentity_task.httpsgithub.entitytask.R;
import comhummeltronentity_task.httpsgithub.entitytask.task_classes.TaskStorage;


/**
* In diesem Fenster startet die App
 * und man hat die auswahl nach der designvorlage zu kalender oder tasks zu gehen
* */

public class MainActivity extends AppCompatActivity {
    
    private TaskStorage taskStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Erstellung des Taskspeicherobjekts
        taskStorage = new TaskStorage();
    }

    //Link zu TaskActivity
    public void gotoTasks(View view){
        Intent intent = new Intent(this, TasksActivity.class);
        intent.putExtra("TASKSTORAGE", taskStorage);    //Übergeben des Storage über Intent
        startActivityForResult(intent, 1);
    }

    //Link zu CalendarActivity
    public void gotoCalendar(View view){
        Intent intent = new Intent(this, CalendarActivity.class);
        intent.putExtra("TASKSTORAGE", taskStorage);    //Übergeben des Storage über Intent
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            //if (resultCode == RESULT_OK)
            taskStorage = data.getExtras().getParcelable("TASKSTORAGE");

        }
    }
}
