package comhummeltronentity_task.httpsgithub.entitytask;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

/**
* Hier werden die einzelnen Tasks erzeugt
 * TODO tasks löschen/back
 * TODO input-pretexte löschen bei touch
 * TODO reminderfunktion in tasks einbauen, ist ne seperate geschichte, wird über solche push notifications geregelt
 * TODO ist eine zeit bereits gewählt sollte diese noch nachträglich änderbar sein
 * (siehe https://www.youtube.com/watch?v=SWsuijO5NGE)
 *
 *
 * vorallem:
 * TODO finde eine passende auswahlmöglichkeit für weekly
 * TODO datum/datums in richtiger form an jeweilige subtasks übergeben
 * TODO xml lauyout richtig sortieren und organisierte verankerungen
 *
 *
 * für die custom und motnhly auswahl gibt es ne sache die heist datepicker, ist n popup ding von google mit einfacher tagesausauswahl
 * für weekly gäbe es die möglichkeit einzelner felder für die einzelnen tage, deren darstellung ist die frage
 *
 *
* */


public class TaskcreatorActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private ArrayList<Task> tasks = new ArrayList<>();

    //Attribute für GUI
    private RadioButton rbtnCustom;
    private RadioButton rbtnMonthly;
    private RadioButton rbtnWeekly;
    private Button btnPick;
    private Button btnAddDate;
    private TextView txtInputDate, txtInputTime;
    private Switch switchReminder;

    //Attribute zum Date/Time input
    private String inputTime;
    private String inputYear, inputMonth, inputDay;
    private ArrayList<String> dates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskcreator);

        btnPick = findViewById(R.id.btnPick);
        btnAddDate = findViewById(R.id.btnAddDate);
        rbtnCustom = findViewById(R.id.rbtnCustom);
        rbtnMonthly = findViewById(R.id.rbtnMonthly);
        rbtnWeekly = findViewById(R.id.rbtnWeekly);

        switchReminder = findViewById(R.id.switchReminder);
        txtInputDate = findViewById(R.id.txtInputDate);
        txtInputTime = findViewById(R.id.txtInputTime);
    }

    //Handler der Radiobutton -> Anzeige der Time/Date picker
    public void rbtnCustomHandler(View v){
        btnPick.setVisibility(View.VISIBLE);
    }

    public void rbtnMonthlyHandler(View v){
        btnPick.setVisibility(View.VISIBLE);
    }

    public void rbtnWeeklyHandler(View v){
        btnPick.setVisibility(View.GONE);
    }


    //Methoden der Time/Datepicker

    //auswahl 1. datum & time
    public void btnPickHandler(View v){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, year, month, day);
        datePickerDialog.show();
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

       if (btnPick.getVisibility() == View.VISIBLE){
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

        btnPick.setVisibility(View.GONE);
        btnAddDate.setVisibility(View.VISIBLE);

        txtInputDate.setText(dates.get(0));
        txtInputTime.setText(inputTime);
    }


    //Erzeugung der Taskobjekte
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addTask(View v){
        EditText inputTitle = findViewById(R.id.inputTitle);
        EditText inputDescription = findViewById(R.id.inputDescription);

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
            tasks.add(newtask);

            //MonthlyTask
        }else if (rbtnMonthly.isChecked()) {
            ArrayList<LocalDate> datesFormat = new ArrayList<>();
            for (String d : dates) {
                datesFormat.add(LocalDate.parse(d));
            }
            LocalTime timeFormat = LocalTime.parse(inputTime);

            TaskMonthly newtask = new TaskMonthly(title, description, reminder, timeFormat, datesFormat);
            tasks.add(newtask);

            //WeeklyTask                        //TODO weekly tasks erstellen
        }else{

        }
    }

    //Getter
    public ArrayList<Task> getTasks() {
        return tasks;
    }
}
