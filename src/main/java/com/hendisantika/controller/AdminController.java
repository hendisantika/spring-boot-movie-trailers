package com.hendisantika.controller;

import com.hendisantika.repository.GenreRepository;
import com.hendisantika.repository.MovieRepository;
import com.hendisantika.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-movie-trailers
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 20/12/21
 * Time: 08.23
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private WarehouseService warehouseService;
}
