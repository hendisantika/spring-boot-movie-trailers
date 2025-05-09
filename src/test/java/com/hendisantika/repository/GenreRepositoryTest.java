package com.hendisantika.repository;

import com.hendisantika.SpringBootMovieTrailersApplicationTests;
import com.hendisantika.model.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GenreRepositoryTest extends SpringBootMovieTrailersApplicationTests {

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void save_shouldSaveGenre() {
        // Given
        Genre genre = new Genre(1, "Action");

        // When
        Genre savedGenre = genreRepository.save(genre);

        // Then
        assertEquals(1, savedGenre.getId(), "Genre ID should match");
        assertEquals("Action", savedGenre.getTitle(), "Genre title should match");
    }

    @Test
    void findById_shouldReturnGenre_whenGenreExists() {
        // Given
        Genre genre = new Genre(1, "Action");
        genreRepository.save(genre);

        // When
        Optional<Genre> foundGenre = genreRepository.findById(1);

        // Then
        assertTrue(foundGenre.isPresent(), "Genre should be found");
        assertEquals("Action", foundGenre.get().getTitle(), "Genre title should match");
    }

    @Test
    void findById_shouldReturnEmpty_whenGenreDoesNotExist() {
        // When
        Optional<Genre> foundGenre = genreRepository.findById(999);

        // Then
        assertFalse(foundGenre.isPresent(), "Genre should not be found");
    }

    @Test
    void findAll_shouldReturnAllGenres() {
        // Given
        Genre action = new Genre(1, "Action");
        Genre comedy = new Genre(2, "Comedy");
        Genre drama = new Genre(3, "Drama");
        genreRepository.saveAll(Arrays.asList(action, comedy, drama));

        // When
        List<Genre> genres = genreRepository.findAll();

        // Then
        assertEquals(3, genres.size(), "Should return 3 genres");
    }

    @Test
    void findAll_withSort_shouldReturnSortedGenres() {
        // Given
        Genre action = new Genre(1, "Action");
        Genre comedy = new Genre(2, "Comedy");
        Genre drama = new Genre(3, "Drama");
        genreRepository.saveAll(Arrays.asList(action, comedy, drama));

        // When
        List<Genre> genres = genreRepository.findAll(Sort.by("title"));

        // Then
        assertEquals(3, genres.size(), "Should return 3 genres");
        assertEquals("Action", genres.get(0).getTitle(), "First genre should be Action");
        assertEquals("Comedy", genres.get(1).getTitle(), "Second genre should be Comedy");
        assertEquals("Drama", genres.get(2).getTitle(), "Third genre should be Drama");
    }

    @Test
    void delete_shouldRemoveGenre() {
        // Given
        Genre genre = new Genre(1, "Action");
        genreRepository.save(genre);

        // When
        genreRepository.delete(genre);

        // Then
        Optional<Genre> foundGenre = genreRepository.findById(1);
        assertFalse(foundGenre.isPresent(), "Genre should be deleted");
    }
}