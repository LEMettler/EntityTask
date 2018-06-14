package comhummeltronentity_task.httpsgithub.entitytask;

/**
 * Created by Meerlu on 08.05.2018.
 */


import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import comhummeltronentity_task.httpsgithub.entitytask.activity_classes.Profile;
import comhummeltronentity_task.httpsgithub.entitytask.task_classes.Task;

/**
 * Ein Objekt dieser Klasse wird in Main erzeugt, und daraufhin jeder neuen Activitiy übergegeben
 * alle Activities mit Zugriff, können dadurch also -Tasks abrufen  -Tasks hinzufügen
 */
public class TaskStorage implements Parcelable {
    //Hier liegen alle Tasks
    private ArrayList<Task> tasks;
    private ArrayList<Boolean> taskState;

    public Profile profile;

    public TaskStorage() {
        tasks = new ArrayList<>();
        taskState = new ArrayList<>();
        profile = new Profile();
    }

    //****************************************************************************************************
    //Parcelable-Methoden, benötigt um Objekte zwischen Aktivites zu übergeben 
    public TaskStorage(Parcel in) {
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        tasks = in.readArrayList(Task.class.getClassLoader());
        taskState = in.readArrayList(Boolean.class.getClassLoader());
        //in.readList( tasks,Task.class.getClassLoader());
        profile = in.readParcelable(Profile.class.getClassLoader());
    }

    public static final Creator<TaskStorage> CREATOR = new Creator<TaskStorage>() {
        @Override
        public TaskStorage createFromParcel(Parcel in) {
            return new TaskStorage(in);
        }

        @Override
        public TaskStorage[] newArray(int size) {
            return new TaskStorage[size];
        }
    };

    @Override

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(tasks);
        parcel.writeList(taskState);
        parcel.writeParcelable(profile, i);
    }
    //***********************************************************************************************

    /**
     *
     * TEST TO SAVE ALL TASKS TO A TEXTFILE
     */

    public void saveTasksToFile(Context context) throws IOException {

        String tasks_file_name = "tasks_data.txt";

        String tasks_file_path = context.getFilesDir().toString();
        File tasks_file = new File(tasks_file_path, tasks_file_name);
        tasks_file.createNewFile();

        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(tasks_file));

            for ( Task task : tasks ) {
                outputStream.writeObject(task);
            }
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String state_file_name = "state_data.txt";
        String state_file_path = context.getFilesDir().toString();
        File state_file = new File(state_file_path, state_file_name);
        state_file.createNewFile();
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(state_file));

            for ( Boolean b : taskState ) {
                outputStream.writeObject(b);
            }
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void readTasksFromFile(Context context) {

        String tasks_file_name = "tasks_data.txt";
        String tasks_file_path = context.getFilesDir().toString();
        File tasks_file = new File(tasks_file_path, tasks_file_name);

        if (tasks_file.exists()){

        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(tasks_file));
            boolean cont = true;
            while (cont) {
                Task task = (Task) inputStream.readObject();
                if (task != null) {
                    tasks.add(task);
                } else {
                    cont = false;
                }
                inputStream.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        }

        String state_file_name = "state_data.txt";
        String state_file_path = context.getFilesDir().toString();
        File state_file = new File(state_file_path, state_file_name);

        if (state_file.exists()){

            try {
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(state_file));
                boolean cont = true;
                while (cont) {
                    Boolean b = (Boolean) inputStream.readObject();
                    if (b != null) {
                        taskState.add(b);
                    } else {
                        cont = false;
                    }
                    inputStream.close();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

        /**
     *
     *
     */

    //Access für die Activities
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public ArrayList<Boolean> getTaskState() {
        return taskState;
    }

    public Boolean getOneTaskState(int i){

        return taskState.get(i);
    }

    public void setTaskState(ArrayList<Boolean> taskDone) {
        this.taskState = taskDone;
    }

    public void setOneTaskState(int index, Boolean state){
        taskState.set(index, state);
    }

    public void addTask(Task task) {
        tasks.add(task);
        taskState.add(false);
    }
}
