package sk.kosickaakademia.deco.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class Controller {

    @RequestMapping("/hello")
    public String getHello(){
        return "Hello Friend";
    }

    @GetMapping("/hello/{name}")
    public String getHello(@PathVariable String name){
        return "Hello Friend" + name;
    }
    
    @RequestMapping("/time")
    public String getTime(){
        return new Date().toString();
    }


}
