package comhummeltronentity_task.httpsgithub.entitytask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


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
        //startActivity(intent);                          //todo muss auch wie in taskactivity for result, damit wenn wir wieder reinundraus gehen, dann
        startActivityForResult(intent, 1);
    }                                                   //die tasks immernoch da sind

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            //if (resultCode == RESULT_OK)
            taskStorage = data.getExtras().getParcelable("TASKSTORAGE");

        }
    }

    //Link zu CalendarActivity
    public void gotoCalendar(View view){
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);
    }

}
