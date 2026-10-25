package com.example.shopping.repository;
import com.example.shopping.entity.Customer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    List<Customer> findAllByOrderByNameAsc();
    @EntityGraph(attributePaths = "orders") Optional<Customer> findWithOrdersById(Integer id);
}
