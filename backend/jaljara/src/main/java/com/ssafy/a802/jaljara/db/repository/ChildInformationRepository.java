package com.ssafy.a802.jaljara.db.repository;

import com.ssafy.a802.jaljara.db.entity.ChildInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChildInformationRepository extends JpaRepository<ChildInformation, Long> {
    Optional<ChildInformation> findByChildId(long childId);
    boolean existsByChildId(long childId);
    List<ChildInformation> findAllByParentId(long parentId);
}
