package com.fc.shimpyo_be.domain.product.repository;


import com.fc.shimpyo_be.domain.product.entity.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;


public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where p.name like %:keyword% or p.address like %:keyword%")
    List<Product> findAllContainingKeyword(@Param("keyword") String keyword, Pageable pageable);

//    @Query(value = "select count(p)"
//        + "from reservation_product p join reservation r on p.reservation_id = r.id"
//        + "where p.room_id = :roomId AND DATE(r.accomodation_date) BETWEEN :startDate AND :endDate"
//        + "GROUP BY r.accomodation_date"
//        + "order by 1 desc"
//        + "limit 1", nativeQuery = true)
//    Long findRoomReservationCount(@Param("roomdId") Long roomdId,
//        @Param("startDate") String startDate, @Param("endDate") String endDate);

}
