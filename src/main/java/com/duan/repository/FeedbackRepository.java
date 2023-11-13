package com.duan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.duan.entity.FeedbackEntity;
import com.duan.entity.FeedbackId;
import com.duan.entity.AccountEntity;
import com.duan.entity.DetailOrderEntity;
import com.duan.entity.DiscountEntity;
import com.duan.entity.ProductEntity;

import jakarta.transaction.Transactional;

public interface FeedbackRepository extends JpaRepository<FeedbackEntity, FeedbackId> {
	List<FeedbackEntity> findByProduct(ProductEntity product);

	List<FeedbackEntity> findByAccount(AccountEntity accountEntity);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Transactional
	@Query(value = "INSERT INTO [Feedback] (Feedback_Rate, Feedback_Comment, Product_ID, Account_Phone) "
			+ "VALUES (?1, ?2, ?3, ?4)", nativeQuery = true)
	void insertNewFeedback(int feedbackRate, String feedbackComment, int productId, String accountPhone);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Transactional
	@Query(value = 
			"UPDATE [Feedback] " + 
			"SET Feedback_Rate = ?1 " + 
			"WHERE Feedback_Comment = ?2 AND Feedback_Active = ?3"
	    ,nativeQuery = true)
	void updateFeedback(int feedbackRate,String feedbackComment,boolean feedbackActive);

}
