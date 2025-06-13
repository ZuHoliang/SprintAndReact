package com.example.demo.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.demo.model.enums.RequestStatus;
import com.example.demo.model.enums.ShiftType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "shift_swap_request")
public class ShiftSwapRequest {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "shift_swap_id")
	private Long shiftSwapId;
	
	//換班申請人
	@ManyToOne
	@JoinColumn(name = "requester_id")
	private User requestUser;
	
	//換班被申請人
	@ManyToOne
	@JoinColumn(name = "target_id", nullable = true)
	private User targetUser;
	
	//要換班的日期
	private LocalDate swapDate;
	
	//要換班的班別
	@Enumerated(EnumType.STRING)
	@Column(name = "swap_to_shift", nullable = true)
	private ShiftType swapToShift;
	
	//原本的班別(可以原先並未排班)
	@Enumerated(EnumType.STRING)
	@Column(name = "swap_from_shift", nullable = true)
	private ShiftType swapFromShift;
	
	//可備註換班事由
	@Column(name = "swap_message", nullable = true)
	private String swapMessage;
	
	//回應訊息
	@Column(name = "resp_message", nullable = true)
	private String respMessage;
	
	//請求狀態
	@Enumerated(EnumType.STRING)
	private RequestStatus reqStatus;
	
	//申請時間
	@CreationTimestamp
	private LocalDateTime requestTime;
	
	//回應時間
	@UpdateTimestamp
	private LocalDateTime responseTime;	

}
