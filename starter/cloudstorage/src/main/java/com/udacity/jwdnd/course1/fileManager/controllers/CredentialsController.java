package com.udacity.jwdnd.course1.fileManager.controllers;

import com.udacity.jwdnd.course1.fileManager.forms.CredentialForm;
import com.udacity.jwdnd.course1.fileManager.forms.FileForm;
import com.udacity.jwdnd.course1.fileManager.forms.NoteForm;
import com.udacity.jwdnd.course1.fileManager.model.Credential;
import com.udacity.jwdnd.course1.fileManager.model.User;
import com.udacity.jwdnd.course1.fileManager.services.CredentialService;
import com.udacity.jwdnd.course1.fileManager.services.EncryptionService;
import com.udacity.jwdnd.course1.fileManager.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/credential")
public class CredentialsController {
    private UserService userService;
    private CredentialService credentialService;
    private EncryptionService encryptionService;

    public CredentialsController(UserService userService, CredentialService credentialService, EncryptionService encryptionService) {
        this.userService = userService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @GetMapping
    public String getHomePage(
            Authentication authentication, @ModelAttribute("newFile") FileForm newFile,
            @ModelAttribute("newCredential") CredentialForm newCredential,
            @ModelAttribute("newNote") NoteForm newNote, Model model) {
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        model.addAttribute("credentials", credentialService.getAllCredentials(user.getUserId()));
        model.addAttribute("encryptionService", encryptionService);
        return "home";
    }

    @PostMapping("add-credential")
    public String onSubmitCredentials(Authentication authentication, @ModelAttribute("newFile") FileForm newFile,
                                      @ModelAttribute("newNote") NoteForm newNote, @ModelAttribute("newCredential") CredentialForm newCredential,
                                      Model model){
        String username = authentication.getName();
        User user = userService.getUser(username);
        Integer userId = user.getUserId();
        System.out.println("Username in Cred: " + username);
        String url = newCredential.getUrl();

        String credId = newCredential.getCredentialId();
        String password = newCredential.getPassword();


        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(password, encodedKey);

        if(credId.isEmpty()){
            credentialService.addCredential(userId, url, newCredential.getUsername(), encryptedPassword, encodedKey);
        }
        else{
            Credential credential = getCredential(Integer.parseInt(credId));
            credentialService.updateCredential(credential.getCredentialId(), newCredential.getUsername(),url,encodedKey,encryptedPassword);
        }
        model.addAttribute("credentials", credentialService.getAllCredentials(userId));
        List<Credential> credentialList = credentialService.getAllCredentials(userId);
        System.out.println(credentialList);
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("result","success");
        return "result";
    }
    @GetMapping(value = "/get-credential/{credentialId}")
    public Credential getCredential(@PathVariable Integer credentialId) {
        return credentialService.getCredential(credentialId);
    }
    @GetMapping(value = "/delete-credential/{credentialId}")
    public String deleteCredential(
            Authentication authentication, @PathVariable Integer credentialId,
            @ModelAttribute("newCredential") CredentialForm newCredential,
            @ModelAttribute("newFile") FileForm newFile,
            @ModelAttribute("newNote") NoteForm newNote, Model model) {
        credentialService.deleteCredential(credentialId);
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        model.addAttribute("credentials", credentialService.getAllCredentials(user.getUserId()));
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("result", "success");
        return "home";
    }
}
