package com.udacity.jwdnd.course1.fileManager.mappers;

import com.udacity.jwdnd.course1.fileManager.model.File;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Delete;


import java.util.List;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM FILES where filename = #{fileName}")
    File getFile(String fileName);

    @Insert("INSERT INTO FILES(filename, contenttype, filesize, userid, filedata) VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insertFile(File file);

    @Delete("DELETE FROM FILES WHERE filename = #{fileName}")
    void deleteFile(String fileName);

    @Select("SELECT filename FROM FILES WHERE userid = #{userId}")
    List<String> getAllFileNames(Integer userId);
}
