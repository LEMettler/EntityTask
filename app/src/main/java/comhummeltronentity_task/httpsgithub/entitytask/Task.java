package comhummeltronentity_task.httpsgithub.entitytask;

import java.time.LocalTime;

/**
 * Created by Meerlu on 03.05.2018.
 */

public abstract class Task{
    /**
     * Klasse zum Speichern einzelner Tasks, deren objekte dann gespeichert werden können
     * TODO tasks im system speichern
     */
    protected String title;
    protected String description;
    protected LocalTime time;
    protected Boolean reminder;

    //Wenn der Button zum Speichern gedrückt wird wird das Objekt erzeugt und die Atrribute übern constructor übergeben
    public Task(String title, String description, Boolean reminder, LocalTime time) {
        this.title = title;
        this.description = description;
        this.reminder = reminder;
        this.time = time;
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
}
