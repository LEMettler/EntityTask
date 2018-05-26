package comhummeltronentity_task.httpsgithub.entitytask.activity_classes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import comhummeltronentity_task.httpsgithub.entitytask.R;
import comhummeltronentity_task.httpsgithub.entitytask.activity_classes.taskactivity_support.SectionsPageAdapter;
import comhummeltronentity_task.httpsgithub.entitytask.activity_classes.taskactivity_support.TaskviewFragment;
import comhummeltronentity_task.httpsgithub.entitytask.task_classes.Task;
import comhummeltronentity_task.httpsgithub.entitytask.task_classes.TaskStorage;

public class TasksActivity extends AppCompatActivity {
    /**
     * Das Ist die Activity die die Übersicht über die einzelnen bereits erzeugten Tasks anzeigt
     * Über den kleinen button (fap) kommt man zum taskcreator
     * <p>
     * TODO tasks anzeigen
     * TODO tasks löschen/erledigen
     */

    private static final String TAG = "TaskActivity";
    private static TaskStorage taskStorage;

    private SectionsPageAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;


    //setup of fragments (1 task -> 1 fragment)
    private void setupViewPager(ViewPager viewPager){

        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());

        for (Task t : taskStorage.getTasks()) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("TASK", t);
            TaskviewFragment fragment = new TaskviewFragment();
            fragment.setArguments(bundle);

            adapter.addFragment(fragment, t.getTitle());
        }
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //taskStorage = getIntent().getParcelableExtra("TASKSTORAGE");
        taskStorage = getIntent().getExtras().getParcelable("TASKSTORAGE");

        //Fragment aufsetzung des viewpagers
        mSectionsPagerAdapter =new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoTaskcreator(view);
            }
        });
    }


    //Link zu TaskCreator
    //  die Referenz zum TaskStorage wird mit übergeben über den Intent

    public void gotoTaskcreator(View view) {

        Intent intent = new Intent(this, TaskcreatorActivity.class);
        intent.putExtra("TASKSTORAGE", taskStorage);
        //startActivity(intent);
        startActivityForResult(intent, 1); //1 == successful
    }


    //nach schließen des TaskCreators wird der taskStorage erneuert und der sectionspageadapter der fragments neu geladen
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            //if (resultCode == RESULT_OK) {

            taskStorage = data.getExtras().getParcelable("TASKSTORAGE");
            //}
        }
        setupViewPager(mViewPager);
    }

    @Override
    public void finish() {
        //rückgabe des taskstorage an mainactivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("TASKSTORAGE", taskStorage);
        setResult(1, resultIntent);

        super.finish();
    }


    //erstellt die action bar oben, die die drei menüpunkte hat
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tasks, menu);
        return true;
    }

    //selected-handler bei auswahl eines menüpunktes
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            if (!taskStorage.getTasks().isEmpty()) {
                deleteDialog();                         //todo refresh nachdem ein task deletet wurde
                //setupViewPager(mViewPager);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //erstellt einen dialog zum deleten des tasks X
    private void deleteDialog(){
        new AlertDialog.Builder(this)
                .setTitle((CharSequence) taskStorage.getTasks().get(mViewPager.getCurrentItem()).getTitle())
                .setMessage("Delete?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        taskStorage.getTasks().remove(mViewPager.getCurrentItem());
                    }
                }).create().show();
    }


}

