package comhummeltronentity_task.httpsgithub.entitytask.activity_classes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;

import comhummeltronentity_task.httpsgithub.entitytask.R;
import comhummeltronentity_task.httpsgithub.entitytask.activity_classes.calendaractivity_support.ViewPagerAdapter;
import comhummeltronentity_task.httpsgithub.entitytask.task_classes.TaskStorage;

/**
 * Hier wird der Kalender von Google implementiert. Hail Corporate.
 *
 * wird ein tag angeklickt sollen die tasks für diesen tag unterhalb erscheinen, am besten hübsch
 */

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendar;
    private ViewPager viewPager;
    private TaskStorage taskStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        taskStorage = getIntent().getExtras().getParcelable("TASKSTORAGE");

        calendar = findViewById(R.id.calendarView);

        viewPager = findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, taskStorage);
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void finish() {
        //rückgabe des taskstorage an mainactivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("TASKSTORAGE", taskStorage);
        setResult(1, resultIntent);

        super.finish();
    }
}
