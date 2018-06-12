package comhummeltronentity_task.httpsgithub.entitytask.activity_classes;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;

import comhummeltronentity_task.httpsgithub.entitytask.R;
import comhummeltronentity_task.httpsgithub.entitytask.activity_classes.mainactivity_support.ViewPagerAdapter;
import comhummeltronentity_task.httpsgithub.entitytask.task_classes.Task;
import comhummeltronentity_task.httpsgithub.entitytask.task_classes.TaskCustom;
import comhummeltronentity_task.httpsgithub.entitytask.task_classes.TaskMonthly;
import comhummeltronentity_task.httpsgithub.entitytask.TaskStorage;


/**
* In diesem Fenster startet die App
 * und man hat die auswahl nach der designvorlage zu kalender oder tasks zu gehen
* */

public class MainActivity extends AppCompatActivity {
    
    private TaskStorage taskStorage;
    private ViewPager viewPager;

    private ProgressBar progressBar;
    private ImageView viewLogo;
    private TextView txtLevel;
    private TextView txtToday;
    private Animation rotateAnimation;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Erstellung des Taskspeicherobjekts
        taskStorage = new TaskStorage();

        //txt-today
        txtToday = findViewById(R.id.txtToday);

        //logo
        final Context context = this;
        viewLogo = findViewById(R.id.viewLogo);
        viewLogo.setClickable(true);
        viewLogo.setOnClickListener(new View.OnClickListener() {
            @Override                                               //click auf logo wechselt ob today sichbar ist
            public void onClick(View view) {
                rotateAnimation = AnimationUtils.loadAnimation(context,R.anim.rotate);
                viewLogo.startAnimation(rotateAnimation);

                if (viewPager.getVisibility() == View.VISIBLE){
                    viewPager.setVisibility(View.GONE);
                    txtToday.setVisibility(View.GONE);
                }else{
                    viewPager.setVisibility(View.VISIBLE);
                    txtToday.setVisibility(View.VISIBLE);
                }
            }
        });

        //progressbar
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMin(0);
        progressBar.setMax(100);
        progressBar.setProgress(0);

        //level-text
        txtLevel = findViewById(R.id.txtLevel);
        int level = taskStorage.profile.getLevel();
        txtLevel.setText("Level: " + level);

        //erstellung des tasks today viewpager
        viewPager = findViewById(R.id.viewPager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            refreshViewPager();
        }
    }

    //erstellen/erneuern  des viewpager auf basis des heutigen datum
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void refreshViewPager() {
        LocalDate today = LocalDate.now();
        ArrayList<Task> selectedTasks = new ArrayList<>();              //DONE tasks, spezifisch für ihren type anzeigen

        for (Task t : taskStorage.getTasks()) {

            int i = taskStorage.getTasks().indexOf(t);
            if (!taskStorage.getOneTaskState(i)) {

                if (t instanceof TaskMonthly) {
                    for (LocalDate d : t.getDates()) {
                        if (d.getDayOfMonth() == today.getDayOfMonth()) { //checkt für jeden monthlytask jedes datum ob der tag passt
                            selectedTasks.add(t);
                            break;
                        }
                    }
                } else if (t instanceof TaskCustom) {
                    if (t.getDates().contains(today)) {
                        selectedTasks.add(t);
                    }
                } else {
                    if (t.getDates().contains(today)) {
                        selectedTasks.add(t);
                        //weekly                                    //todo anzeige von weekly anhand von days
                    }
                }
            }
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, selectedTasks);
            viewPager.setAdapter(viewPagerAdapter);
        }
    }

    //Link zu TaskActivity
    public void gotoTasks(View view){
        Intent intent = new Intent(this, TasksActivity.class);
        intent.putExtra("TASKSTORAGE", taskStorage);    //Übergeben des Storage über Intent
        intent.putExtra("ITEMINDEX", -1);   //kein spezielles item, das angezeigt werde soll
        startActivityForResult(intent, 1);
    }

    public void gotoSpecificTask(Task task) {
        int itemIndex = taskStorage.getTasks().indexOf(task);

        Intent intent = new Intent(this, TasksActivity.class);
        intent.putExtra("TASKSTORAGE", taskStorage);    //Übergeben des Storage über Intent
        intent.putExtra("ITEMINDEX", itemIndex);    //übergeben des index des clicked-task, der angezeigt werden soll
        startActivityForResult(intent, 1);
    }

    //Link zu CalendarActivity
    public void gotoCalendar(View view){
        Intent intent = new Intent(this, CalendarActivity.class);
        intent.putExtra("TASKSTORAGE", taskStorage);    //Übergeben des Storage über Intent
        startActivityForResult(intent, 1);
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
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 250);
        toast.show();
        refreshViewPager();

        taskStorage.profile.increasePoints(20);
        updateProgress();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateProgress(){
        progressBar.setProgress(taskStorage.profile.getPoints(), true);
        int level = taskStorage.profile.getLevel();
        txtLevel.setText("Level: " + level);
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
                updateProgress();
            }
        }
    }
}
