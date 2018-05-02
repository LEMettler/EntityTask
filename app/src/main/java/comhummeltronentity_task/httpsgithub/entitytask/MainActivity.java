package comhummeltronentity_task.httpsgithub.entitytask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


/**
* In diesem Fenster startet die App
* */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Link zu TaskActivity
    public void gotoTasks(View view){
        Intent intent = new Intent(this, TasksActivity.class);
        startActivity(intent);
    }

    //Link zu CalendarActivity
    public void gotoCalendar(View view){
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);
    }

}
