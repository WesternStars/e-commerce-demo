package com.bymdev.artem.ecommercedemo.repository;

import com.bymdev.artem.ecommercedemo.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer>, PagingAndSortingRepository<Order, Integer> {

    @NativeQuery(value = """
            SELECT SUM(total_amount) AS sum, CAST(created_at AS DATE) AS data
            FROM e_order
            WHERE CAST(created_at AS DATE) >= ?1 AND CAST(created_at AS DATE) <= ?2
            GROUP BY CAST(created_at AS DATE)
            ORDER BY data DESC
           """,
            countQuery = "SELECT count(*) FROM e_order WHERE CAST(created_at AS DATE) >= ?1 AND CAST(created_at AS DATE) <= ?2 GROUP BY CAST(created_at AS DATE)")
    Page<Map<String, Object>> findSumByDayReportWithPagination(LocalDate from, LocalDate to, Pageable pageable);

    @NativeQuery(value = """
            SELECT o.id
            FROM product AS p
                INNER JOIN order_item AS oi ON p.sku = oi.product_sku
                INNER JOIN e_order AS o ON oi.order_id = o.id
            WHERE p.sku = ?1
            """)
    List<Map<String, Object>> findAllOrderWithProduct(String sku);
}
