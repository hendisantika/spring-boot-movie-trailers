package id.my.hendisantika.movietrailer.controller;

import id.my.hendisantika.movietrailer.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-movie-trailers
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 20/12/21
 * Time: 08.21
 */
@RestController
@RequestMapping("/assets")
public class AssetsController {
    @Autowired
    private WarehouseService warehouseService;

    @GetMapping("/{filename:.+}")
    public Resource getResource(@PathVariable("filename") String filename) {
        return warehouseService.uploadAsResource(filename);
    }
}
