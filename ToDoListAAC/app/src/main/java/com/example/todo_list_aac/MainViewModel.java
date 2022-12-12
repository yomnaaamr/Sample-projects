package com.example.todo_list_aac;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.todo_list_aac.database.AppDatabase;
import com.example.todo_list_aac.database.TaskEntry;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<TaskEntry>> tasks;
    private final static String TAG = MainViewModel.class.getSimpleName();

    public MainViewModel(@NonNull Application application) {
        super(application);

        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG,"Actively retrieving the tasks from database");
        tasks = database.taskDao().loadAllTasks();

    }

    public LiveData<List<TaskEntry>> getTasks(){
        return tasks;
    }
}
