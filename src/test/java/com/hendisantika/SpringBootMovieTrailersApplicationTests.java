package com.hendisantika;

import com.hendisantika.repository.GenreRepository;
import com.hendisantika.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Testcontainers
@SpringBootTest(
        properties = {
                "management.endpoint.health.show-details=always",
                "spring.datasource.url=jdbc:tc:postgresql:17.5-alpine3.21:///cinemaDB"
        },
        webEnvironment = RANDOM_PORT
)
public class SpringBootMovieTrailersApplicationTests {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    @BeforeEach
    void deleteAll() {
        genreRepository.deleteAll();
        movieRepository.deleteAll();
    }

    @Test
    void contextLoads() {
        // Verify that the application context loads successfully
        assertNotNull(movieRepository, "MovieRepository should be autowired");
        assertNotNull(genreRepository, "GenreRepository should be autowired");
    }
}
