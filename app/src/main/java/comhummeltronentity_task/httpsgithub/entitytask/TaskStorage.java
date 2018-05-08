package comhummeltronentity_task.httpsgithub.entitytask;

/**
 * Created by Meerlu on 08.05.2018.
 */


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * istn experiment
 * davon wird ein Objekt erstellt, auf welches der Taskcreator als auch die Taskanzeige zugriff haben
 */
public class TaskStorage implements Parcelable{

    private ArrayList<Task> tasks;

    public TaskStorage(){
        tasks = new ArrayList<>();
    }


    public TaskStorage(Parcel in) {
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in){
        tasks = in.readArrayList(null);
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
    }



    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task){
        tasks.add(task);
        System.out.println("y");
    }



}
