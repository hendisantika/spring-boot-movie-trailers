package com.hendisantika.controller;

import com.hendisantika.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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

}
