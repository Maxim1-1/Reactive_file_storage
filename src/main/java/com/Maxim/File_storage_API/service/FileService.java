package com.Maxim.File_storage_API.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FileService {

//    @Autowired
//    private FileRepository fileRepository;
//
//    public File saveFile (File file) {
//        return fileRepository.save(file);
//    }
//
//    public File getFileById(Integer userId) {
//        Optional<File> optionalFile = fileRepository.findById(userId);
////        TODO добавить обработку на предмет null
//        return optionalFile.get();
//    }
//
//    public List<File> getAllFiles() {
//        return fileRepository.findAll();
//    }
//
//    public File updateFileById(File file) {
//
////TODO make it through, userRepository.existsById(id)
//// +  сделать сравнение полей юзера из бд, и полей из json
//        return null;
//    }
//
//    public void deleteFileById(Integer id) {
//        //TODO change status instead of delete
////        userRepository.existsById(id)
//        fileRepository.deleteById(id);
//    }
}
