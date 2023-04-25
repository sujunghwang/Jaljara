package com.ssafy.a802.jaljara.db.repository;

import com.ssafy.a802.jaljara.db.entity.ChildInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ChildInformationRepository extends JpaRepository<ChildInformation, Long> {
    ChildInformation findByChildId(long childId);
    List<ChildInformation> findAllByParentId(long parentId);
    @Transactional
    void deleteByChildId(long childId);
}
