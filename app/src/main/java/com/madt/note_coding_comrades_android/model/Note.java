package com.madt.note_coding_comrades_android.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;


@Entity(tableName = "note", foreignKeys = @ForeignKey(entity = Category.class,
        parentColumns = "cat_id",
        childColumns = "noteCategoryId",
        onDelete = CASCADE))
public class Note {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id")
    private int noteId;



    private int noteCategoryId;

    @NonNull
    @ColumnInfo(name = "note_name")
    private String noteName;

    @NonNull
    @ColumnInfo(name = "note_detail")
    private String noteDetail;

    @ColumnInfo(name = "note_image", typeAffinity = ColumnInfo.BLOB)
    private byte[] noteImage;
/*
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
  /*public Note(int noteId, int noteCategoryId, @NonNull String noteName, @NonNull String noteDetail) {
        this.noteId = noteId;
        this.noteCategoryId = noteCategoryId;
        this.noteName = noteName;
        this.noteDetail = noteDetail;
       *//* this.noteImage = noteImage;
        this.noteRecording = noteRecording;*//*
    }*/

    public Note(@NonNull String noteName, @NonNull String noteDetail, int noteCategoryId, byte[] noteImage) {
        this.noteName = noteName;
        this.noteDetail = noteDetail;
        this.noteCategoryId=noteCategoryId;
        this.noteImage = noteImage;
        /*this.noteRecording = noteRecording;*/
    }

    public int getNoteCategoryId() {
        return noteCategoryId;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public void setNoteCategoryId(int noteCategoryId) {
        this.noteCategoryId = noteCategoryId;
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

    public byte[] getNoteImage() {
        return noteImage;
    }

    public void setNoteImage(byte[] noteImage) {
        this.noteImage = noteImage;
    }

   /* public Blob getNoteRecording() {
        return noteRecording;
    }

    public void setNoteRecording(Blob noteRecording) {
        this.noteRecording = noteRecording;
    }*/
}
