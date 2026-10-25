package com.example.shopping.service;
import com.example.shopping.entity.Customer;
import com.example.shopping.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
@Service public class CustomerService {
    private final CustomerRepository repository;
    public CustomerService(CustomerRepository repository) { this.repository = repository; }
    public List<Customer> findAll() { return repository.findAllByOrderByNameAsc(); }
    public Customer findById(Integer id) { return repository.findWithOrdersById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy khách hàng có ID " + id)); }
}
