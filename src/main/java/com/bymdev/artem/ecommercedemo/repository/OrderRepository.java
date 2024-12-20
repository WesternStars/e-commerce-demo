package com.bymdev.artem.ecommercedemo.repository;

import com.bymdev.artem.ecommercedemo.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Map;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer>, PagingAndSortingRepository<Order, Integer> {

    @Query("SELECT sum(o.total_amount) AS sum, cast(o.createdAt AS DATE) AS data " +
            "FROM Order o " +
            "GROUP BY cast(o.createdAt AS DATE)")
    Page<Map<String, Object>> findSumReportsByDayWithPagination(LocalDate from, Pageable pageable);

    @NativeQuery(value = "SELECT SUM(total_amount) AS sum, CAST(created_at AS DATE) AS data FROM e_order WHERE CAST(created_at AS DATE) >= ?1 GROUP BY CAST(created_at AS DATE) ORDER BY data DESC",
            countQuery = "SELECT count(*) FROM e_order WHERE CAST(created_at AS DATE) >= ?1 GROUP BY CAST(created_at AS DATE)")
    Page<Map<String, Object>> findNSumReportsByDayWithPagination(LocalDate from, Pageable pageable);
}
