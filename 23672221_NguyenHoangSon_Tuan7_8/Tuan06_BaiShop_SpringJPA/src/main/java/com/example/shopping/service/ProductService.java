package com.example.shopping.service;
import com.example.shopping.entity.Product;
import com.example.shopping.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
@Service public class ProductService {
    private final ProductRepository repository;
    public ProductService(ProductRepository repository) { this.repository = repository; }
    public List<Product> findAll() { return repository.findAllByOrderByNameAsc(); }
    public Product findById(Integer id) { return repository.findWithCommentsById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm có ID " + id)); }
}
