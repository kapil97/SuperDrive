package com.udacity.jwdnd.course1.fileManager.controllers;

import com.udacity.jwdnd.course1.fileManager.forms.CredentialForm;
import com.udacity.jwdnd.course1.fileManager.forms.FileForm;
import com.udacity.jwdnd.course1.fileManager.forms.NoteForm;
import com.udacity.jwdnd.course1.fileManager.model.Note;
import com.udacity.jwdnd.course1.fileManager.model.User;
import com.udacity.jwdnd.course1.fileManager.services.NoteService;
import com.udacity.jwdnd.course1.fileManager.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("note")
public class NotesController {
    NoteService noteService;
    UserService userService;

    public NotesController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }
    @GetMapping()
    public String noteView(Authentication authentication, @ModelAttribute("newFile") FileForm newFile,
                           @ModelAttribute("newNote") NoteForm newNote, @ModelAttribute("newCredential") CredentialForm newCredential,
                           Model model){
        String username = authentication.getName();
        User user = userService.getUser(username);
        Integer userId = user.getUserId();
        System.out.println("Inside NoteController");
        model.addAttribute("notes",noteService.getUserNotes(userId));
        return  "home";
    }
    @PostMapping("add-note")
    public String onNoteSubmit(Authentication authentication, @ModelAttribute("newFile") FileForm newFile,
                               @ModelAttribute("newNote") NoteForm newNote, @ModelAttribute("newCredential") CredentialForm newCredential,
                               Model model){
        String username = authentication.getName();
        User user = userService.getUser(username);
        Integer userId = user.getUserId();
        String noteId = newNote.getNoteId();
        if(noteId.isEmpty()){
            noteService.addNote(newNote.getNoteTitle(), newNote.getNoteDescription(), userId);
        }
        else{
            Note note = getNote(Integer.parseInt(noteId));
            noteService.updateNote(note.getNoteId(),newNote.getNoteTitle(),newNote.getNoteDescription());
        }
        model.addAttribute("notes", noteService.getUserNotes(userId));
        model.addAttribute("result", "success");
        return "result";
    }
    @GetMapping(value = "/get-note/{noteId}")
    public Note getNote(@PathVariable Integer noteId) {
        return noteService.getNote(noteId);
    }

    @GetMapping(value = "/delete-note/{noteId}")
    public String deleteNote(
            Authentication authentication, @PathVariable Integer noteId, @ModelAttribute("newNote") NoteForm newNote,
            @ModelAttribute("newFile") FileForm newFile, @ModelAttribute("newCredential") CredentialForm newCredential,
            Model model) {
        noteService.deleteNote(noteId);
        String username = authentication.getName();
        User user = userService.getUser(username);
        Integer userId = user.getUserId();
        model.addAttribute("notes", noteService.getUserNotes(userId));
        return "result";
    }
}
