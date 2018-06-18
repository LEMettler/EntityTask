package comhummeltronentity_task.httpsgithub.entitytask.activity_classes.mainactivity_support;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;

/**
 *
 * nur für ne hübsche animation der progressbar
 *
 * Created by Meerlu on 18.06.2018.
 */

public class ProgressBarAnimation extends Animation {
    private ProgressBar progressBar;
    private float from;
    private float  to;

    public ProgressBarAnimation(ProgressBar progressBar, float from, float to) {
        super();
        this.progressBar = progressBar;
        this.from = from;
        this.to = to;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float value = from + (to - from) * interpolatedTime;
        progressBar.setProgress((int) value);
    }

}
