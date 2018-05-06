package comhummeltronentity_task.httpsgithub.entitytask;

import java.time.LocalTime;

/**
 * Created by Meerlu on 04.05.2018.
 */

public class TaskWeekly extends Task {

    /**
     * wöchentliche tasks, ist die ausnahme, denn ein array aus booleans (ein bool = ein tag) gibt an, ob die task an diesem tag gilt oder nicht
     * @param title
     * @param description
     * @param time
     */

    private Boolean[] days = new Boolean[7];

    public TaskWeekly(String title, String description, Boolean reminder, LocalTime time, Boolean[] days) {
        super(title, description,reminder, time);

        this.days = days;
    }
}
