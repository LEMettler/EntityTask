package comhummeltronentity_task.httpsgithub.entitytask;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Created by Meerlu on 04.05.2018.
 */

public class TaskCustom extends Task {
    /**
     * subklasse von task
     * die einzelnen tage, an denen der task gilt, werden als liste gespeichert und können durch diese abgerufen werden
     * @param title
     * @param description
     * @param time
     */

    private ArrayList<LocalDate> dates = new ArrayList<>();

    public TaskCustom(String title, String description, Boolean reminder, LocalTime time, ArrayList<LocalDate> dates) {
        super(title, description, reminder, time);

        this.dates = dates;
    }


    //getter & setter
    public ArrayList<LocalDate> getDates() {
        return dates;
    }

    public void setDates(ArrayList<LocalDate> dates) {
        this.dates = dates;
    }
}
