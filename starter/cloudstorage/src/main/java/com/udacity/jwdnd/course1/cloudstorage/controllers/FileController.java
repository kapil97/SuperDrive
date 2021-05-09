package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.forms.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.forms.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.forms.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

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
    private final CredentialService credentialService;

    public FileController(FileService fileService, UserService userService, NoteService noteService, CredentialService credentialService) {
        this.fileService = fileService;
        this.userService = userService;
        this.noteService = noteService;
        this.credentialService = credentialService;
    }

    @GetMapping
    public String homeView(Authentication authentication, @ModelAttribute("newFile") FileForm newFile,
                           @ModelAttribute("newNote") NoteForm newNote, @ModelAttribute("newCredential") CredentialForm newCredential,
                           Model model){
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        model.addAttribute("files", fileService.getALlFileNames(user.getUserId()));
        System.out.println("username: "+ userName);
        return "home";
    }

    @PostMapping
    public String fileSubmit(Authentication authentication, @ModelAttribute("newFile") FileForm newFile,
                             @ModelAttribute("newNote") NoteForm newNote, @ModelAttribute("newCredential") CredentialForm newCredential,
                             Model model){
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        Integer userId = user.getUserId();
        List<String> fileNames = fileService.getALlFileNames(userId);
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
        model.addAttribute("files", fileService.getALlFileNames(userId));
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
        model.addAttribute("files", fileService.getALlFileNames(userId));
        model.addAttribute("result", "success");
        return "result";
    }





}
