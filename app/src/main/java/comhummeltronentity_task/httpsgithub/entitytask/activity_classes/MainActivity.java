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
import comhummeltronentity_task.httpsgithub.entitytask.TaskStorage;
import comhummeltronentity_task.httpsgithub.entitytask.activity_classes.mainactivity_support.ProgressBarAnimation;
import comhummeltronentity_task.httpsgithub.entitytask.activity_classes.mainactivity_support.ViewPagerAdapter;
import comhummeltronentity_task.httpsgithub.entitytask.task_classes.Task;
import comhummeltronentity_task.httpsgithub.entitytask.task_classes.TaskCustom;
import comhummeltronentity_task.httpsgithub.entitytask.task_classes.TaskMonthly;
import comhummeltronentity_task.httpsgithub.entitytask.task_classes.TaskWeekly;


/**
* In diesem Fenster startet die App
 *und man hat die auswahl nach der designvorlage zu kalender oder tasks zu gehen
 *
 * *************************
 * attribute und methoden bitte in englisch halten leude, falls das wer irgendwann mal noch ließt
 * kommentare zum erklären da passiert, eher nicht zeile für zeile
 * *************************
 *
 *TODO zukünftig kann noch eine notification funktion eingebaut werden, grundlagen sind gelegt (hauptproblem: benötigt einen eigenen backgroundservice)
* */

public class MainActivity extends AppCompatActivity {

    //**************************Attribute***********************************************************
    private TaskStorage taskStorage;
    private ViewPager viewPager;

    private ProgressBar progressBar;
    private ImageView viewLogo;
    private TextView txtLevel;
    private TextView txtToday;
    private Animation rotateAnimation;


    //****************************onCreate**********************************************************
    /**
     * initializieren der xml + erste erzeugung des storage
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Erstellung des Taskspeicherobjekts
        taskStorage = new TaskStorage();
        taskStorage.readTasksFromFile(this);
        taskStorage.readProfileFromFile(this);

        //txt-today
        txtToday = findViewById(R.id.txtToday);

        //logo
        final Context context = this;
        viewLogo = findViewById(R.id.viewLogo);
        viewLogo.setClickable(true);
        viewLogo.setOnClickListener(new View.OnClickListener() {
            @Override                                               //click auf logo wechselt ob today sichbar ist
            public void onClick(View view) {                                //rotation des logo
                rotateAnimation = AnimationUtils.loadAnimation(context,R.anim.rotate);
                viewLogo.startAnimation(rotateAnimation);
                viewLogo.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (viewPager.getVisibility() == View.VISIBLE){
                            viewPager.setVisibility(View.GONE);
                            txtToday.setVisibility(View.GONE);
                        }else{
                            viewPager.setVisibility(View.VISIBLE);
                            txtToday.setVisibility(View.VISIBLE);
                        }
                    }

                }, rotateAnimation.getDuration());    //wait until animation finished

            }
        });

        //progressbar
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMin(0);
        progressBar.setMax(100);
        progressBar.setProgress(taskStorage.profile.getPoints());


        //level-text
        txtLevel = findViewById(R.id.txtLevel);
        int level = taskStorage.profile.getLevel();
        txtLevel.setText(Integer.toString(level));

        //erstellung des tasks today viewpager
        viewPager = findViewById(R.id.viewPager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            refreshViewPager();
        }
    }

    @Override
    protected void onStop() {
        taskStorage.saveProfileToFile(this);    //beim schließen nochmal alles speichern
        taskStorage.saveTasksToFile(this);
        super.onStop();
    }


    //****************************viewpager & progressbar*******************************************
    /**
     * <lange, schwierige methode die immernoch angstzustände in mir auslöst>
     *
     * wir wollen in der main nur die tasks anzeigen, die gerade für diesen spezifischen tag (today)
     * nicht done sind. Dazu wirden in ner schleife alle tasks durchgegangen und je nach Subtasks
     * speziell behandelt
     *
     * DONE = FALSE
     * + custom
     *      - today ist in der datumliste des tasks
     * + monthly
     *      - alle datum durchgehen, schauen ob der tag des monats mit dem von today übereinstimmt
     * + weekly
     *      -index von today in der woche pullen
     *      -aus der boolean tagesliste den index abfragen, ob dieser nicht schon done ist
     *
     * DONE = TRUE
     *      wenn der task zwar done ist, nicht heute relevant ist,
     *      aber in der zukunft nochmal vorkommt, wird er wieder zurückgesetzt
     *      (für details siehe randcomments der else)
     *
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void refreshViewPager() {

        LocalDate today = LocalDate.now();
        ArrayList<Task> selectedTasks = new ArrayList<>();              //DONE tasks, spezifisch für ihren type anzeigen

        for (Task t : taskStorage.getTasks()) {

            //done =false
            if (!taskStorage.getOneTaskState(t)) {

                //monthly
                if (t instanceof TaskMonthly) {
                    for (LocalDate d : t.getDates()) {
                        if (d.getDayOfMonth() == today.getDayOfMonth()) { //checkt für jeden monthlytask jedes datum ob der tag passt
                            selectedTasks.add(t);
                            break;
                        }
                    }
                //custom
                } else if (t instanceof TaskCustom) {
                    if (t.getDates().contains(today)) {
                        selectedTasks.add(t);
                    }
                //weekly
                } else if (t instanceof TaskWeekly) {

                    int daysIndex = today.getDayOfWeek().ordinal();
                    boolean days[] = ((TaskWeekly) t).getDays();

                    if (days[daysIndex]) {
                        selectedTasks.add(t);
                    }
                    //DONE anzeige von weekly anhand von days
                }
                //DONE reset done tasks, after the day
                //done = true
            } else{
                //custom: task expired or set to done=false again?
                if (t instanceof TaskCustom){
                    for (LocalDate d : t.getDates()){
                        if (d.isAfter(today)) {
                            taskStorage.setOneTaskState(t, false);
                            break;
                        }
                    }
                    //custom: task expired or set to done=false again?
                }else if (t instanceof TaskMonthly){
                    int i = 0;
                    for (LocalDate d : t.getDates()){
                        if (d != today) {
                            i++;                //wenn alle datume nicht heute sind, dann setze wieder done = false
                        }
                    }
                    if (i == t.getDates().size()) {
                        taskStorage.setOneTaskState(t, false);
                    }
                    //weekly: task expired or set to done=false again?
                } else if (t instanceof TaskWeekly) {
                    int todayIndex = today.getDayOfWeek().ordinal();  //wenn ein tagesindexe des tasks mit heute übereinstimmt -> done bleibt true
                    boolean days[] = ((TaskWeekly) t).getDays();     //=> wenn alle tagesindexe nicht heute sind -> done = false

                    if (!days[todayIndex]) {
                        taskStorage.setOneTaskState(t, false);
                    }
                }
            }
        }
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, selectedTasks);
        viewPager.setAdapter(viewPagerAdapter);
    }

    /**
     * über den viewpager kann der user den button DONE triggern, derr ruft diese methode auf.
     * der state des tasks wird auf done gesetzt und das wird in einem toast (kleine message)
     * angezeigt, anschliesend bekommt der user xp auf sein profil hinzuaddiert und dessen anzeige
     * aktualisiert
     *
     * @param selectedTasks
     * @param position
     */
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

        //xp geben und alles speichern
        taskStorage.profile.increasePoints(20);
        taskStorage.saveTasksToFile(this);
        taskStorage.saveProfileToFile(this);
        updateProgress(false);
    }

    /**
     * animierte aktualisierung der progressbar und des levels
     *
     * @param activityChange
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateProgress(Boolean activityChange){

        //beim activitywechsel -> keine animationen
        if (activityChange) {
            progressBar.setProgress(taskStorage.profile.getPoints(), true);
            int level = taskStorage.profile.getLevel();
            txtLevel.setText(Integer.toString(level));
        }else {

            float from;
            float to;
            if (taskStorage.profile.getPoints() == 0) {
                from = 100;
                to = 0;
            } else {
                to = taskStorage.profile.getPoints();
                from = to - 20;
            }
            //aufruf der animationsclass
            ProgressBarAnimation anim = new ProgressBarAnimation(progressBar, from, to);
            anim.setDuration(1000);
            progressBar.startAnimation(anim);

            if (from == 100) {
                //beim level-up einmal rotieren
                rotateAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate);
                rotateAnimation.setDuration(1000);
                txtLevel.startAnimation(rotateAnimation);
                txtLevel.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int level = taskStorage.profile.getLevel();
                        txtLevel.setText(Integer.toString(level));
                    }
                }, (rotateAnimation.getDuration() / 2) );    //wait until animation finished
            }
        }
    }

    //****************************Activity-Wechsel**************************************************
    /**
     * Activities werden für eine rückgabe aufgerufen,
     * dabei wird ihnen ein Intent gegeben, dieser dient einfach zur übertragung des taskStroage,
     * der auch wieder zurück gegeben wird (weil die referenz an ein objekt zwar weitergegeben wird,
     * aber beim zurückgehen nicht dort aktualisiert wird)
     *
     * Den an deise zurückgegebene Storage speichern wir dann wieder
     * @param view
     */

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            //if (resultCode == RESULT_OK)
            taskStorage = data.getExtras().getParcelable("TASKSTORAGE");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                refreshViewPager();
                updateProgress(true);
            }
        }
    }

}
