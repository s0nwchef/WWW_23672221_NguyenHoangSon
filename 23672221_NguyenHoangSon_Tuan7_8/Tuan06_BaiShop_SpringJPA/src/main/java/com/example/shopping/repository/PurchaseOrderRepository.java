package com.example.shopping.repository;
import com.example.shopping.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Integer> {
    @EntityGraph(attributePaths = "customer") List<PurchaseOrder> findAllByOrderByOrderDateDesc();
    @EntityGraph(attributePaths = {"customer", "lines", "lines.product"}) Optional<PurchaseOrder> findDetailById(Integer id);
}
