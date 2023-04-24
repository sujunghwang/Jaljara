package com.ssafy.a802.jaljara.db.repository;

import com.ssafy.a802.jaljara.db.entity.ChildInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChildInformationRepository extends JpaRepository<ChildInformation, Long> {
    ChildInformation findByChildId(long childId);
}
