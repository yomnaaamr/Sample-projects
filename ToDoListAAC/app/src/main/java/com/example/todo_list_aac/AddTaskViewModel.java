package com.example.todo_list_aac;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.todo_list_aac.database.AppDatabase;
import com.example.todo_list_aac.database.TaskEntry;

public class AddTaskViewModel extends ViewModel {

    private LiveData<TaskEntry> task;

    public LiveData<TaskEntry> getTask(){
        return task;
    }

    public AddTaskViewModel(AppDatabase database, int taskId){

        task = database.taskDao().loadTaskById(taskId);
    }
}
