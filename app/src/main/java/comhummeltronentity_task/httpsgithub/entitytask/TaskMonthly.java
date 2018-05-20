package comhummeltronentity_task.httpsgithub.entitytask;

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
public class TaskMonthly extends Task {

    /**
     * sub für monatliche tasks, auch hier gibt es eine liste, so können auch mehrere tage monthly wiederholt werden
     * @param title
     * @param description
     * @param time
     */
    private ArrayList<LocalDate> dates = new ArrayList<>();

    public TaskMonthly(String title, String description, Boolean reminder, LocalTime time, ArrayList<LocalDate> dates) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<TaskMonthly> CREATOR = new Parcelable.Creator<TaskMonthly>() {
        public TaskMonthly createFromParcel(Parcel in) {
            return new TaskMonthly(in);
        }

        public TaskMonthly[] newArray(int size) {
            return new TaskMonthly[size];
        }
    };

    private TaskMonthly(Parcel in) {
        title = in.readString();
        description = in.readString();
        reminder = in.readByte() != 0;     //myBoolean == true if byte != 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            time = LocalTime.parse(in.readString());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dates = in.readArrayList(LocalDate.class.getClassLoader());
        }
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.title);
        parcel.writeString(this.description);
        parcel.writeByte((byte) (this.reminder ? 1 : 0)); //reminder(bool) -> byte (in parcel
        parcel.writeString(this.time.toString());         //time(LocalTime) -> string (in parcel)
        parcel.writeList(this.dates);     //todo macht vlcht probleme, dann liste ->string und dann probieren
    }
}
