package com.duan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.duan.entity.SupportEntity;

import jakarta.transaction.Transactional;

public interface SupportRepository extends JpaRepository<SupportEntity, Integer>{
	@Modifying(clearAutomatically	= true, flushAutomatically = true)
	@Transactional
	@Query(value = 
		"INSERT INTO [Support] (Support_Reason, Support_Title,Support_Content) " + 
		"VALUES (?1, ?2, ?3)"
	, nativeQuery = true)
	void insertNewSupport(String supportReason,String supportTitle,String supportContent);
	
	@Modifying(clearAutomatically	= true, flushAutomatically = true)
	@Transactional
	@Query(value = 
		"UPDATE [Support] " + 
		"SET Support_Reason = ?1 " + 
		"WHERE Support_Title = ?2 AND Support_Content = ?3"
	, nativeQuery = true)
	void updateSupport(String supportReason,String supportTitle,String supportContent);
}
