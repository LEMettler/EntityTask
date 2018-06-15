package comhummeltronentity_task.httpsgithub.entitytask;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.time.LocalDate;

import comhummeltronentity_task.httpsgithub.entitytask.task_classes.Task;
import comhummeltronentity_task.httpsgithub.entitytask.task_classes.TaskCustom;

/**
 * Created by Meerlu on 14.06.2018.
 *
 * KANN AUCH GELÖSCHT WERDENKANN AUCH GELÖSCHT WERDENKANN AUCH GELÖSCHT WERDEN
 * KANN AUCH GELÖSCHT WERDENKANN AUCH GELÖSCHT WERDENKANN AUCH GELÖSCHT WERDEN
 */

public class DBHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "tasksdatabase.db";

    //tasks-table
    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_TID = "tid";
    public static final String COLUMN_TASK_TITLE = "task_title";
    public static final String COLUMN_TASK_DESCRIPTION = "task_description";
    public static final String COLUMN_TASK_REMINDER = "task_reminder";
    public static final String COLUMN_TASK_TIME = "task_time";

    //date-table
    public static final String TABLE_DATES = "dates";
    public static final String COLUMN_DID = "did";
    public static final String COLUMN_DATES_DATE = "dates_date";

    //tasks_date-table
    public static final String TABLE_TASKSDATES = "tasksdates";
    public static final String COLUMN_TDID = "tdid";
    public static final String COLUMN_TASKSDATES_TID = "tid";
    public static final String COLUMN_TASKSDATES_DID = "did";

    public static final String COLUMN_TASKSDATES_MONDAY = "td_monday";
    public static final String COLUMN_TASKSDATES_TUESDAY = "td_tuesday";
    public static final String COLUMN_TASKSDATES_WEDNESDAY = "td_wednesday";
    public static final String COLUMN_TASKSDATES_THURSDAY = "td_thursday";
    public static final String COLUMN_TASKSDATES_FRIDAY = "td_friday";
    public static final String COLUMN_TASKSDATES_SATURDAY = "td_saturday";
    public static final String COLUMN_TASKSDATES_SUNDAY = "td_sunday";

    //todo auch hier fehlt wöchentlich, für jeden tag in dieser tabelle eine column, dann did = null


    // table-create statements
    private static final String CREATE_TABLE_TASKS =
            "CREATE TABLE " + TABLE_TASKS
            + "("
                + COLUMN_TID + " INTEGER PRIMARY KEY,"
                + COLUMN_TASK_TITLE + " TEXT,"
                + COLUMN_TASK_DESCRIPTION + "TEXT"
                + COLUMN_TASK_REMINDER + " INTEGER,"
                + COLUMN_TASK_TIME + " TEXT"
            + ")";

    private static final String CREATE_TABLE_DATES =
            "CREATE TABLE " + TABLE_DATES
             + "("
                    + COLUMN_DID + " INTEGER PRIMARY KEY,"
                    + COLUMN_DATES_DATE + " TEXT"
             + ")";

    private static final String CREATE_TABLE_TASKS_DATES =
            "CREATE TABLE " + TABLE_TASKSDATES
            + "("
                    + COLUMN_TDID + " INTEGER PRIMARY KEY,"
                    + COLUMN_TASKSDATES_TID + " INTEGER SECONDARY KEY,"

                    + COLUMN_TASKSDATES_MONDAY + " INTEGER,"
                    + COLUMN_TASKSDATES_TUESDAY + " INTEGER,"
                    + COLUMN_TASKSDATES_WEDNESDAY + " INTEGER,"
                    + COLUMN_TASKSDATES_THURSDAY + " INTEGER,"
                    + COLUMN_TASKSDATES_FRIDAY + " INTEGER,"
                    + COLUMN_TASKSDATES_SATURDAY + " INTEGER,"
                    + COLUMN_TASKSDATES_SUNDAY + " INTEGER,"


                    + COLUMN_TASKSDATES_DID + " INTEGER SECONDARY KEY"
            + ")";

    //**********************************************************************************************

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create tables
        db.execSQL(CREATE_TABLE_TASKS);
        db.execSQL(CREATE_TABLE_DATES);
        db.execSQL(CREATE_TABLE_TASKS_DATES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
// on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKSDATES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATES);

        // create new tables
        onCreate(db);
    }

    //**********************************************************************************************
    public void addTask(Task task, long[] tag_ids) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valuesTASKS = new ContentValues();
        valuesTASKS.put(COLUMN_TASK_TITLE, task.getTitle());
        valuesTASKS.put(COLUMN_TASK_DESCRIPTION, task.getDescription());
        int x = task.getReminder() ? 1 : 0;
        valuesTASKS.put(COLUMN_TASK_REMINDER, x);
        valuesTASKS.put(COLUMN_TASK_TIME, task.getTime().toString());

        ContentValues valuesDATES = new ContentValues();
        if ((task instanceof TaskCustom)){

            for ( LocalDate date : task.getDates() ) {

            }
        }


        db.insert(TABLE_TASKS, null, valuesTASKS);
        db.close();
    }
}
