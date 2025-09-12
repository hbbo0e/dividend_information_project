package com.example.dividence.persist;

import com.example.dividence.persist.entity.DividendEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DividendRepository extends JpaRepository<DividendEntity, Long> {
  List<DividendEntity> findAllByCompanyId(Long companyId);
}
