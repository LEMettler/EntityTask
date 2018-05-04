package comhummeltronentity_task.httpsgithub.entitytask;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Created by Meerlu on 04.05.2018.
 */

public class TaskMonthly extends Task {

    /**
     * sub für monatliche tasks, auch hier gibt es eine liste, so können auch mehrere tage monthly wiederholt werden
     * @param title
     * @param description
     * @param time
     */
    private ArrayList<LocalDate> dates = new ArrayList<>();

    public TaskMonthly(String title, String description, LocalTime time, ArrayList<LocalDate> dates) {
        super(title, description, time);

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
