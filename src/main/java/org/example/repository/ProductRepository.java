package org.example.repository;

import org.example.entityes.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findByCategory(String category);
    List<Product> findByManufacturer(String manufacturer);
    List<Product> findByModel(String model);
    List<Product> findByCategoryAndManufacturer(String category, String manufacturer);
    List<Product> findByCategoryAndModel(String category, String model);
    List<Product> findByManufacturerAndModel(String manufacturer, String model);
    List<Product> findByCategoryAndManufacturerAndModel(String category, String manufacturer, String model);
    List<Product> findAll();
    Product findById(long id);
}
