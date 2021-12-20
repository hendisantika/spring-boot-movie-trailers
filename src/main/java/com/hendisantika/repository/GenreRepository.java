package com.hendisantika.repository;

import com.hendisantika.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-movie-trailers
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 20/12/21
 * Time: 07.52
 */
public interface GenreRepository extends JpaRepository<Genre, Integer> {
}
