package com.udacity.jwdnd.course1.fileManager.services;

import com.udacity.jwdnd.course1.fileManager.mappers.CredentialMapper;
import com.udacity.jwdnd.course1.fileManager.mappers.UserMapper;
import com.udacity.jwdnd.course1.fileManager.model.Credential;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {
    private CredentialMapper credentialMapper;

    public CredentialService(UserMapper userMapper, CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }

    public List<Credential> getAllCredentials(Integer userId){
        return credentialMapper.getAllCredentials(userId);
    }

    public void deleteCredential(Integer credId){
        credentialMapper.deleteCredential(credId);
    }

    public Credential getCredential(Integer credId){
        return credentialMapper.getCredential(credId);
    }

    public void addCredential(Integer userId, String url, String username, String password, String key ){
        Credential credential = new Credential(null, url, username, key, password, userId);
        credentialMapper.addCredential(credential);
    }

    public void updateCredential(Integer credentialId, String newUserName, String url, String key, String password){
        credentialMapper.updateCredential(credentialId,newUserName,url,key,password);
    }

}
