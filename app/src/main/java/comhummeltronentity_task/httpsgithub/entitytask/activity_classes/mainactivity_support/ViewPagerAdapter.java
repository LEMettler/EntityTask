package comhummeltronentity_task.httpsgithub.entitytask.activity_classes.mainactivity_support;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import comhummeltronentity_task.httpsgithub.entitytask.R;
import comhummeltronentity_task.httpsgithub.entitytask.activity_classes.MainActivity;
import comhummeltronentity_task.httpsgithub.entitytask.task_classes.Task;

/**
 * Created by Meerlu on 24.05.2018.
 *
 *
 * builds den slider()viewPager in der CalendarActivity
 *
 * DONE nur die tasks des tags X
 * todo description anzeigen?
 * todo zeit anzeigen und nach dieser ordnen
 */

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Task> taskList;
    private MainActivity main;

    public ViewPagerAdapter(Context context, ArrayList<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
        main = (MainActivity) context;
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    //initializiert ein item der liste
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.fragment_taskslider, null);

            TextView txtTitle = view.findViewById(R.id.txtTitle);
            String title = taskList.get(position).getTitle();     //genau das nochmal nur für description, wenn wir noch ne description wollen
            txtTitle.setText(title);                              // + xml file

        Button btnDone = view.findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                main.setTaskDone(taskList, position);
                //DONE hier code um task done zu setzen
            }
        });

            ViewPager viewPager = (ViewPager) container;
            viewPager.addView(view, 0);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    main.gotoSpecificTask(taskList.get(position));
                }
            });

            return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}
