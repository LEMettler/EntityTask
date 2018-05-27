package comhummeltronentity_task.httpsgithub.entitytask.activity_classes;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;

import java.time.LocalDate;
import java.util.ArrayList;

import comhummeltronentity_task.httpsgithub.entitytask.R;
import comhummeltronentity_task.httpsgithub.entitytask.activity_classes.calendaractivity_support.ViewPagerAdapter;
import comhummeltronentity_task.httpsgithub.entitytask.task_classes.Task;
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
    private LocalDate selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        taskStorage = getIntent().getExtras().getParcelable("TASKSTORAGE");

        viewPager = findViewById(R.id.viewPager);                                                   //initialisiert den taskslider(viewpager)

        calendar = findViewById(R.id.calendarView);
        selectedDate = null;                                                                        //Listener für ausgewählte tage
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String date;
                date = Integer.toString(i) + "-";
                i1++;
                if (i1 < 10) {
                    date = date + "0";              //das wunder von struktogram
                }
                date = date + i1 + "-";
                if (i2 < 10) {
                    date = date + "0";
                }
                date = date + i2;

                selectedDate = LocalDate.parse(date);

                refreshViewPager();
            }
        });
    }

    //erstellen/erneuern  des staskslider(viewpager) auf basis des ausgewählten datumani
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void refreshViewPager() {

        ArrayList<Task> selectedTasks = new ArrayList<>();              //TODO tasks, spezifisch für ihren type anzeigen

        for (Task t : taskStorage.getTasks()) {
            if (t.TYPE == "MONTHLY") {
                for (LocalDate d : t.getDates()) {
                    if (d.getDayOfMonth() == selectedDate.getDayOfMonth()) { //checkt für jeden monthlytask jedes datum ob der tag passt
                        selectedTasks.add(t);
                        break;
                    }
                }

            } else if (t.TYPE == "CUSTOM") {
                if (t.getDates().contains(selectedDate)) {
                    selectedTasks.add(t);
                }
            } else {
                if (t.getDates().contains(selectedDate)) {
                    selectedTasks.add(t);
                    //weekly                                    //todo anzeige von weekly anhand von days
                }

            }

            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, selectedTasks);
            viewPager.setAdapter(viewPagerAdapter);
        }
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

