package com.madt.note_coding_comrades_android.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.madt.note_coding_comrades_android.model.Category;
import com.madt.note_coding_comrades_android.model.CategoryHavingNotes;
import com.madt.note_coding_comrades_android.model.Note;

import java.util.List;

@Dao
public interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertCategory(Category category);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertNote(Note note);

    @Query("SELECT * FROM category")
    LiveData<List<Category>> getAllCategories();

    @Transaction
    @Query("SELECT * FROM category")
    public List<CategoryHavingNotes> getNotesForCategory();

    @Query("DELETE FROM note")
    void deleteAll();

    @Query("DELETE FROM note")
    void deleteAllNotes();


}
