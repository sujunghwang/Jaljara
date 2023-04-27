package com.ssafy.a802.jaljara.db.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Time;

import javax.persistence.*;

@Entity
@Table(name="child_information")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder(toBuilder = true)
public class ChildInformation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id", insertable = false, updatable = false, nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User parent;

	@Column(name = "parent_id", nullable = false)
	private long parentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "child_id", insertable = false, updatable = false, nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User child;

	@Column(name = "child_id", nullable = false)
	private long childId;

	@Column(nullable = false)
	@Builder.Default
	private String currentReward = "";

	@Column(nullable = false, columnDefinition = "SMALLINT")
	@Builder.Default
	private int streakCount = 0;

	@Column(nullable = false)
	@Builder.Default
	private Time targetBedTime = Time.valueOf("22:00:00");

	@Column(nullable = false)
	@Builder.Default
	private Time targetWakeupTime = Time.valueOf("08:00:00");;

}
