package com.hendisantika.controller;

import com.hendisantika.model.Genre;
import com.hendisantika.model.Movie;
import com.hendisantika.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class HomeControllerTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private HomeController homeController;

    private MockMvc mockMvc;

    private Movie movie1;
    private Movie movie2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(homeController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        Genre action = new Genre(1, "Action");

        movie1 = new Movie();
        movie1.setId(1);
        movie1.setTitle("Test Movie 1");
        movie1.setSinopsis("Test Synopsis 1");
        movie1.setPremiereDate(LocalDate.now());
        movie1.setYoutubeTrailerId("abc123");
        movie1.setRouteCover("test-cover-1.jpg");
        movie1.setGenres(List.of(action));

        movie2 = new Movie();
        movie2.setId(2);
        movie2.setTitle("Test Movie 2");
        movie2.setSinopsis("Test Synopsis 2");
        movie2.setPremiereDate(LocalDate.now().minusDays(1));
        movie2.setYoutubeTrailerId("def456");
        movie2.setRouteCover("test-cover-2.jpg");
        movie2.setGenres(List.of(action));
    }

    @Test
    void seeHomepage_shouldReturnIndexViewWithLatestMovies() throws Exception {
        // Given
        List<Movie> latestMovies = Arrays.asList(movie1, movie2);
        when(movieRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(latestMovies));

        // When/Then
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("latestMovies", hasSize(2)));
    }

    @Test
    void listMovies_shouldReturnMoviesViewWithPagedMovies() throws Exception {
        // Given
        Page<Movie> moviePage = new PageImpl<>(Arrays.asList(movie1, movie2));
        when(movieRepository.findAll(any(Pageable.class))).thenReturn(moviePage);

        // When/Then
        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(view().name("movies"))
                .andExpect(model().attributeExists("movies"));
    }

    @Test
    void showMovieDetails_shouldReturnMovieViewWithMovie() throws Exception {
        // Given
        when(movieRepository.findById(eq(1))).thenReturn(Optional.of(movie1));

        // When/Then
        mockMvc.perform(get("/movies/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("movie"))
                .andExpect(model().attributeExists("movie"));
    }
}