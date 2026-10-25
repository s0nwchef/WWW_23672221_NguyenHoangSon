package com.example.shopping.service;
import com.example.shopping.entity.PurchaseOrder;
import com.example.shopping.repository.PurchaseOrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
@Service public class OrderService {
    private final PurchaseOrderRepository repository;
    public OrderService(PurchaseOrderRepository repository) { this.repository = repository; }
    public List<PurchaseOrder> findAll() { return repository.findAllByOrderByOrderDateDesc(); }
    public PurchaseOrder findById(Integer id) { return repository.findDetailById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy hóa đơn có ID " + id)); }
}
