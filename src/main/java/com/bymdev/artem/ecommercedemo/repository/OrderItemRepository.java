package com.bymdev.artem.ecommercedemo.repository;

import com.bymdev.artem.ecommercedemo.entity.OrderItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface OrderItemRepository extends CrudRepository<OrderItem, Integer>, PagingAndSortingRepository<OrderItem, Integer> {
    List<OrderItem> findAllByOrder_Id(Integer orderId);

    Collection<Object> findAllByProduct_Sku(String productSku);

    @Query("SELECT i FROM OrderItem i WHERE (i.order is not null)")
    List<OrderItem> findAllByOrderIdIsNotNull();
}
