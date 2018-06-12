package comhummeltronentity_task.httpsgithub.entitytask.task_classes;

/**
 * Created by Meerlu on 08.05.2018.
 */


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Ein Objekt dieser Klasse wird in Main erzeugt, und daraufhin jeder neuen Activitiy übergegeben
 * alle Activities mit Zugriff, können dadurch also -Tasks abrufen  -Tasks hinzufügen
 */
public class TaskStorage implements Parcelable {
    //Hier liegen alle Tasks
    private ArrayList<Task> tasks;
    private ArrayList<Boolean> taskState;

    public TaskStorage() {
        tasks = new ArrayList<>();
        taskState = new ArrayList<>();
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
    }
    //***********************************************************************************************

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
