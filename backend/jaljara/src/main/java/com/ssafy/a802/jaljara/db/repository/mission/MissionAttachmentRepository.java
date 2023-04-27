package com.ssafy.a802.jaljara.db.repository.mission;

import java.sql.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ssafy.a802.jaljara.db.entity.MissionAttachment;

public interface MissionAttachmentRepository extends JpaRepository<MissionAttachment, Long> {

}
