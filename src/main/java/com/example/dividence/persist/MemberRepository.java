package com.example.dividence.persist;

import com.example.dividence.model.MemberEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

  Optional<MemberEntity> findByUsername(String username);
  boolean existsByUsername (String username);
}
