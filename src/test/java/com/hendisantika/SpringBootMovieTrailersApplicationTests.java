package com.hendisantika;

import com.hendisantika.repository.GenreRepository;
import com.hendisantika.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Testcontainers
@SpringBootTest(
        properties = {
                "management.endpoint.health.show-details=always",
                "spring.datasource.url=jdbc:tc:mysql:8.4.0:///cinemaDB"
        },
        webEnvironment = RANDOM_PORT
)
class SpringBootMovieTrailersApplicationTests {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    @BeforeEach
    void deleteAll() {
        genreRepository.deleteAll();
        movieRepository.deleteAll();
    }

}
