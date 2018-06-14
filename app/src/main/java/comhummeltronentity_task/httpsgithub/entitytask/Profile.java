package comhummeltronentity_task.httpsgithub.entitytask;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Meerlu on 12.06.2018.
 *
 *
 * das ist das profil des users, es wird im taskstorage erzeugt, da dieses objekt eh schon einmal durch alle activities geleitet wird
 * dadurch hat das ganze auch nähe zu den tasks, mit denen interagiert wird.
 *
 * TODO MAKE PARCELABLE
 * TODO INTERACT WITH TASKS
 */

public class Profile implements Parcelable, Serializable{

    //public static final int MAX_LEVEL = 100;
    public static final int POINT_LIMIT = 100;

    private int points;
    private int level;

    public Profile() {
        this.points = 0;
        this.level = 0;
    }

    public void increasePoints(int xp){
        this.points += xp;
        checkPoints();
    }

    private void checkPoints(){
        if (points >= POINT_LIMIT){
            level++;
            points = POINT_LIMIT - points;
        }
    }

    //get&set
    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    //****************************************************************************************************
    //Parcelable-Methoden, benötigt um Objekte zwischen Aktivites zu übergeben
    @Override
    public int describeContents() {
        return 0;
    }

    protected Profile(Parcel in) {
        points = in.readInt();
        level = in.readInt();
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(points);
        parcel.writeInt(level);
    }
}
