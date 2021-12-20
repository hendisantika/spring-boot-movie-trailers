package com.hendisantika.controller;

import com.hendisantika.model.Movie;
import com.hendisantika.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-movie-trailers
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 20/12/21
 * Time: 08.14
 */
@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    private MovieRepository movieRepository;

    @GetMapping
    public ModelAndView seeHomepage() {
        List<Movie> latestMovies =
                movieRepository.findAll(PageRequest.of(0, 4, Sort.by("premiereDate").descending())).toList();
        return new ModelAndView("index")
                .addObject("latest Movies", latestMovies);
    }

    @GetMapping("movies")
    public ModelAndView listMovies(@PageableDefault(sort = "premiereDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Movie> movies = movieRepository.findAll(pageable);
        return new ModelAndView("movies")
                .addObject("movies", movies);
    }

    @GetMapping("movies/{id}")
    public ModelAndView showMovieDetails(@PathVariable Integer id) {
        Movie movie = movieRepository.findById(id).get();
        return new ModelAndView("movie").addObject("movie", movie);
    }
}
