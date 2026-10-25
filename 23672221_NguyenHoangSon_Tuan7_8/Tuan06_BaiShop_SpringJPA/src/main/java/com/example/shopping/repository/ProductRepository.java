package com.example.shopping.repository;
import com.example.shopping.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAllByOrderByNameAsc();
    @EntityGraph(attributePaths = "comments") Optional<Product> findWithCommentsById(Integer id);
}
