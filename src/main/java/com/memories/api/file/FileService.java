package com.memories.api.file;


import com.memories.api.config.MemoriesProperties;
import com.memories.api.user.User;
import org.apache.tika.Tika;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@EnableScheduling
public class FileService {

    private final MemoriesProperties memoriesProperties;
    private final FileAttachmentRepository fileAttachmentRepository;
    Tika tika = new Tika();
    public FileService(MemoriesProperties memoriesProperties, FileAttachmentRepository fileAttachmentRepository) {
        this.memoriesProperties = memoriesProperties;
        this.fileAttachmentRepository = fileAttachmentRepository;
    }
    public String saveBas64StringAsFile(String image) {
        String fileName = UUID.randomUUID().toString();

        Path path = getProfileImagePath(fileName);
        try {
            OutputStream outputStream = new FileOutputStream(path.toFile());
            outputStream.write(decodedImage(image));
            outputStream.close();
            return fileName;
        } catch (IOException e) {
                e.printStackTrace();
        }
        return null;
    }

    private Path getProfileImagePath(String fileName) {
        return Paths.get(memoriesProperties.getStorage().getRoot(),
                memoriesProperties.getStorage().getProfile(),
                fileName);
    }


    public String detectType(String value) {
        return tika.detect(decodedImage(value));
    }

    private byte[] decodedImage(String encodedImage) {
        return Base64.getDecoder().decode(encodedImage.split(",")[1]);
    }

    public void deleteProfileImage(String image) {
        if (image == null) {
            return;
        }
        Path path = getProfileImagePath(image);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public FileAttachment saveMemoryAttachment(MultipartFile multipartFile) {
        String fileName = UUID.randomUUID().toString();
        Path path = getMemoryImagePath(fileName);
        try {
            byte[] arr = multipartFile.getBytes();
            OutputStream outputStream = new FileOutputStream(path.toFile());
            outputStream.write(arr);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileAttachment attachment = new FileAttachment();
        attachment.setName(fileName);
        attachment.setDate(new Date());

        return fileAttachmentRepository.save(attachment);
    }

    private Path getMemoryImagePath(String fileName) {
        return Paths.get(memoriesProperties.getStorage().getRoot(),
                memoriesProperties.getStorage().getMemory(),
                fileName);
    }

    public void deleteAttachmentFile(String imageName) {
        if (imageName == null) {
            return;
        }
        Path path = getMemoryImagePath(imageName);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void cleanupStorage() {
        Date twentyFourHoursAgo = new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
        List<FileAttachment> filesToBeDeleted = fileAttachmentRepository.findByDateBeforeAndMemoryIsNull(twentyFourHoursAgo);
        for(FileAttachment file : filesToBeDeleted) {
            deleteAttachmentFile(file.getName());
            fileAttachmentRepository.deleteById(file.getId());
        }

    }

    public void deleteAllStoredFilesOfUser(User user) {
        deleteProfileImage(user.getImage());
        List<FileAttachment> filesToBeRemoved = fileAttachmentRepository.findByMemoryUser(user);
        filesToBeRemoved.forEach(fileAttachment -> deleteAttachmentFile(fileAttachment.getName()));

    }
}
