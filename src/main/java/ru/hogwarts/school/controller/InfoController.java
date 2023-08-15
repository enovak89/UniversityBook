package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {
    @Value("${local.server.port}")
    Integer port;

    @GetMapping("/get-port")
    public Integer getApplicationPort() {
        System.out.println(port);
        return port;
    }
}
