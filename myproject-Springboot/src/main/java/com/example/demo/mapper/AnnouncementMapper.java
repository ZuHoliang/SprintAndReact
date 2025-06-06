package com.example.demo.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.dto.AnnouncementDto;
import com.example.demo.model.dto.AnnouncementEditDto;
import com.example.demo.model.entity.Announcement;
import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;

@Component
public class AnnouncementMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private UserRepository userRepository;
	
	//Entity轉DTO
	public AnnouncementDto toDto(Announcement announcement) {
		AnnouncementDto dto = modelMapper.map(announcement, AnnouncementDto.class);
		
		//authorName(需要查詢User)
		if(announcement.getAuthorId() != null) {
			User author = userRepository.findById(announcement.getAuthorId()).orElse(null);
			if(author != null) {
				dto.setAuthorName(author.getUsername()); //使用username顯示名稱
			} else {
				dto.setAuthorName("未知用戶"); //是否要做為exception處理?
			} 
		} else {
				dto.setAuthorName("系統發布"); //系統發布
			}
		return dto;
		}
	
	//DTO轉Entity
	public Announcement toEntity(AnnouncementDto dto) {
		
		Announcement announcement = modelMapper.map(dto, Announcement.class);
		
		//創建時自動設定
		announcement.setAnnouncementId(null);
		announcement.setCreatedTime(null);
		announcement.setUpdateTime(null);
		announcement.setAuthorId(null);
		
		//設定預設值
		if (announcement.getAnnouncementActive() == null) {
			announcement.setAnnouncementActive(true);
		}
		return announcement;
	}
	//AnnouncementEdit Dto 轉 Entity
	public Announcement toEntity(AnnouncementEditDto editDto) {
		Announcement announcement = new Announcement();
		announcement.setTitle(editDto.getTitle());
		announcement.setContent(editDto.getContent());
		announcement.setAnnouncementActive(true);
		
		return announcement;
	}
	
	
	//Entity List 轉換到 DTO List
	
	public List<AnnouncementDto> toDtoList(List<Announcement> announcements){
		return announcements.stream()
							.map(this::toDto)
							.collect(Collectors.toList());
	}
	
	//更新時保留訊息
	public void updateEntityFromDto(AnnouncementDto dto, Announcement targetAnnouncement) {
		//更新時修改欄位
		targetAnnouncement.setTitle(dto.getTitle());
		targetAnnouncement.setContent(dto.getContent());
	 // targetAnnouncement.setAuthorId() 作者暫不改變
	}
	
}
