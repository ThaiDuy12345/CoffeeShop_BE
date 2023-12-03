package com.duan.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.duan.entity.CategoryEntity;
import com.duan.entity.ProductEntity;
import com.duan.entity.ProductEntityStatistic;
import com.duan.entity.ProductEntityStatistic2;
import com.duan.entity.ProductEntityStatistic3;
import com.duan.entity.ProductEntityWithMinPrice;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer>{
	List<ProductEntity> findByCategoryEntity(CategoryEntity category);
	Boolean existsByProductId(Integer productId);
    Integer countByProductActive(Boolean productActive);
	@Query(value =
    "SELECT  " +
    "   p.productId as productId, " + 
    "   p.productName as productName, " + 
    "   p.productDescription as productDescription, " + 
    "   p.productIsPopular as productIsPopular, " +
    "   p.productActive as productActive, " + 
    "   p.productCreationDate as productCreationDate, " + 
    "   p.productImageUrl as productImageUrl, " + 
    "   p.categoryEntity as categoryEntity, " + 
    "   MIN(ps.productSizePrice) as productMinPrice " +
    "FROM ProductEntity p " +
    "INNER JOIN ProductSizeEntity ps ON ps.productEntity.productId = p.productId " +
    "GROUP BY p, p.categoryEntity"
	)
	List<ProductEntityWithMinPrice> findAllProductWithMinPrice();


    @Query(value = 
    "SELECT " +
    "   COALESCE(SUM(do.Detail_Order_Product_Quantity), 0) " +
    "FROM Detail_Order do " +
    "INNER JOIN Ordering o ON o.Ordering_ID = do.Ordering_ID " +
    "INNER JOIN Product_Size ps ON do.Product_Size_ID = ps.Product_Size_ID " +
    "INNER JOIN Product p ON p.Product_ID = ps.Product_ID " +
    "WHERE p.Product_ID  = ?1 AND o.Ordering_Status = 4" 
    , nativeQuery = true)
    Integer getSoldQuantityByProductId(Integer productId);

    @Query(value = 
    "SELECT " +
    "    p.Product_ID as productId, " +
    "    p.Product_Name as productName, " +
    "    p.Product_Active as productActive, " +
    "    COALESCE(SUM(do.Detail_Order_Product_Quantity), 0) as productSoldQuantity " +
    "FROM Detail_Order do " +
    "INNER JOIN Ordering o ON o.Ordering_ID = do.Ordering_ID " +
    "INNER JOIN Product_Size ps ON do.Product_Size_ID = ps.Product_Size_ID " +
    "INNER JOIN Product p ON p.Product_ID = ps.Product_ID " +
    "WHERE o.Ordering_Status = 4 " +
    "GROUP BY p.Product_ID, p.Product_Name, p.Product_Active " +
    "ORDER BY productSoldQuantity DESC "
    , nativeQuery = true)
    List<ProductEntityStatistic> getProductStatisticsBySoldQuantity();

    @Query(value = 
    "SELECT " +
    "    p.Product_ID as productId, " +
    "    p.Product_Name as productName, " +
    "    p.Product_Active as productActive, " +
    "    COALESCE(COUNT(fb.Product_Id), 0) as productFeedbackQuantity " +
    "FROM Feedback fb " +
    "INNER JOIN Product p ON p.Product_ID = fb.Product_ID " +
    "GROUP BY p.Product_ID, p.Product_Name, p.Product_Active " +
    "ORDER BY productFeedbackQuantity DESC "
    , nativeQuery = true)
    List<ProductEntityStatistic2> getProductStatisticsByFeedbackQuantity();

    @Query(value = 
    "SELECT  " +
    "   o.Ordering_Creation_Date as date, " +
    "   COALESCE(SUM(do.Detail_Order_Product_Quantity), 0) as productSoldQuantity " +
    "FROM Detail_Order do " +
    "INNER JOIN Ordering o ON o.Ordering_ID = do.Ordering_ID " +
    "INNER JOIN Product_Size ps ON do.Product_Size_ID = ps.Product_Size_ID " +
    "INNER JOIN Product p ON p.Product_ID = ps.Product_ID " +
    "WHERE o.Ordering_Status = 4 AND o.Ordering_Creation_Date >= ?1 AND  " +
    "o.Ordering_Creation_Date <=  ?2 " +
    "GROUP BY o.Ordering_Creation_Date " +
    "ORDER BY o.Ordering_Creation_Date ASC"
    , nativeQuery = true)
    List<ProductEntityStatistic3> getProductStatisticsBySoldQuantityAndDate(Date startDate, Date endDate);
}
