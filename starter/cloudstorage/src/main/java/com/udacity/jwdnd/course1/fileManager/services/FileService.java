package com.udacity.jwdnd.course1.fileManager.services;

import com.udacity.jwdnd.course1.fileManager.mappers.FileMapper;
import com.udacity.jwdnd.course1.fileManager.mappers.UserMapper;
import com.udacity.jwdnd.course1.fileManager.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class FileService {
    private final UserMapper userMapper;
    private final FileMapper fileMapper;

    public FileService(UserMapper userMapper, FileMapper fileMapper) {
        this.userMapper = userMapper;
        this.fileMapper = fileMapper;
    }
    public List<String> getAllFileNames(Integer userId){
        return fileMapper.getAllFileNames(userId);
    }
    public File getFile(String fileName) {
        return fileMapper.getFile(fileName);
    }

    public void addFile(MultipartFile multipartFile, String userName){
        InputStream fis = null;
        try {
            fis = multipartFile.getInputStream();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead=0;
        byte[] data = new byte[1024];
        while (true) {
            try {
                if (!((nRead = fis.read(data, 0, data.length)) != -1)) break;
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            buffer.write(data, 0, nRead);
        }
        try {
            buffer.flush();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        byte[] fileData = buffer.toByteArray();

        String fileName = multipartFile.getOriginalFilename();
        String contentType = multipartFile.getContentType();
        String fileSize = String.valueOf(multipartFile.getSize());
        Integer userId = userMapper.getUser(userName).getUserId();

        File file = new File(null, fileName, contentType, fileSize, userId, fileData);
        fileMapper.insertFile(file);
        return;
    }

    public void deleteFile(String fileName) {
        fileMapper.deleteFile(fileName);
    }

}
