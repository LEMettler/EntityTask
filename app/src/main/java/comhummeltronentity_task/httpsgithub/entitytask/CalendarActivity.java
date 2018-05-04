package comhummeltronentity_task.httpsgithub.entitytask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Hier wird der Kalender von Google implementiert. Hail Corporate.
 *
 * wird ein tag angeklickt sollen die tasks für diesen tag unterhalb erscheinen, am besten hübsch
 */

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
    }
}
