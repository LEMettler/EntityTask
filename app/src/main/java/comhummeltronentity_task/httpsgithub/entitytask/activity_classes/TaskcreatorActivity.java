package comhummeltronentity_task.httpsgithub.entitytask.activity_classes;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;

import comhummeltronentity_task.httpsgithub.entitytask.R;
import comhummeltronentity_task.httpsgithub.entitytask.task_classes.Task;
import comhummeltronentity_task.httpsgithub.entitytask.task_classes.TaskCustom;
import comhummeltronentity_task.httpsgithub.entitytask.task_classes.TaskMonthly;
import comhummeltronentity_task.httpsgithub.entitytask.task_classes.TaskStorage;

/**
* Hier werden die einzelnen Tasks erzeugt
 * TODO tasks löschen/back  DONE
 *
 * TODO reminderfunktion in tasks einbauen, ist ne seperate geschichte, wird über solche push notifications geregelt
 * TODO ist eine zeit bereits gewählt sollte diese noch nachträglich änderbar sein
 * (siehe https://www.youtube.com/watch?v=SWsuijO5NGE)
 *TODO wenn kein datum (auch zeit?) ausgewählt, soll der task nicht erstellt werden können  DONE
 *
 *
 * vorallem:
 * TODO finde eine passende auswahlmöglichkeit für weekly
 * TODO xml lauyout richtig sortieren und organisierte verankerungen
 *
 *
 * für die custom und motnhly auswahl gibt es ne sache die heist datepicker, ist n popup ding von google mit einfacher tagesausauswahl
 * für weekly gäbe es die möglichkeit einzelner felder für die einzelnen tage, deren darstellung ist die frage
 *
 *
* */


public class TaskcreatorActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private TaskStorage taskStorage = new TaskStorage();

    //Attribute für GUI
    private RadioButton rbtnCustom;
    private RadioButton rbtnMonthly;
    private RadioButton rbtnWeekly;
    private Button btnPick;
    private Button btnAddDate;
    private Button btnSave;
    private TextView txtInputDate, txtInputTime;
    private Switch switchReminder;
    private EditText inputTitle;
    private EditText inputDescription;


    //Attribute zum Date/Time input
    private String inputTime;
    private String inputYear, inputMonth, inputDay;
    private ArrayList<String> dates = new ArrayList<>();
    private AlertDialog.Builder  alertDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskcreator);


        inputTitle = findViewById(R.id.inputTitle);
        inputDescription = findViewById(R.id.inputDescription);
        btnPick = findViewById(R.id.btnPick);
        btnAddDate = findViewById(R.id.btnAddDate);
        rbtnCustom = findViewById(R.id.rbtnCustom);
        rbtnMonthly = findViewById(R.id.rbtnMonthly);
        rbtnWeekly = findViewById(R.id.rbtnWeekly);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setVisibility(View.GONE);

        switchReminder = findViewById(R.id.switchReminder);
        txtInputDate = findViewById(R.id.txtInputDate);
        txtInputTime = findViewById(R.id.txtInputTime);

        //taskStorage = getIntent().getParcelableExtra("TASKSTORAGE");
        taskStorage = getIntent().getExtras().getParcelable("TASKSTORAGE");

        //edit eines bestehenden tasks - setup
        int i = getIntent().getExtras().getInt("EDITTASK");
        if (i >= 0){
            Task task = taskStorage.getTasks().get(i);
            inputTitle.setText(task.getTitle());
            inputDescription.setText(task.getDescription());
        }


        //**************************************************************************************************************************************
        //**************************************************************************************************************************************
        /**
         * hier einen alertdialog erstellen, der für die weekly-auswahl einen dialog anzeigt, in dem versch tage ausgewählt werden können
         * dazu müsst ihr die xml datei "dialog_weeky" in res/layout/ dem dialogbuilder adden etc.
         * schaut tutorials dazu, was glaubt ihr, warum ich das nicht selbst mache.
         * den dialog initialisiert ihr in btnPickHandler() und in addTask() soll ein weekly-task erstellt werden und geadded werden (siehe andere tasks)
         */
        alertDialogBuilder = new AlertDialog.Builder(this);     //hier entsteht der dialog für die tage auswahl


        //**************************************************************************************************************************************
        //**************************************************************************************************************************************
    }

    //Handler der Radiobutton -> Anzeige der Time/Date picker
    public void rbtnCustomHandler(View v){
        btnPick.setVisibility(View.VISIBLE);
    }

    public void rbtnMonthlyHandler(View v){
        btnPick.setVisibility(View.VISIBLE);
    }

    public void rbtnWeeklyHandler(View v){      //brauchen wir das noch?
        btnPick.setVisibility(View.VISIBLE);
    }


    //Methoden der Time/Datepicker

    //auswahl 1. datum & time
    public void btnPickHandler(View v){

        if (!rbtnWeekly.isChecked()) {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, year, month, day);
            datePickerDialog.show();
        }else{
            /**
             * hier den weekly-dialog erstellen
             *anschließend soll auch ein timepicker erstellt werden (siehe ~ line 169)
             * die tage (sollten am besten gleich) in einem boolean-array zwischengespeichert werden, um sie dann in addTask() gleich so zu adden
             */
        }
    }
    //auswahl weiterer datuminen
    public void btnAddDateHandler(View v){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, year, month, day);
        datePickerDialog.show();

        txtInputDate.setText(txtInputDate.getText() + ", " + dates.get(dates.size() - 1));
    }
    //nachdem das popup zur datumsasuwahl mit ok bestätigt wurde, wird diese methode aufgerufen, zuerstmal wird das datum abgespeichert (+ überprüfung ob doppelt)
    //wenn es das erste datum ist -> auch time picken
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        inputYear = Integer.toString(i);

        if ((i1+1) <10)                                     //Damit das Datum später einfacher verarbeitet werden kann
            inputMonth = "0" + Integer.toString(i1+1);   //erfüllen wir das ISO-8601-Format (YYYY-MM-DD)
        else                                               //dazu müssen gegebenenfalls eine 0 vor monat oder tag gesetzt werden
            inputMonth = Integer.toString(i1+1);

        if ((i2) <10)
            inputDay = "0" + Integer.toString(i2);
        else
            inputDay = Integer.toString(i2);

        dates.add(inputYear +"-"+ inputMonth +"-"+ inputDay);

       if (btnPick.getVisibility() == View.VISIBLE){        //Timepicker soll nur bei erster auswahl geöffnet werden
           Calendar c = Calendar.getInstance();
           int hour = c.get(Calendar.HOUR_OF_DAY);
           int minute = c.get(Calendar.MINUTE);
           TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, hour, minute, true);
           timePickerDialog.show();
       }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {

        if (i<10)                                           //auch hier soll das iso-format erfüllt sein (HH:MM:SS)
            inputTime = "0" + i + ":";                      //todo feintuning: die sekunden nicht anzeigen
        else
            inputTime = i + ":";

        if (i1<10)
            inputTime = inputTime + "0" + i1 + ":00";
        else
            inputTime = inputTime + i1 + ":00";

        txtInputDate.setText(dates.get(0));
        txtInputTime.setText(inputTime);

        btnPick.setVisibility(View.GONE);
        btnAddDate.setVisibility(View.VISIBLE);
        btnSave.setVisibility(View.VISIBLE);
    }


    //Erzeugung der Taskobjekte
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addTask(View v){


        String title = inputTitle.getText().toString();
        String description = inputDescription.getText().toString();
        Boolean reminder = switchReminder.isActivated();


        //CustomTask
        if (rbtnCustom.isChecked()) {
            ArrayList<LocalDate> datesFormat = new ArrayList<>();        //im falle von custom/monthly wird die
            for (String d : dates) {                                     //liste der datuminanen  ins LocalDateFormat umgewandelt
                datesFormat.add(LocalDate.parse(d));
            }
            LocalTime timeFormat = LocalTime.parse(inputTime);

            TaskCustom newtask = new TaskCustom(title, description, reminder, timeFormat, datesFormat);
          //  returnTask.putExtra("TASK", newtask);
            taskStorage.addTask(newtask);

            //MonthlyTask
        }else if (rbtnMonthly.isChecked()) {
            ArrayList<LocalDate> datesFormat = new ArrayList<>();
            for (String d : dates) {
                datesFormat.add(LocalDate.parse(d));
            }
            LocalTime timeFormat = LocalTime.parse(inputTime);

            TaskMonthly newtask = new TaskMonthly(title, description, reminder, timeFormat, datesFormat);
          //  returnTask.putExtra("TASK", newtask);
            taskStorage.addTask(newtask);

            //WeeklyTask                        //TODO weekly tasks erstellen
        }else{
            /**
             * hier die weekly-tasks erstellen und in den taskStorage adden
             */

        }

        //zurück zu taskactivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("TASKSTORAGE", taskStorage);
        setResult(1, resultIntent);
        finish();
    }


//delete-button -> zurück
    public void btnDeleteHandler(View v){
        finish();
    }

    @Override
    public void finish() {
        //rückgabe des taskstorage an taskactivity (auch ohne änderung oder sinn , nur für guten codeflow)
        Intent resultIntent = new Intent();
        resultIntent.putExtra("TASKSTORAGE", taskStorage);
        setResult(1, resultIntent);

        super.finish();
    }

}
