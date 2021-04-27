package com.example.mynote.ui.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mynote.models.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM task ORDER by id DESC")
    List<Task> getAll();

    @Query("SELECT * FROM task")
    LiveData<List<Task>> getAllAlive();

    @Query("SELECT * FROM task ORDER BY title ASC ")
    List<Task> sortByASC();

    @Query("SELECT * FROM task ORDER BY title DESC ")
    List<Task> sortByDESC();

    @Query("SELECT * FROM task ORDER BY updatedAt DESC")
    List<Task> sortByEditedTime();


    @Query("DELETE FROM task")
    void deleteAll();

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);



}
