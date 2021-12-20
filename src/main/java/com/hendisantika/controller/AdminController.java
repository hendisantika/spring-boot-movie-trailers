package com.hendisantika.controller;

import com.hendisantika.model.Movie;
import com.hendisantika.repository.GenreRepository;
import com.hendisantika.repository.MovieRepository;
import com.hendisantika.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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

    @GetMapping
    public ModelAndView seeHomepage(@PageableDefault(sort = "titulo", size = 5) Pageable pageable) {
        Page<Movie> movies = movieRepository.findAll(pageable);
        return new ModelAndView("admin/index").addObject("movies", movies);
    }

}
