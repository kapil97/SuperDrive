package com.udacity.jwdnd.course1.fileManager.forms;

public class NoteForm {
    String noteId, noteTitle, noteDescription;

    public NoteForm(String noteId, String noteTitle, String noteDescription) {
        this.noteId = noteId;
        this.noteTitle = noteTitle;
        this.noteDescription = noteDescription;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getNoteDescription() {
        return noteDescription;
    }

    public void setNoteDescription(String noteDescription) {
        this.noteDescription = noteDescription;
    }

    @Override
    public String toString() {
        return "NoteForm{" +
                "noteTitle='" + noteTitle + '\'' +
                ", noteDescription='" + noteDescription + '\'' +
                '}';
    }
}
