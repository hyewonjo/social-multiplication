package com.example.demo.controller;

import com.example.demo.domain.Multiplication;
import com.example.demo.service.MultiplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/multiplications")
public class MultiplicationController {

    private final MultiplicationService multiplicationService;

    @GetMapping("/random")
    public Multiplication getRandomMultiplication() {
        return multiplicationService.createRandomMultiplication();
    }
}
