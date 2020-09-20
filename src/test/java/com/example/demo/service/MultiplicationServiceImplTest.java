package com.example.demo.service;

import com.example.demo.domain.Multiplication;
import com.example.demo.domain.MultiplicationResultAttempt;
import com.example.demo.domain.User;
import com.example.demo.repository.MultiplicationRepository;
import com.example.demo.repository.MultiplicationResultAttemptRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class MultiplicationServiceImplTest {

    @Mock
    private RandomGeneratorService randomGeneratorService;

    private MultiplicationServiceImpl multiplicationServiceImpl;

    @Mock
    private MultiplicationResultAttemptRepository multiplicationResultAttemptRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MultiplicationRepository multiplicationRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.multiplicationServiceImpl = new MultiplicationServiceImpl(randomGeneratorService,
                multiplicationResultAttemptRepository, userRepository, multiplicationRepository);
    }

    @Test
    void createRandomMultiplicationTest() {
        // given (randomGeneratorService가 처음에 50, 나중에 30을 반환하도록 설정)
        given(randomGeneratorService.generateRandomFactor()).willReturn(50, 30);

        // when
        Multiplication multiplication = multiplicationServiceImpl.createRandomMultiplication();

        assertThat(multiplication.getFactorA()).isEqualTo(50);
        assertThat(multiplication.getFactorB()).isEqualTo(30);
    }

    @Test
    public void checkCorrectAttemptTest() {
        // given
        User user = new User("hyewon jo");
        Multiplication multiplication = new Multiplication(50, 60);
        int resultAttempt = 3000;
        MultiplicationResultAttempt multiplicationResultAttempt = new MultiplicationResultAttempt(
                user, multiplication, resultAttempt,false);
        MultiplicationResultAttempt verifiedAttempt = new MultiplicationResultAttempt(
                user, multiplication, resultAttempt, true);

        // when
        boolean attemptResult = multiplicationServiceImpl.checkAttempt(multiplicationResultAttempt);

        // assert
        assertThat(attemptResult).isTrue();
        verify(multiplicationResultAttemptRepository).save(verifiedAttempt);
    }

    @Test
    public void checkWrongAttemptTest() {
        // given
        User user = new User("hyewon jo");
        Multiplication multiplication = new Multiplication(50, 60);
        int resultAttempt = 3010;
        MultiplicationResultAttempt multiplicationResultAttempt = new MultiplicationResultAttempt(user, multiplication, resultAttempt,
                false);

        // when
        boolean attemptResult = multiplicationServiceImpl.checkAttempt(multiplicationResultAttempt);

        // assert
        assertThat(attemptResult).isFalse();
        verify(multiplicationResultAttemptRepository).save(multiplicationResultAttempt);
    }
}