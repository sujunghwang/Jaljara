package com.ssafy.a802.jaljara.db.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Table(name="child_information")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class ChildInformation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "parent_id", nullable = false)
	private long parentsId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id", insertable = false, updatable = false, nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User parents;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "child_id", insertable = false, updatable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User child;

	@Column(name = "child_id")
	private Long childId;

	private String currentReward;

	private int streakCount;

	private LocalDateTime targetBedTime;

	private LocalDateTime targetWakeupTime;

}
