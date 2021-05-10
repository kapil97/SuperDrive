package com.udacity.jwdnd.course1.fileManager.services;

import com.udacity.jwdnd.course1.fileManager.mappers.NoteMapper;
import com.udacity.jwdnd.course1.fileManager.mappers.UserMapper;
import com.udacity.jwdnd.course1.fileManager.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    NoteMapper noteMapper;
    UserMapper userMapper;

    public NoteService(NoteMapper noteMapper, UserMapper userMapper) {
        this.noteMapper = noteMapper;
        this.userMapper = userMapper;
    }
    public List<Note> getUserNotes(Integer userId){
        return noteMapper.getUserNotes(userId);
    }
    public void addNote(String title, String description, Integer userId){;
        noteMapper.insertNote(new Note(null, title, description, userId));
    }
    public Note getNote(Integer noteId){
       return noteMapper.getNote(noteId);
    }
    public void deleteNote(Integer noteId){
        noteMapper.deleteNote(noteId);
    }
    public void updateNote(Integer noteId, String title, String description){
        noteMapper.updateNote(noteId,title,description);
    }
}
