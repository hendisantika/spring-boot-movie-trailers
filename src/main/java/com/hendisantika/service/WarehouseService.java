package com.hendisantika.service;

import com.hendisantika.exception.WarehouseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-movie-trailers
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 20/12/21
 * Time: 07.54
 */
@Service
public class WarehouseService {
    @Value("${storage.location}")
    private String storageLocation;

    //This is used to indicate that this method is going to be executed every time it finds a new instance of this
    // class.
    @PostConstruct
    public void startStoreFiles() {
        try {
            Files.createDirectories(Paths.get(storageLocation));
        } catch (IOException exception) {
            throw new WarehouseException("Failed to initialize the location in the file store");
        }
    }

    public String storeFile(MultipartFile archive) {
        String filename = archive.getOriginalFilename();
        if (archive.isEmpty()) {
            throw new WarehouseException("Can't store an empty file");
        }
        try {
            InputStream inputStream = archive.getInputStream();
            Files.copy(inputStream, Paths.get(storageLocation).resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            throw new WarehouseException("Error al almacenar el archive " + filename, exception);
        }
        return filename;
    }
}
