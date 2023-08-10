package com.v1.ecommerce.exception.handler;


import com.v1.ecommerce.exception.CartItemException;
import com.v1.ecommerce.exception.OrderException;
import com.v1.ecommerce.exception.ProductException;
import com.v1.ecommerce.exception.UserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ ProductException.class, UserException.class, CartItemException.class, OrderException.class})  // Có thể bắt nhiều loại exception
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(432).body(e.getMessage());
    }

    // Có thêm các @ExceptionHandler khác...

    // Nên bắt cả Exception.class
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnwantedException(Exception e) {
        // Log lỗi ra và ẩn đi message thực sự (xem phần 3.2)
        e.printStackTrace();  // Thực tế người ta dùng logger
        return ResponseEntity.status(500).body("Error server");
    }
}


