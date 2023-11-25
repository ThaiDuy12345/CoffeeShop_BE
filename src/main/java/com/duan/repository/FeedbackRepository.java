package com.duan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.duan.entity.FeedbackEntity;
import com.duan.entity.FeedbackId;

import jakarta.transaction.Transactional;

public interface FeedbackRepository extends JpaRepository<FeedbackEntity, FeedbackId>{
  List<FeedbackEntity> findAllByFeedbackIdAccountPhone(String accountPhone);  
  List<FeedbackEntity> findAllByFeedbackIdProductId(Integer productID);  
  List<FeedbackEntity> findAllByFeedbackIdProductIdAndFeedbackIdAccountPhone(Integer productID, String accountPhone);

  @Modifying
  @Transactional
  @Query(value = 
    "INSERT INTO feedback(Product_ID, Account_Phone, Feedback_Rate, Feedback_Comment) " + 
    "VALUES (?1, ?2, ?3, ?4)"
  , nativeQuery = true)
  void insertNewFeedback(
    Integer productId,
    String accountPhone,
    Integer feedbackRate,
    String feedbackComment
  );

  @Modifying
  @Transactional
  @Query(value = 
    "UPDATE feedback " +
    "SET Feedback_Rate = ?1, Feedback_Comment = ?2, Feedback_Active = ?3 " + 
    "WHERE Product_ID = ?4 AND Account_Phone like ?5 "
  , nativeQuery = true)
  void updateNewFeedback(
    Integer feedbackRate,
    String feedbackComment,
    Boolean feedbackActive,
    Integer productId,
    String accountPhone
  );
}
