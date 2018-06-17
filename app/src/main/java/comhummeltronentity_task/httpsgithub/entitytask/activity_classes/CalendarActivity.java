package comhummeltronentity_task.httpsgithub.entitytask.activity_classes;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;

import comhummeltronentity_task.httpsgithub.entitytask.R;
import comhummeltronentity_task.httpsgithub.entitytask.TaskStorage;
import comhummeltronentity_task.httpsgithub.entitytask.activity_classes.calendaractivity_support.ViewPagerAdapter;
import comhummeltronentity_task.httpsgithub.entitytask.task_classes.Task;
import comhummeltronentity_task.httpsgithub.entitytask.task_classes.TaskCustom;
import comhummeltronentity_task.httpsgithub.entitytask.task_classes.TaskMonthly;
import comhummeltronentity_task.httpsgithub.entitytask.task_classes.TaskWeekly;

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

        viewPager = findViewById(R.id.viewPager);   //initialisiert den taskslider

        calendar = findViewById(R.id.calendarView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            selectedDate = LocalDate.now();                                                                        //Listener für ausgewählte tage
        }
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            refreshViewPager();
        }
    }

    //vom viewpageadapter gecallt wenn ein view geklickt wird
    public void gotoTaskActivity(Task task){
        int itemIndex = taskStorage.getTasks().indexOf(task);

        Intent intent = new Intent(this, TasksActivity.class);
        intent.putExtra("TASKSTORAGE", taskStorage);    //Übergeben des Storage über Intent
        intent.putExtra("ITEMINDEX", itemIndex);    //übergeben des index des longclicked-task, der angezeigt werden soll
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            //if (resultCode == RESULT_OK)
            taskStorage = data.getExtras().getParcelable("TASKSTORAGE");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                refreshViewPager();
            }
            viewPager.setCurrentItem(data.getExtras().getInt("ITEMINDEX"));
        }
    }

    //erstellen/erneuern  des staskslider(viewpager) auf basis des ausgewählten datumani
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void refreshViewPager() {

        ArrayList<Task> selectedTasks = new ArrayList<>();              //DONE tasks, spezifisch für ihren type anzeigen

        for (Task t : taskStorage.getTasks()) {

            int i = taskStorage.getTasks().indexOf(t);
            //if (taskStorage.getOneTaskState(i)) {

                if (t instanceof TaskMonthly) {

                    for (LocalDate d : t.getDates()) {
                        if (d.getDayOfMonth() == selectedDate.getDayOfMonth()) { //checkt für jeden monthlytask jedes datum ob der tag passt
                            selectedTasks.add(t);
                            break;
                        }
                    }
                } else if (t instanceof TaskCustom) {

                    if (t.getDates().contains(selectedDate)) {
                        selectedTasks.add(t);
                    }
                } else if (t instanceof TaskWeekly) {

                    int daysIndex = selectedDate.getDayOfWeek().ordinal();
                    boolean days[] = ((TaskWeekly) t).getDays();

                    if (days[daysIndex]) {
                        System.out.println("ADDDAY");
                        selectedTasks.add(t);
                    }
                    //done anzeige von weekly anhand von days
                }

                    //TODO erledigt anzeigen

            //}
        }
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, selectedTasks);
        viewPager.setAdapter(viewPagerAdapter);
    }

    //der viewpager ruft diese methode auf um einen task zu erledigen
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setTaskDone(ArrayList<Task> selectedTasks, int position){
        Task t = selectedTasks.get(position);
        int i = taskStorage.getTasks().indexOf(t);

        taskStorage.setOneTaskState(i, true);

        //toasts sind kleine Einbeldungen, die kurzes Feedback geben
        Context context = getApplicationContext();
        CharSequence text = t.getTitle() + " is Done!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        refreshViewPager();

        taskStorage.profile.increasePoints(20);
        taskStorage.saveTasksToFile(this);
        taskStorage.saveProfileToFile(this);
    }

    @Override
    public void finish() {
        //rückgabe des taskstorage an mainactivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("TASKSTORAGE", taskStorage);
        setResult(1, resultIntent);

        super.finish();
    }

    @Override
    protected void onStop() {
        taskStorage.saveProfileToFile(this);
        super.onStop();
    }
}

