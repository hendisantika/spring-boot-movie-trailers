package com.hendisantika.controller;

import com.hendisantika.model.Genre;
import com.hendisantika.model.Movie;
import com.hendisantika.repository.GenreRepository;
import com.hendisantika.repository.MovieRepository;
import com.hendisantika.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("movies/new")
    public ModelAndView showNewFilmForm() {
        List<Genre> genres = genreRepository.findAll(Sort.by("title"));
        return new ModelAndView("admin/new-movie")
                .addObject("movie", new Movie())
                .addObject("genres", genres);
    }

    @PostMapping("/movies")
    public ModelAndView registerMovie(@Validated Movie movie, BindingResult bindingResult) {
        if (bindingResult.hasErrors() || movie.getFrontPage().isEmpty()) {
            if (movie.getFrontPage().isEmpty()) {
                bindingResult.rejectValue("frontPage", "MultipartNotEmpty");
            }

            List<Genre> genres = genreRepository.findAll(Sort.by("title"));
            return new ModelAndView("admin/new-movie")
                    .addObject("movie", movie)
                    .addObject("genres", genres);
        }

        String routeCover = warehouseService.storeFile(movie.getFrontPage());
        movie.setRouteCover(routeCover);

        movieRepository.save(movie);
        return new ModelAndView("redirect:/admin");
    }

    @GetMapping("/movies/{id}/edit")
    public ModelAndView showMovieEditForm(@PathVariable Integer id) {
        Movie movie = movieRepository.findById(id).get();
        List<Genre> genres = genreRepository.findAll(Sort.by("title"));

        return new ModelAndView("admin/edit-movie")
                .addObject("movie", movie)
                .addObject("genres", genres);
    }
}
