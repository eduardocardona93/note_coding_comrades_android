package com.madt.note_coding_comrades_android.data;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface NoteDao {

    @Query("DELETE FROM note")
    void deleteAllNotes();
}
