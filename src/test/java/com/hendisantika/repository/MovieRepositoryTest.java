package com.hendisantika.repository;

import com.hendisantika.SpringBootMovieTrailersApplicationTests;
import com.hendisantika.model.Genre;
import com.hendisantika.model.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MovieRepositoryTest extends SpringBootMovieTrailersApplicationTests {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void save_shouldSaveMovie() {
        // Given
        Genre action = new Genre(1, "Action");
        genreRepository.save(action);

        Movie movie = new Movie();
        movie.setTitle("Test Movie");
        movie.setSinopsis("Test Synopsis");
        movie.setPremiereDate(LocalDate.now());
        movie.setYoutubeTrailerId("abc123");
        movie.setRouteCover("test-cover.jpg");
        movie.setGenres(List.of(action));

        // When
        Movie savedMovie = movieRepository.save(movie);

        // Then
        assertNotNull(savedMovie.getId(), "Movie ID should not be null after saving");
        assertEquals("Test Movie", savedMovie.getTitle(), "Movie title should match");
    }

    @Test
    void findById_shouldReturnMovie_whenMovieExists() {
        // Given
        Genre action = new Genre(1, "Action");
        genreRepository.save(action);

        Movie movie = new Movie();
        movie.setTitle("Test Movie");
        movie.setSinopsis("Test Synopsis");
        movie.setPremiereDate(LocalDate.now());
        movie.setYoutubeTrailerId("abc123");
        movie.setRouteCover("test-cover.jpg");
        movie.setGenres(List.of(action));
        Movie savedMovie = movieRepository.save(movie);

        // When
        Optional<Movie> foundMovie = movieRepository.findById(savedMovie.getId());

        // Then
        assertTrue(foundMovie.isPresent(), "Movie should be found");
        assertEquals("Test Movie", foundMovie.get().getTitle(), "Movie title should match");
    }

    @Test
    void findById_shouldReturnEmpty_whenMovieDoesNotExist() {
        // When
        Optional<Movie> foundMovie = movieRepository.findById(999);

        // Then
        assertFalse(foundMovie.isPresent(), "Movie should not be found");
    }

    @Test
    void findAll_shouldReturnAllMovies() {
        // Given
        Genre action = new Genre(1, "Action");
        Genre comedy = new Genre(2, "Comedy");
        genreRepository.saveAll(Arrays.asList(action, comedy));

        Movie movie1 = new Movie();
        movie1.setTitle("Test Movie 1");
        movie1.setSinopsis("Test Synopsis 1");
        movie1.setPremiereDate(LocalDate.now());
        movie1.setYoutubeTrailerId("abc123");
        movie1.setRouteCover("test-cover-1.jpg");
        movie1.setGenres(List.of(action));

        Movie movie2 = new Movie();
        movie2.setTitle("Test Movie 2");
        movie2.setSinopsis("Test Synopsis 2");
        movie2.setPremiereDate(LocalDate.now().minusDays(1));
        movie2.setYoutubeTrailerId("def456");
        movie2.setRouteCover("test-cover-2.jpg");
        movie2.setGenres(List.of(comedy));

        movieRepository.saveAll(Arrays.asList(movie1, movie2));

        // When
        List<Movie> movies = movieRepository.findAll();

        // Then
        assertEquals(2, movies.size(), "Should return 2 movies");
    }

    @Test
    void findAll_shouldReturnPagedMovies() {
        // Given
        Genre action = new Genre(1, "Action");
        Genre comedy = new Genre(2, "Comedy");
        Genre drama = new Genre(3, "Drama");
        genreRepository.saveAll(Arrays.asList(action, comedy, drama));

        Movie movie1 = new Movie();
        movie1.setTitle("Test Movie 1");
        movie1.setSinopsis("Test Synopsis 1");
        movie1.setPremiereDate(LocalDate.now());
        movie1.setYoutubeTrailerId("abc123");
        movie1.setRouteCover("test-cover-1.jpg");
        movie1.setGenres(List.of(action));

        Movie movie2 = new Movie();
        movie2.setTitle("Test Movie 2");
        movie2.setSinopsis("Test Synopsis 2");
        movie2.setPremiereDate(LocalDate.now().minusDays(1));
        movie2.setYoutubeTrailerId("def456");
        movie2.setRouteCover("test-cover-2.jpg");
        movie2.setGenres(List.of(comedy));

        Movie movie3 = new Movie();
        movie3.setTitle("Test Movie 3");
        movie3.setSinopsis("Test Synopsis 3");
        movie3.setPremiereDate(LocalDate.now().minusDays(2));
        movie3.setYoutubeTrailerId("ghi789");
        movie3.setRouteCover("test-cover-3.jpg");
        movie3.setGenres(List.of(drama));

        movieRepository.saveAll(Arrays.asList(movie1, movie2, movie3));

        // When
        Page<Movie> moviePage = movieRepository.findAll(PageRequest.of(0, 2, Sort.by("premiereDate").descending()));

        // Then
        assertEquals(2, moviePage.getContent().size(), "Page should contain 2 movies");
        assertEquals(3, moviePage.getTotalElements(), "Total should be 3 movies");
        assertEquals("Test Movie 1", moviePage.getContent().get(0).getTitle(), "First movie should be the most recent");
    }

    @Test
    void delete_shouldRemoveMovie() {
        // Given
        Genre action = new Genre(1, "Action");
        genreRepository.save(action);

        Movie movie = new Movie();
        movie.setTitle("Test Movie");
        movie.setSinopsis("Test Synopsis");
        movie.setPremiereDate(LocalDate.now());
        movie.setYoutubeTrailerId("abc123");
        movie.setRouteCover("test-cover.jpg");
        movie.setGenres(List.of(action));
        Movie savedMovie = movieRepository.save(movie);

        // When
        movieRepository.delete(savedMovie);

        // Then
        Optional<Movie> foundMovie = movieRepository.findById(savedMovie.getId());
        assertFalse(foundMovie.isPresent(), "Movie should be deleted");
    }
}