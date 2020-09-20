package com.example.demo.repository;

import com.example.demo.domain.Multiplication;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * {@link Multiplication}을 저장하고 조회하기 위한 인터페이스
 */
public interface MultiplicationRepository extends CrudRepository<Multiplication, Long> {
    Optional<Multiplication> findByFactorAAndFactorB(int factorA, int factorB);
}
