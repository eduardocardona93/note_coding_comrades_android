package com.madt.note_coding_comrades_android.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.madt.note_coding_comrades_android.model.Category;
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

    @Query("SELECT * FROM note")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM category WHERE cat_id <> :cateId")
    LiveData<List<Category>> getAllCategoriesBut(int cateId);


    @Query("SELECT * FROM note WHERE noteCategoryId = :cateId AND note_name LIKE  '%' || :searchKey || '%'  ORDER BY  " +
            "CASE  WHEN :isAsc = 1 THEN note_name END ASC, " +
            "CASE WHEN :isDesc = 1 THEN note_name END DESC, " +
            "CASE WHEN :byDate = 1 THEN note_name END DESC"
    )
    LiveData<List<Note>> getNotesForCategory(int cateId, boolean isAsc, boolean isDesc, String searchKey,boolean byDate);

    @Query("SELECT * FROM note WHERE noteCategoryId = :cateId AND note_name LIKE :searchKey")
    public List<Note> getNotesBySearch(int cateId, String searchKey);


    @Query("SELECT * FROM note WHERE noteCategoryId = :cateId")
    LiveData<List<Note>> getNotesForCategory(int cateId);
    @Query("DELETE FROM note")
    void deleteAll();

    @Query("DELETE FROM note")
    void deleteAllNotes();

    @Delete
    void deleteNote(Note note);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(Note note);

    @Query("SELECT * FROM note WHERE note_id = :id LIMIT 1")
    public LiveData<List<Note>> getNoteById(int id);
}
