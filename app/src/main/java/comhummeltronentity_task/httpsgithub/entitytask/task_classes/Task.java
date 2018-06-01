package comhummeltronentity_task.httpsgithub.entitytask.task_classes;

import android.os.Parcelable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Created by Meerlu on 03.05.2018.
 */

public abstract class Task implements Parcelable {
    /**
     * Blueprint für alle spezifischen Tasks
     * dateDone stellt für jeden date dar, ob dieser done ist oder nicht, je nach subtask wird dieser resetet, oder der task gelöscht, wenn alle erledigt
     *
     *
     * TODO tasks im system speichern
     */
    protected String title;
    protected String description;
    protected LocalTime time;
    protected Boolean reminder;

    //super der Subclasses
    public Task(String title, String description, Boolean reminder, LocalTime time) {
        this.title = title;
        this.description = description;
        this.reminder = reminder;
        this.time = time;
    }

    protected Task() {
    }

    //Getter & Setter
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Boolean getReminder() {
        return reminder;
    }

    public void setReminder(Boolean reminder) {
        this.reminder = reminder;
    }

    public abstract ArrayList<LocalDate> getDates();
}
