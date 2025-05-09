package com.hendisantika.controller;

import com.hendisantika.model.Genre;
import com.hendisantika.model.Movie;
import com.hendisantika.repository.GenreRepository;
import com.hendisantika.repository.MovieRepository;
import com.hendisantika.service.WarehouseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private WarehouseService warehouseService;

    @InjectMocks
    private AdminController adminController;

    private MockMvc mockMvc;

    private Movie movie1;
    private Movie movie2;
    private Genre action;
    private Genre comedy;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        action = new Genre(1, "Action");
        comedy = new Genre(2, "Comedy");

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
        movie2.setGenres(List.of(comedy));
    }

    @Test
    void seeHomepage_shouldReturnAdminIndexViewWithPagedMovies() throws Exception {
        // Given
        Page<Movie> moviePage = new PageImpl<>(Arrays.asList(movie1, movie2));
        when(movieRepository.findAll(any(Pageable.class))).thenReturn(moviePage);

        // When/Then
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/index"))
                .andExpect(model().attributeExists("movies"));
    }

    @Test
    void showNewFilmForm_shouldReturnNewMovieViewWithGenres() throws Exception {
        // Given
        List<Genre> genres = Arrays.asList(action, comedy);
        when(genreRepository.findAll(any(Sort.class))).thenReturn(genres);

        // When/Then
        mockMvc.perform(get("/admin/movies/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/new-movie"))
                .andExpect(model().attributeExists("movie"))
                .andExpect(model().attributeExists("genres"));
    }

    @Test
    void registerMovie_shouldRedirectToAdmin_whenValidInput() throws Exception {
        // Given
        MockMultipartFile frontPage = new MockMultipartFile(
                "frontPage", "test.jpg", "image/jpeg", "test image content".getBytes());

        when(warehouseService.storeFile(any())).thenReturn("test.jpg");
        when(movieRepository.save(any(Movie.class))).thenReturn(movie1);

        // When/Then
        mockMvc.perform(multipart("/admin/movies")
                        .file(frontPage)
                        .param("title", "Test Movie")
                        .param("sinopsis", "Test Synopsis")
                        .param("premiereDate", LocalDate.now().toString())
                        .param("youtubeTrailerId", "abc123")
                        .param("genres", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));

        verify(warehouseService, times(1)).storeFile(any());
        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    @Test
    void registerMovie_shouldReturnToForm_whenInvalidInput() throws Exception {
        // Given
        List<Genre> genres = Arrays.asList(action, comedy);
        when(genreRepository.findAll(any(Sort.class))).thenReturn(genres);

        // When/Then
        mockMvc.perform(multipart("/admin/movies")
                        .param("title", "") // Invalid: empty title
                        .param("sinopsis", "Test Synopsis")
                        .param("premiereDate", LocalDate.now().toString())
                        .param("youtubeTrailerId", "abc123")
                        .param("genres", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/new-movie"))
                .andExpect(model().attributeExists("movie"))
                .andExpect(model().attributeExists("genres"));

        verify(warehouseService, times(0)).storeFile(any());
        verify(movieRepository, times(0)).save(any(Movie.class));
    }

    @Test
    void showMovieEditForm_shouldReturnEditMovieViewWithMovieAndGenres() throws Exception {
        // Given
        List<Genre> genres = Arrays.asList(action, comedy);
        when(movieRepository.findById(eq(1))).thenReturn(Optional.of(movie1));
        when(genreRepository.findAll(any(Sort.class))).thenReturn(genres);

        // When/Then
        mockMvc.perform(get("/admin/movies/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/edit-movie"))
                .andExpect(model().attributeExists("movie"))
                .andExpect(model().attributeExists("genres"));
    }

    @Test
    void updateMovie_shouldRedirectToAdmin_whenValidInput() throws Exception {
        // Given
        when(movieRepository.getById(eq(1))).thenReturn(movie1);
        when(movieRepository.save(any(Movie.class))).thenReturn(movie1);

        // When/Then
        mockMvc.perform(multipart("/admin/movies/1/edit")
                        .param("title", "Updated Movie")
                        .param("sinopsis", "Updated Synopsis")
                        .param("premiereDate", LocalDate.now().toString())
                        .param("youtubeTrailerId", "updated123")
                        .param("genres", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));

        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    @Test
    void updateMovie_withNewFrontPage_shouldUpdateCoverAndRedirectToAdmin() throws Exception {
        // Given
        MockMultipartFile frontPage = new MockMultipartFile(
                "frontPage", "updated.jpg", "image/jpeg", "updated image content".getBytes());

        when(movieRepository.getById(eq(1))).thenReturn(movie1);
        when(movieRepository.save(any(Movie.class))).thenReturn(movie1);
        doNothing().when(warehouseService).deleteArchive(anyString());
        when(warehouseService.storeFile(any())).thenReturn("updated.jpg");

        // When/Then
        mockMvc.perform(multipart("/admin/movies/1/edit")
                        .file(frontPage)
                        .param("title", "Updated Movie")
                        .param("sinopsis", "Updated Synopsis")
                        .param("premiereDate", LocalDate.now().toString())
                        .param("youtubeTrailerId", "updated123")
                        .param("genres", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));

        verify(warehouseService, times(1)).deleteArchive(anyString());
        verify(warehouseService, times(1)).storeFile(any());
        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    @Test
    void updateMovie_shouldReturnToForm_whenInvalidInput() throws Exception {
        // Given
        List<Genre> genres = Arrays.asList(action, comedy);
        when(genreRepository.findAll(any(Sort.class))).thenReturn(genres);

        // When/Then
        mockMvc.perform(multipart("/admin/movies/1/edit")
                        .param("title", "") // Invalid: empty title
                        .param("sinopsis", "Updated Synopsis")
                        .param("premiereDate", LocalDate.now().toString())
                        .param("youtubeTrailerId", "updated123")
                        .param("genres", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/edit-movie"))
                .andExpect(model().attributeExists("movie"))
                .andExpect(model().attributeExists("genres"));

        verify(movieRepository, times(0)).save(any(Movie.class));
    }

    @Test
    void deleteMovie_shouldDeleteMovieAndRedirectToAdmin() throws Exception {
        // Given
        when(movieRepository.getById(eq(1))).thenReturn(movie1);
        doNothing().when(movieRepository).delete(any(Movie.class));
        doNothing().when(warehouseService).deleteArchive(anyString());

        // When/Then
        mockMvc.perform(post("/admin/movies/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));

        verify(movieRepository, times(1)).delete(any(Movie.class));
        verify(warehouseService, times(1)).deleteArchive(anyString());
    }
}