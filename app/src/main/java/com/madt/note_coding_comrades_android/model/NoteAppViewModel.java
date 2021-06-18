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
    private LiveData<List<Note>> allNotes;

    public NoteAppViewModel(@NonNull Application application) {
        super(application);

        repository = new NoteAppRepository(application);
        allCategories = repository.getAllCategories();
        allNotes = repository.getAllNotes();
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    public LiveData<List<Category>> getAllCategoriesBut(int cateId) {
        return repository.getAllCategoriesBut(cateId);
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public LiveData<List<Note>> getNotesByCategory(int catId, boolean isAsc, boolean isDesc, String searchKey, boolean byDate) {
        allNotes = repository.getNotesByCategory(catId, isAsc, isDesc, searchKey, byDate);
        return allNotes;
    }

    public LiveData<List<Note>> getNotesByCategory(int catId) {
        allNotes = repository.getNotesByCategory(catId);
        return allNotes;
    }


    public void insertCategory(Category category) {
        repository.insertCategory(category);
    }

    public void insertNote(Note note) {
        repository.insertNote(note);
    }

    public void delete(Note note) {
        repository.delete(note);
    }

    public void update(Note note) {
        repository.update(note);
    }

    public void updateCategory(Category category) {
        repository.updateCategory(category);
    }

    public LiveData<Note> getNoteById(int id) {
        return repository.getNoteById(id);
    }
    public LiveData<Category> getCategoryById(int id) {
        return repository.getCategoryById(id);
    }
}
