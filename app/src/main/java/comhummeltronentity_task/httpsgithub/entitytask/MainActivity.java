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
        startActivity(intent);
    }

    //Link zu CalendarActivity
    public void gotoCalendar(View view){
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);
    }

}
