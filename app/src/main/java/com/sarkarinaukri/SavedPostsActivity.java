package com.sarkarinaukri;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sarkarinaukri.adapter.JobsAdapter;
import com.sarkarinaukri.helperClass.Constant;
import com.sarkarinaukri.helperClass.HelperClass;
import com.sarkarinaukri.model.PostData;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SavedPostsActivity extends AppCompatActivity {

    JobsAdapter mAdapter;
    private ListView jobsListView;
    private static ActionMode mActionMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_posts);
        jobsListView = findViewById(R.id.list);


        mAdapter = new JobsAdapter(this, R.layout.data_list_item, new ArrayList<PostData>(10));
        jobsListView.setAdapter(mAdapter);

        final Type listOfObjects = new TypeToken<List<PostData>>() {
        }.getType();
        ArrayList<PostData> retrievedList;

        final SharedPreferences myPrefs = getSharedPreferences(Constant.SAVED_POSTS_PREF_NAME, Context.MODE_PRIVATE);
        final String json = myPrefs.getString("PostDatasList", "");
        final Gson gson = new Gson();
        retrievedList = gson.fromJson(json, listOfObjects);

        if (retrievedList != null && !retrievedList.isEmpty()) {
            mAdapter.addAll(retrievedList);
            mAdapter.notifyDataSetChanged();
        }


        jobsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PostData job = mAdapter.getItem(i);
                HelperClass.openOrSaveUrlInChromeCustomTab(SavedPostsActivity.this, job.getTitle(), job.getPostUrl());
            }
        });


        //Code for deleting Selected Items from List

        jobsListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        final ArrayList<PostData> finalRetrievedList = retrievedList;
        jobsListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                final int checkedCount = jobsListView.getCheckedItemCount();
                mode.setTitle(checkedCount + " Selected");
                mAdapter.toggleSelection(position);
            }

            @Override
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        SparseBooleanArray selected = mAdapter.getSelectedIds();

                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                PostData selecteditem = mAdapter.getItem(selected.keyAt(i));
                                mAdapter.remove(selecteditem);
                                if (finalRetrievedList != null) {
                                    finalRetrievedList.remove(selecteditem);
                                }
                                String strObject = gson.toJson(finalRetrievedList, listOfObjects);
                                SharedPreferences.Editor prefsEditor = myPrefs.edit();
                                prefsEditor.putString("PostDatasList", strObject);
                                prefsEditor.apply();

                            }
                        }
                        mode.finish();
                        Toast.makeText(getApplicationContext(), "Deleted Successfully!", Toast.LENGTH_SHORT).show();


                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.saved_jobs_menu_delete, menu);
                mActionMode = mode;
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                mAdapter.removeSelection();
                mActionMode = null;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }


        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_delete_job_help:
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Do you know ?");
                alertDialog.setMessage("You can select and delete jobs by long pressing and selecting them.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Great!",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.saved_jobs_menu_help, menu);
        return true;
    }


}
