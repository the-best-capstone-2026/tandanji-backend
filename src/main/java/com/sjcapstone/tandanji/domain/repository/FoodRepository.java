package com.sjcapstone.tandanji.domain.repository;

import com.sjcapstone.tandanji.domain.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository // 이 인터페이스가 Repository임을 선언합니다.
public interface FoodRepository extends JpaRepository<Food, Long> {
    // JpaRepository를 상속받으면 기본적인 저장(save), 조회(findById), 삭제(delete) 기능을 자동으로 가집니다.
}