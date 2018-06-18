package comhummeltronentity_task.httpsgithub.entitytask.activity_classes;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import comhummeltronentity_task.httpsgithub.entitytask.R;
import comhummeltronentity_task.httpsgithub.entitytask.TaskStorage;
import comhummeltronentity_task.httpsgithub.entitytask.task_classes.Task;
import comhummeltronentity_task.httpsgithub.entitytask.task_classes.TaskCustom;
import comhummeltronentity_task.httpsgithub.entitytask.task_classes.TaskMonthly;
import comhummeltronentity_task.httpsgithub.entitytask.task_classes.TaskWeekly;

/**
* Hier werden die einzelnen Tasks erzeugt
 * DONE tasks löschen/back
 *
 * TODO reminderfunktion in tasks einbauen, ist ne seperate geschichte, wird über solche push notifications geregelt
 * TODO ist eine zeit bereits gewählt sollte diese noch nachträglich änderbar sein
 * TODO gleiches datumano sollte nicht mehrmals ausgewählt werden können;
 * (siehe https://www.youtube.com/watch?v=SWsuijO5NGE)
 *DONE wenn kein datum (auch zeit?) ausgewählt, soll der task nicht erstellt werden können
 *
 *
 * vorallem:
 * DONE finde eine passende auswahlmöglichkeit für weekly
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

    private Task preTask;
    private int preIndex;


    //Attribute zum Date/Time input
    private String inputTime;
    private boolean[] inputDays;
    private String inputYear, inputMonth, inputDay;
    private ArrayList<String> dates = new ArrayList<>();

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

        inputDays = new boolean[7];
        Arrays.fill(inputDays,false);

        //taskStorage = getIntent().getParcelableExtra("TASKSTORAGE");
        taskStorage = getIntent().getExtras().getParcelable("TASKSTORAGE");

        //edit eines bestehenden tasks - setup
        preIndex = (int) getIntent().getExtras().get("EDITTASK");
        System.out.println(preIndex);
        if (preIndex != -1) {
            preTask = taskStorage.getTasks().get(preIndex);
            inputTitle.setText(preTask.getTitle());
            inputDescription.setText(preTask.getDescription());
        }

    }

    //Handler der Radiobutton -> reset der Time/Date picker bei neuauswahl
    public void rbtnHandler(View v){
        btnPick.setVisibility(View.VISIBLE);
        btnAddDate.setVisibility(View.INVISIBLE);
        btnSave.setVisibility(View.GONE);
        inputDay = "";
        inputMonth = "";
        inputYear = "";
        inputTime = "";

        txtInputDate.setText("-");
        txtInputTime.setText("-");
    }


    //Methoden der Time/Datepicker

    //auswahl 1. datum & time
    public void btnPickHandler(View v){

        final Context context = this;

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        if (!rbtnWeekly.isChecked()) {
            //DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, year, month, day);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme, this, year, month, day );
            datePickerDialog.show();
        }else{

            final Dialog d = new Dialog(this);
            d.setTitle("Days");
            d.setCancelable(false);

            d.setContentView(R.layout.dialog_weekly);

            final CheckBox rbtnMonday = d.findViewById(R.id.checkMonday);
            final CheckBox rbtnTuesday = d.findViewById(R.id.checkTuesday);
            final CheckBox rbtnWednesday = d.findViewById(R.id.checkWednesday);
            final CheckBox rbtnThursday = d.findViewById(R.id.checkThursday);
            final CheckBox rbtnFriday = d.findViewById(R.id.checkFriday);
            final CheckBox rbtnSaturday = d.findViewById(R.id.checkSaturday);
            final CheckBox rbtnSunday = d.findViewById(R.id.checkSunday);


            Button btnCancel = d.findViewById(R.id.btnCancel);
            Button btnOK = d.findViewById(R.id.btnOK);

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    d.dismiss();
                }
            });

            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inputDays[0] = rbtnMonday.isChecked();
                    inputDays[1] = rbtnTuesday.isChecked();
                    inputDays[2] = rbtnWednesday.isChecked();
                    inputDays[3] = rbtnThursday.isChecked();
                    inputDays[4] = rbtnFriday.isChecked();
                    inputDays[5] = rbtnSaturday.isChecked();
                    inputDays[6] = rbtnSunday.isChecked();

                    Calendar c = Calendar.getInstance();
                    int hour = c.get(Calendar.HOUR_OF_DAY);
                    int minute = c.get(Calendar.MINUTE);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(context, R.style.DialogTheme, (TimePickerDialog.OnTimeSetListener) context, hour, minute, true);
                    timePickerDialog.show();

                    d.dismiss();
                }
            });
            d.show();

        }
    }
    //auswahl weiterer datuminen
    public void btnAddDateHandler(View v){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        final Context context = this;

        if (rbtnWeekly.isChecked()) {

            final Dialog d = new Dialog(this);
            d.setTitle("Days");
            d.setCancelable(false);

            d.setContentView(R.layout.dialog_weekly);

            final CheckBox rbtnMonday = d.findViewById(R.id.checkMonday);
            final CheckBox rbtnTuesday = d.findViewById(R.id.checkTuesday);
            final CheckBox rbtnWednesday = d.findViewById(R.id.checkWednesday);
            final CheckBox rbtnThursday = d.findViewById(R.id.checkThursday);
            final CheckBox rbtnFriday = d.findViewById(R.id.checkFriday);
            final CheckBox rbtnSaturday = d.findViewById(R.id.checkSaturday);
            final CheckBox rbtnSunday = d.findViewById(R.id.checkSunday);

            Button btnCancel = d.findViewById(R.id.btnCancel);
            Button btnOK = d.findViewById(R.id.btnOK);

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    d.dismiss();
                }
            });

            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inputDays[0] = rbtnMonday.isChecked();
                    inputDays[1] = rbtnTuesday.isChecked();
                    inputDays[2] = rbtnWednesday.isChecked();
                    inputDays[3] = rbtnThursday.isChecked();
                    inputDays[4] = rbtnFriday.isChecked();
                    inputDays[5] = rbtnSaturday.isChecked();
                    inputDays[6] = rbtnSunday.isChecked();

                    d.dismiss();
                }
            });
            d.show();

            txtInputDate.setText("");

            if (inputDays[0])
                txtInputDate.setText("Monday");
            if (inputDays[1])
                txtInputDate.setText(txtInputDate.getText() + "Tuesday");
            if (inputDays[2])
                txtInputDate.setText(txtInputDate.getText() + "Wednesday");
            if (inputDays[3])
                txtInputDate.setText(txtInputDate.getText() + "Thursday");
            if (inputDays[4])
                txtInputDate.setText(txtInputDate.getText() + "Friday");
            if (inputDays[5])
                txtInputDate.setText(txtInputDate.getText() + "Saturday");
            if (inputDays[6])
                txtInputDate.setText(txtInputDate.getText() + "Sunday");

        }else {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme, this, year, month, day);
            datePickerDialog.show();
            txtInputDate.setText(txtInputDate.getText() + ", " + dates.get(dates.size() - 1));

        }

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
           TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.DialogTheme,this, hour, minute, true);
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

        if (rbtnWeekly.isChecked()) {
            txtInputDate.setText("");

            if (inputDays[0])
                txtInputDate.setText("Monday");
            if (inputDays[1])
                txtInputDate.setText(txtInputDate.getText() + "Tuesday");
            if (inputDays[2])
                txtInputDate.setText(txtInputDate.getText() + "Wednesday");
            if (inputDays[3])
                txtInputDate.setText(txtInputDate.getText() + "Thursday");
            if (inputDays[4])
                txtInputDate.setText(txtInputDate.getText() + "Friday");
            if (inputDays[5])
                txtInputDate.setText(txtInputDate.getText() + "Saturday");
            if (inputDays[6])
                txtInputDate.setText(txtInputDate.getText() + "Sunday");

        } else {
            txtInputDate.setText(dates.get(0));

        }

        txtInputTime.setText(inputTime);

        btnPick.setVisibility(View.INVISIBLE);
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

            if (preIndex == -1) {
                taskStorage.addTask(newtask);
            } else {
               taskStorage.getTasks().set(preIndex, newtask);
                taskStorage.getTaskState().set(preIndex,false);

            }
//**************************************************************************************************
            //MonthlyTask
        }else if (rbtnMonthly.isChecked()) {
            ArrayList<LocalDate> datesFormat = new ArrayList<>();
            for (String d : dates) {
                datesFormat.add(LocalDate.parse(d));
            }
            LocalTime timeFormat = LocalTime.parse(inputTime);

            TaskMonthly newtask = new TaskMonthly(title, description, reminder, timeFormat, datesFormat);

            if (preIndex == -1) {
                taskStorage.addTask(newtask);
            } else {
                taskStorage.getTasks().set(preIndex, newtask);
                taskStorage.getTaskState().set(preIndex,false);

            }
//**************************************************************************************************
            //WeeklyTask                        //DONE weekly tasks erstellen
        }else{

            LocalTime timeFormat = LocalTime.parse(inputTime);
            TaskWeekly newtask = new TaskWeekly(title, description, reminder, timeFormat, inputDays);

            if (preIndex == -1) {
                taskStorage.addTask(newtask);
            } else {
                taskStorage.getTasks().set(preIndex, newtask);
                taskStorage.getTaskState().set(preIndex, false);
            }
        }

        //zurück zu taskactivity
        taskStorage.saveTasksToFile(this);
        finish();
    }

    @Override
    protected void onStop() {
        taskStorage.saveProfileToFile(this);
        super.onStop();
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
