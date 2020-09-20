package com.example.demo.service;

import com.example.demo.domain.Multiplication;
import com.example.demo.domain.MultiplicationResultAttempt;
import com.example.demo.domain.User;
import com.example.demo.repository.MultiplicationResultAttemptRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
public class MultiplicationServiceImpl implements MultiplicationService {

    private final RandomGeneratorService randomGeneratorService;
    private final MultiplicationResultAttemptRepository multiplicationResultAttemptRepository;
    private final UserRepository userRepository;

    @Autowired
    public MultiplicationServiceImpl(RandomGeneratorService randomGeneratorService,
                                     MultiplicationResultAttemptRepository multiplicationResultAttemptRepository,
                                     UserRepository userRepository) {
        this.randomGeneratorService = randomGeneratorService;
        this.multiplicationResultAttemptRepository = multiplicationResultAttemptRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Multiplication createRandomMultiplication() {
        return new Multiplication(randomGeneratorService.generateRandomFactor(), randomGeneratorService.generateRandomFactor());
    }

    @Override
    public boolean checkAttempt(MultiplicationResultAttempt multiplicationResultAttempt) {
        // 해당 닉네임의 사용자가 존재하는지 확인
        Optional<User> user = userRepository.findByAlias(multiplicationResultAttempt.getUser().getAlias());

        // expression 이 true 가 아닌 경우 exception 을 던진다.
        // 조작된 답안을 방지
        Assert.isTrue(!multiplicationResultAttempt.isCorrect(), "채점한 상태로 보낼 수 없습니다!");

        // 답안을 채점
        boolean isCorrect = multiplicationResultAttempt.getResultAttempt() ==
                multiplicationResultAttempt.getMultiplication().getFactorA() *
                        multiplicationResultAttempt.getMultiplication().getFactorB();

        MultiplicationResultAttempt checkedAttempt = new MultiplicationResultAttempt(user.orElse(multiplicationResultAttempt.getUser()),
                multiplicationResultAttempt.getMultiplication(), multiplicationResultAttempt.getResultAttempt(),
                isCorrect);

         // 답안을 저장
        multiplicationResultAttemptRepository.save(checkedAttempt);

        return isCorrect;
    }
}
