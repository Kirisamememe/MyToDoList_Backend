package com.example.mytodolist_backend;

import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    //GETリクエストでサーバにデータを送る
    @PostMapping("/hello3")
    public String sayHello3(@RequestParam("name") String name){
        return "Hello world! こんにちは" + name + "さん";
    }

    @GetMapping("/hello")
    public String sayHello(String name, int age){
        return "Hello world!" + "こんにちは" + age + "歳の" + name + "さん";
    }

    @GetMapping("/hello2/{name}/{age}")
    public String sayHello2(@PathVariable("name") String name, @PathVariable("age") int age){
        return "Hello world!" + "こんにちは" + age + "歳の" + name + "さん";
    }
}
