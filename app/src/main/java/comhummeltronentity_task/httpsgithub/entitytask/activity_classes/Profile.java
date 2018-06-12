package comhummeltronentity_task.httpsgithub.entitytask.activity_classes;

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

public class Profile {

    //public static final int MAX_LEVEL = 100;
    public static final int POINT_LIMIT = 100;

    private int points;
    private int level;

    public Profile() {
        this.points = 0;
        this.level = 0;
    }

    public void increasePoints(int xp){
        this.points += points;
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
}
