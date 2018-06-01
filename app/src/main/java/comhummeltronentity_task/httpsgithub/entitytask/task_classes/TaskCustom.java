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
        //this.dateDone = new Boolean[dates.size()];
        //Arrays.fill(dateDone,false);
    }



    //getter & setter
    public ArrayList<LocalDate> getDates() {
        return dates;
    }

    public void setDates(ArrayList<LocalDate> dates) {
        this.dates = dates;
    }


    public static final Parcelable.Creator<TaskCustom> CREATOR = new Parcelable.Creator<TaskCustom>() {
        public TaskCustom createFromParcel(Parcel in) {
            return new TaskCustom(in);
        }

        public TaskCustom[] newArray(int size) {
            return new TaskCustom[size];
        }
    };

    private TaskCustom(Parcel in) {
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
      parcel.writeString(this.title);
      parcel.writeString(this.description);
      parcel.writeByte((byte) (this.reminder ? 1 : 0)); //reminder(bool) -> byte (in parcel
      parcel.writeString(this.time.toString());         //time(LocalTime) -> string (in parcel)
      parcel.writeList(this.dates);     // macht vlcht probleme, dann liste ->string und dann probieren
      //parcel.writeArray(dateDone);
    }

}
