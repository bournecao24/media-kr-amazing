package com.kr.media.amazing.controller;


import com.kr.media.amazing.entity.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("test")
public class HelloWordController {

    private List<Book> books = new ArrayList<>();

    @Value("${wuhan2020}")
    String wuhan2020;


    /**
     *   ResponseEntity :  表示整个HTTP Response：状态码，标头和正文内容。我们可以使用它来自定义HTTP Response 的内容。
     * @param book
     * @return
     */
    @PostMapping("/book")
    public ResponseEntity<List<Book>> addBook(@RequestBody Book book) {
        books.add(book);
        return ResponseEntity.ok(books);
    }





    @GetMapping("hello")
    public String sayHello() {
        return "Hello World";
    }

}
