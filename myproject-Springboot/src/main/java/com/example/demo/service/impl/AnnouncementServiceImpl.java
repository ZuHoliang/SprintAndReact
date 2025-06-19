package com.example.demo.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.config.AIModerationConfig;
import com.example.demo.exception.AnnouncementNotFoundException;
import com.example.demo.mapper.AnnouncementMapper;
import com.example.demo.model.dto.AnnouncementDto;
import com.example.demo.model.dto.AnnouncementEditDto;
import com.example.demo.model.entity.Announcement;
import com.example.demo.repository.AnnouncementRepository;
import com.example.demo.service.AIModerationService;
import com.example.demo.service.AnnouncementService;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

	@Autowired
	private AnnouncementRepository announcementRepository;

	@Autowired
	private AnnouncementMapper announcementMapper;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private AIModerationService aiModerationService;
	
	@Autowired
	private AIModerationConfig moderationConfig;

	// 取得最新公告(WebStocket)
	private void broadcastLatest() {
		List<Announcement> latest = announcementRepository.findTop5ByAnnouncementActiveTrueOrderByCreatedTimeDesc();
		List<AnnouncementDto> dtos = announcementMapper.toDtoList(latest);
		messagingTemplate.convertAndSend("/topic/announcements", dtos);
	}

	// 取得最新公告
	@Override
	public List<AnnouncementDto> getLatestAnnouncements() {
		List<Announcement> latest = announcementRepository.findTop5ByAnnouncementActiveTrueOrderByCreatedTimeDesc();
		return announcementMapper.toDtoList(latest);
	}

	// 發布公告
	@Override
	public AnnouncementDto createAnnouncement(AnnouncementEditDto dto, Integer authorId) {
		if (moderationConfig.isAnnouncementCheck() && (!aiModerationService.isAllowed(dto.getTitle()) || !aiModerationService.isAllowed(dto.getContent()))) {
			 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "公告內容不當");
		}
		Announcement entity = announcementMapper.toEntity(dto);
		entity.setAuthorId(authorId);
		Announcement saved = announcementRepository.save(entity);
		AnnouncementDto dtoResult = announcementMapper.toDto(saved);
		broadcastLatest();
		return dtoResult;
	}

	// 取得所有公告
	@Override
	public List<AnnouncementDto> getAllAnnouncements() {
		List<Announcement> all = announcementRepository.findByAnnouncementActiveTrueOrderByCreatedTimeDesc();
		return announcementMapper.toDtoList(all);
	}

	// 公告分頁
	@Override
	public List<AnnouncementDto> getAnnouncementsByPage(int page) {
		List<Announcement> result = announcementRepository.findAllByOrderByCreatedTimeDesc(PageRequest.of(page, 10));
		return announcementMapper.toDtoList(result);
	}

	// ID查詢公告
	@Override
	public AnnouncementDto getAnnouncementById(Long id) {
		Announcement announcement = announcementRepository.findById(id)
				.orElseThrow(() -> new AnnouncementNotFoundException("找不到公告:ID=" + id));
		return announcementMapper.toDto(announcement);
	}

	// 搜尋公告
	@Override
	public List<AnnouncementDto> searchAnnouncements(String keyword, String startDate, String endDate) {
		LocalDateTime start = null;
		LocalDateTime end = null;
		try {
			if (startDate != null && !startDate.isEmpty()) {
				start = LocalDate.parse(startDate).atStartOfDay();
			}
			if (endDate != null && !endDate.isEmpty()) {
				end = LocalDate.parse(endDate).atTime(23, 59, 59);
			}
		} catch (DateTimeParseException e) {
			 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "日期格式錯誤(yyyy-MM-dd)");
		}
		List<Announcement> result = announcementRepository.searchByKeyword(keyword, start, end);
		return announcementMapper.toDtoList(result);
	}

	// 更新公告
	@Override
	public AnnouncementDto updateAnnouncement(Long id, AnnouncementEditDto dto) {
		Announcement existing = announcementRepository.findById(id)
				.orElseThrow(() -> new AnnouncementNotFoundException("找不到要更新的公告:ID=" + id));
		if (moderationConfig.isAnnouncementCheck() &&(!aiModerationService.isAllowed(dto.getTitle()) || !aiModerationService.isAllowed(dto.getContent()))) {
			 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "公告內容不當");
		}
		existing.setTitle(dto.getTitle());
		existing.setContent(dto.getContent());
		if (dto.getAnnouncementActive() != null) {
			existing.setAnnouncementActive(dto.getAnnouncementActive());
		}
		Announcement update = announcementRepository.save(existing);
		AnnouncementDto dtoResult = announcementMapper.toDto(update);
		broadcastLatest();
		return dtoResult;
	}

	// 隱藏公告
	@Override
	public AnnouncementDto setAnnouncementActive(Long id, Boolean active) {
		Announcement announcement = announcementRepository.findById(id)
				.orElseThrow(() -> new AnnouncementNotFoundException("找不到公告:ID=" + id));
		announcement.setAnnouncementActive(active);
		Announcement update = announcementRepository.save(announcement);
		AnnouncementDto dtoResult = announcementMapper.toDto(update);
		broadcastLatest();
		return dtoResult;
	}

	// 刪除公告
	@Override
	public void deleteAnnouncement(Long id) {
		if (!announcementRepository.existsById(id)) {
			throw new AnnouncementNotFoundException("找不到要刪除的公告:ID=" + id);
		}
		announcementRepository.deleteById(id);
		System.out.println("公告已刪除:ID=" + id);
		broadcastLatest();
	}

}
