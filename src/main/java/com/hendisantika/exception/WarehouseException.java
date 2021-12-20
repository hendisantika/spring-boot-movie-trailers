package com.hendisantika.exception;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-movie-trailers
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 20/12/21
 * Time: 07.46
 */
public class WarehouseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public WarehouseException(String message) {
        super(message);
    }

    public WarehouseException(String message, Throwable exception) {
        super(message, exception);
    }
}
