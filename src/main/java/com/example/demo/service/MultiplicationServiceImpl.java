package com.example.demo.service;

import com.example.demo.domain.Multiplication;
import com.example.demo.domain.MultiplicationResultAttempt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class MultiplicationServiceImpl implements MultiplicationService {

    private final RandomGeneratorService randomGeneratorService;

    @Autowired
    public MultiplicationServiceImpl(RandomGeneratorService randomGeneratorService) {
        this.randomGeneratorService = randomGeneratorService;
    }

    @Override
    public Multiplication createRandomMultiplication() {
        return new Multiplication(randomGeneratorService.generateRandomFactor(), randomGeneratorService.generateRandomFactor());
    }

    @Override
    public boolean checkAttempt(MultiplicationResultAttempt multiplicationResultAttempt) {
        boolean correct = multiplicationResultAttempt.getResultAttempt() ==
                multiplicationResultAttempt.getMultiplication().getFactorA() *
                        multiplicationResultAttempt.getMultiplication().getFactorB();

        // expression이 true가 아닌 경우 exception을 던진다.
        // 조작된 답안을 방지
        Assert.isTrue(!multiplicationResultAttempt.isCorrect(), "채점한 상태로 보낼 수 없습니다!");

        // 복사본을 만들고 correct 필드를 상황에 맞게 설정
        // 자.. 복사본을 왜 만들어야할까. ? final이니까?
        // 심지어 지금 안써.
        MultiplicationResultAttempt checkedAttempt = new MultiplicationResultAttempt(multiplicationResultAttempt.getUser(),
                multiplicationResultAttempt.getMultiplication(), multiplicationResultAttempt.getResultAttempt(),
                correct);

        return correct;
    }
}
