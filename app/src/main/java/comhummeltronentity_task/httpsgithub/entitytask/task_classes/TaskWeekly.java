package comhummeltronentity_task.httpsgithub.entitytask.task_classes;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Created by Meerlu on 04.05.2018.
 */

@SuppressLint("ParcelCreator")
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

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<TaskWeekly> CREATOR = new Parcelable.Creator<TaskWeekly>() {
        public TaskWeekly createFromParcel(Parcel in) {
            return new TaskWeekly(in);
        }

        public TaskWeekly[] newArray(int size) {
            return new TaskWeekly[size];
        }
    };

    private TaskWeekly(Parcel in) {
        title = in.readString();
        description = in.readString();
        reminder = in.readByte() != 0;     //myBoolean == true if byte != 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            time = LocalTime.parse(in.readString());
        }
        days = (Boolean[]) in.readArray(Boolean.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.title);
        parcel.writeString(this.description);
        parcel.writeByte((byte) (this.reminder ? 1 : 0)); //reminder(bool) -> byte (in parcel
        parcel.writeString(this.time.toString());         //time(LocalTime) -> string (in parcel)
        parcel.writeArray(days);
    }

    @Override
    public ArrayList<LocalDate> getDates() {
        return null;
    }
}
