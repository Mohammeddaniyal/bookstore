package com.daniyal.bookstore.repository;

import com.daniyal.bookstore.entity.Order;
import com.daniyal.bookstore.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {
    public List<Order> findAllByUser_Username(String username);
    @Query("SELECT o FROM Order o " +
            "JOIN FETCH o.orderItems oi " +
            "JOIN FETCH oi.book WHERE o.id=:orderId")
    Optional<Order> findByIdWithItemsAndBooks(@Param("orderId")Long Id);
    @Query("SELECT DISTINCT o FROM Order o " +
            "JOIN FETCH o.orderItems oi " +
            "JOIN FETCH oi.book " +
            "WHERE o.user.email=:email " +
            "ORDER BY o.createdAt DESC")
    List<Order> findAllByUserEmailWithItemsAndBooks(@Param("email") String email);

    @Query("SELECT DISTINCT o FROM Order o " +
            "JOIN FETCH o.orderItems oi " +
            "JOIN FETCH oi.book " +
            "ORDER BY o.createdAt DESC")
   List<Order> findAllWithItemsAndBooks();

    @EntityGraph(attributePaths = {"orderItems","orderItems.book"})
    Page<Order> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"orderItems", "orderItems.book", "user"})
    @Query(value = """
        SELECT o FROM Order o
        WHERE (:status IS NULL OR o.status = :status)
          AND (:email IS NULL OR LOWER(o.user.email) LIKE LOWER(CONCAT('%', :email, '%')))
        """)
    Page<Order> findByStatusAndUserEmail(
            @Param("status") OrderStatus status,
            @Param("email") String email,
            Pageable pageable);
}
