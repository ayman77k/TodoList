package com.codelab.todolist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.task_title)
    EditText taskTitle;

    TasksAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String tasksJSON = preferences.getString("TASKS", "[]");

        ArrayList<Task> savedTasks = new Gson().fromJson(tasksJSON, new TypeToken<ArrayList<Task>>() {
        }.getType());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TasksAdapter(this, savedTasks);
        recyclerView.setAdapter(adapter);
    }

    public void addTask(View view) {

        String taskTitle = this.taskTitle.getText().toString();

        if (!taskTitle.isEmpty()) {
            adapter.addTask(new Task(taskTitle));
//            ((TasksAdapter)recyclerView.getAdapter()).addTask(new Task(taskTitle));
        } else
            Toast.makeText(this, "Can't create an empty task!", Toast.LENGTH_SHORT).show();


    }

    @Override
    protected void onDestroy() {
        ArrayList<Task> tasks = adapter.getItems();

        Gson gson = new Gson();
        String tasksJSON = gson.toJson(tasks);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("TASKS", tasksJSON);
        editor.apply();

        super.onDestroy();
    }
}
