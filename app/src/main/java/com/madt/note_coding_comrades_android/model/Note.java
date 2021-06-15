package com.madt.note_coding_comrades_android.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Blob;

@Entity(tableName = "note")
public class Note {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id")
    private int noteId;

    public int getNoteCategoryId() {
        return noteCategoryId;
    }

    @NonNull
    @ColumnInfo(name = "note_category_id")
    private int noteCategoryId;

    @NonNull
    @ColumnInfo(name = "note_name")
    private String noteName;

    @NonNull
    @ColumnInfo(name = "note_detail")
    private String noteDetail;

/*

    @ColumnInfo(name = "note_image")
    private Blob noteImage;

    @ColumnInfo(name = "note_recording")
    private Blob noteRecording;
*/

   /* public Note(int noteId, int noteCategoryId, @NonNull String noteName, @NonNull String noteDetail, Blob noteImage, Blob noteRecording) {
        this.noteId = noteId;
        this.noteCategoryId = noteCategoryId;
        this.noteName = noteName;
        this.noteDetail = noteDetail;
       *//* this.noteImage = noteImage;
        this.noteRecording = noteRecording;*//*
    }*/
  public Note(int noteId, int noteCategoryId, @NonNull String noteName, @NonNull String noteDetail) {
        this.noteId = noteId;
        this.noteCategoryId = noteCategoryId;
        this.noteName = noteName;
        this.noteDetail = noteDetail;
       /* this.noteImage = noteImage;
        this.noteRecording = noteRecording;*/
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public int getCatId() {
        return noteCategoryId;
    }

    public void setCatId(int catId) {
        this.noteCategoryId = catId;
    }

    @NonNull
    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(@NonNull String noteName) {
        this.noteName = noteName;
    }

    @NonNull
    public String getNoteDetail() {
        return noteDetail;
    }

    public void setNoteDetail(@NonNull String noteDetail) {
        this.noteDetail = noteDetail;
    }

   /* public Blob getNoteImage() {
        return noteImage;
    }

    public void setNoteImage(Blob noteImage) {
        this.noteImage = noteImage;
    }

    public Blob getNoteRecording() {
        return noteRecording;
    }

    public void setNoteRecording(Blob noteRecording) {
        this.noteRecording = noteRecording;
    }*/
}
