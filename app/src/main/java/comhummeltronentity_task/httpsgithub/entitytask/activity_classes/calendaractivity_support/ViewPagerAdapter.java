package comhummeltronentity_task.httpsgithub.entitytask.activity_classes.calendaractivity_support;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import comhummeltronentity_task.httpsgithub.entitytask.R;
import comhummeltronentity_task.httpsgithub.entitytask.task_classes.TaskStorage;

/**
 * Created by Meerlu on 24.05.2018.
 *
 *
 * builds den slider()viewPager in der CalendarActivity
 *
 * todo nur die tasks des tags X
 * todo description anzeigen?
 */

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private TaskStorage taskStorage;


    public ViewPagerAdapter(Context context, TaskStorage taskStorage) {
        this.context = context;
        this.taskStorage = taskStorage;
    }

    @Override
    public int getCount() {
        return taskStorage.getTasks().size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.fragment_taskslider, null);
        TextView txtTitle = view.findViewById(R.id.txtTitle);

        String title = taskStorage.getTasks().get(position).getTitle();
        txtTitle.setText(title);

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view,0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }

}
