package com.hendisantika.service;

import com.hendisantika.exception.WarehouseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
}
