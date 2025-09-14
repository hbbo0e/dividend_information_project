package com.example.dividence.persist;

import com.example.dividence.persist.entity.DividendEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface DividendRepository extends JpaRepository<DividendEntity, Long> {
  List<DividendEntity> findAllByCompanyId(Long companyId);

  @Transactional
  void deleteAllByCompanyId(Long id);

  boolean existsByCompanyIdAndDate(Long companyId, LocalDateTime date);
}
