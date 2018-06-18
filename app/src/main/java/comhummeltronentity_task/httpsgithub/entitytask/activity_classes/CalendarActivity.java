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

    //************************Attribute**************************************************************
    private CalendarView calendar;
    private ViewPager viewPager;
    private TaskStorage taskStorage;
    private LocalDate selectedDate;

    //************************onCreate**************************************************************
    /**
     * initializieren der xml mit calendar + aufnehmen des taskstorage
     */
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


//****************************viewpager & progressbar*******************************************
    /**
     * <lange, schwierige methode die immernoch angstzustände in mir auslöst>
     *
     * angezeigt werden die tasks, die zum, im calendar, ausgewählten datum passen, das today aus
     * der main ist also selectedDate
     * auch wollen wir hier nicht nur die noch nicht done tasks anzeigen, sondern alle des tages.
     * je nachdem welchen status der task in beziehung zu heute hat, wird der done button ersetzt
     * dies regelt aber der viewpageadapter, der im support-ordner liegt
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

            //}else{

                LocalDate today = LocalDate.now();

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
                    int j = 0;
                    for (LocalDate d : t.getDates()){
                        if (d != today) {
                            i++;                //wenn alle datume nicht heute sind, dann setze wieder done = false
                        }
                    }
                    if (j == t.getDates().size()) {
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
            //}
        }

        ArrayList<Boolean> selectedTaskStates = new ArrayList<>();
        for ( Task t: selectedTasks) {
            Boolean state = taskStorage.getOneTaskState(t);
            selectedTaskStates.add(state);
        }

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, selectedTasks, selectedTaskStates, selectedDate);
        viewPager.setAdapter(viewPagerAdapter);
    }


    /**
     * über den viewpager kann der user den button DONE triggern, der ruft diese methode auf.
     * der state des tasks wird auf done gesetzt und das wird in einem toast (kleine message)
     * angezeigt, anschliesend bekommt der user xp auf sein profil hinzuaddiert und dessen anzeige
     * aktualisiert
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
        toast.show();
        refreshViewPager();

        taskStorage.profile.increasePoints(20);
        taskStorage.saveTasksToFile(this);
        taskStorage.saveProfileToFile(this);
    }

    //****************************Activity-Wechsel**************************************************
    /**
     * Activities werden für eine rückgabe aufgerufen,
     * dabei wird ihnen ein Intent gegeben, dieser dient einfach zur übertragung des taskStroage,
     * der auch wieder zurück gegeben wird (weil die referenz an ein objekt zwar weitergegeben wird,
     * aber beim zurückgehen nicht dort aktualisiert wird)
     *
     * Den an deise zurückgegebene Storage speichern wir dann wieder
     */


    //vom viewpageadapter gecallt wenn ein view geklickt wird um genau den anzuzeigen
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

