package com.udacity.jwdnd.course1.fileManager.controllers;

import com.udacity.jwdnd.course1.fileManager.forms.CredentialForm;
import com.udacity.jwdnd.course1.fileManager.forms.FileForm;
import com.udacity.jwdnd.course1.fileManager.forms.NoteForm;
import com.udacity.jwdnd.course1.fileManager.model.File;
import com.udacity.jwdnd.course1.fileManager.model.User;
import com.udacity.jwdnd.course1.fileManager.services.*;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/home")
public class FileController {

    private final FileService fileService;
    private final UserService userService;
    private final NoteService noteService;
    private final EncryptionService encryptionService;
    private final CredentialService credentialService;

    public FileController(FileService fileService, UserService userService, NoteService noteService, EncryptionService encryptionService, CredentialService credentialService) {
        this.fileService = fileService;
        this.userService = userService;
        this.noteService = noteService;
        this.encryptionService = encryptionService;
        this.credentialService = credentialService;
    }

    @GetMapping
    public String homeView(Authentication authentication, @ModelAttribute("newFile") FileForm newFile,
                           @ModelAttribute("newNote") NoteForm newNote, @ModelAttribute("newCredential") CredentialForm newCredential,
                           Model model){
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        model.addAttribute("files", fileService.getAllFileNames(user.getUserId()));
        model.addAttribute("notes", noteService.getUserNotes(user.getUserId()));
        model.addAttribute("credentials", credentialService.getAllCredentials(user.getUserId()));
        model.addAttribute("encryptionService", encryptionService);
        return "home";
    }

    @PostMapping
    public String fileSubmit(Authentication authentication, @ModelAttribute("newFile") FileForm newFile,
                             @ModelAttribute("newNote") NoteForm newNote, @ModelAttribute("newCredential") CredentialForm newCredential,
                             Model model){
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        Integer userId = user.getUserId();
        List<String> fileNames = fileService.getAllFileNames(userId);
        MultipartFile multipartFile = newFile.getFile();
        String fileName = multipartFile.getOriginalFilename();
        System.out.println("FileName: "+fileName);
        if(fileName.isBlank()) {
            System.out.println("Empty File");
            model.addAttribute("result","error");
            model.addAttribute("message", "No File Selected!");
            return "result";
        }
        boolean isDuplicatePresent = fileNames.contains(fileName);
        if (!isDuplicatePresent) {
            fileService.addFile(multipartFile, userName);
            model.addAttribute("result", "success");
        } else {
            model.addAttribute("result", "error");
            model.addAttribute("message", "File with the same name already exists, upload a different file!");
        }
        model.addAttribute("files", fileService.getAllFileNames(userId));
        return "result";
    }

    @GetMapping(
            value = "/get-file/{fileName}",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public @ResponseBody byte[] getFile(@PathVariable String fileName) {

        File file = fileService.getFile(fileName);
        System.out.println("Fileid: "+ file.getFileName()+" fileSize: "+file.getFileSize());
        return fileService.getFile(fileName).getFileData();
    }

    @GetMapping(value = "/delete-file/{fileName}")
    public String deleteFile(
            Authentication authentication, @PathVariable String fileName, @ModelAttribute("newFile") FileForm newFile,
            @ModelAttribute("newNote") NoteForm newNote, @ModelAttribute("newCredential") CredentialForm newCredential,
            Model model) {
        fileService.deleteFile(fileName);
        Integer userId = userService.getUser((authentication).getName()).getUserId();
        model.addAttribute("files", fileService.getAllFileNames(userId));
        model.addAttribute("result", "success");
        return "result";
    }





}
