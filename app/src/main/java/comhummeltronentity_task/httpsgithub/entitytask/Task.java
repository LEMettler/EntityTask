package comhummeltronentity_task.httpsgithub.entitytask;

/**
 * Created by Meerlu on 03.05.2018.
 */

public class Task {
    /**
     * Klasse zum Speichern einzelner Tasks, deren objekte dann gespeichert werden können
     * TODO tasks im system speichern
     */
    private String title;
    private String description;

    //Wenn der Button zum Speichern gedrückt wird wird das Objekt erzeugt und die Atrribute übern constructor übergeben
    public Task(String title, String description) {
        this.title = title;
        this.description = description;
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
}
