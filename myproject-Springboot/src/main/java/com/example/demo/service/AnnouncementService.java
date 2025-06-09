package com.example.demo.service;

import java.util.List;

import com.example.demo.model.dto.AnnouncementDto;
import com.example.demo.model.dto.AnnouncementEditDto;

public interface AnnouncementService {

	List<AnnouncementDto> getLatestAnnouncements();

	List<AnnouncementDto> getAllAnnouncements();
	
	List<AnnouncementDto> searchAnnouncements(String keyword, String startDate, String endDate);

	AnnouncementDto getAnnouncementById(Long id);

	AnnouncementDto createAnnouncement(AnnouncementEditDto dto, Integer authorId);

	AnnouncementDto updateAnnouncement(Long id, AnnouncementEditDto dto);

	AnnouncementDto setAnnouncementActive(Long id, Boolean active);

	void deleteAnnouncement(Long id);

}
