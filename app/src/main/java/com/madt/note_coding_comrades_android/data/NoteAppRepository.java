package com.madt.note_coding_comrades_android.data;

import android.app.Application;

import androidx.lifecycle.LiveData;


import com.madt.note_coding_comrades_android.model.Category;
import com.madt.note_coding_comrades_android.model.Note;
import com.madt.note_coding_comrades_android.utilities.NoteRoomDB;

import java.util.List;

public class NoteAppRepository {
    private NoteDao noteDao;
    private LiveData<List<Category>> allCategories;
    private LiveData<List<Note>> allNotes;

    public NoteAppRepository(Application application) {
        NoteRoomDB db = NoteRoomDB.getInstance(application);
        noteDao = db.noteDao();
        allCategories = noteDao.getAllCategories();
        allNotes = noteDao.getAllNotes();
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }


    public void insertCategory(Category category) {
        NoteRoomDB.databaseWriteExecutor.execute(() -> noteDao.insertCategory(category));
    }

    public void insertNote(Note contact) {
        NoteRoomDB.databaseWriteExecutor.execute(() -> noteDao.insertNote(contact));
    }


}














