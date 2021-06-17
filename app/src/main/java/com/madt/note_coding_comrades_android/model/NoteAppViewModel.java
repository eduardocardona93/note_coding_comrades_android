package com.madt.note_coding_comrades_android.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.madt.note_coding_comrades_android.data.NoteAppRepository;

import java.util.List;

public class NoteAppViewModel extends AndroidViewModel {
    private NoteAppRepository repository;
    private final LiveData<List<Category>> allCategories;
    private final LiveData<List<Note>> allNotes;

    public NoteAppViewModel(@NonNull Application application) {
        super(application);

        repository = new NoteAppRepository(application);
        allCategories = repository.getAllCategories();
        allNotes = repository.getAllNotes();
    }

    public LiveData<List<Category>> getAllCategories() {return allCategories;}
    public LiveData<List<Note>> getAllNotes() {return allNotes;}
    public void insertCategory(Category category){repository.insertCategory(category);}
    public void inertNote(Note contact) {repository.insertNote(contact);}


}