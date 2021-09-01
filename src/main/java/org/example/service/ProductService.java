package org.example.service;

import org.example.entityes.Product;
import org.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Value("${upload.path}")
    private String uploadPath;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(long id) {
        return productRepository.findById(id);
    }

    public List<Product> addNewProduct(Product product, MultipartFile file) {
        if (file != null) {
            File uploadDir = new File(uploadPath);
            if (uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();

            try {
                file.transferTo(new File(uploadPath + "/" + resultFileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            product.setFilename(resultFileName);
        }

        productRepository.save(product);
        return productRepository.findAll();
    }

    public void update(Product product, MultipartFile file) {
        productRepository.deleteById(product.getId());
        if (file.isEmpty()) {
            productRepository.save(product);
        } else {
            addNewProduct(product, file);
        }
    }

    public void delete(long id) {
        productRepository.deleteById(id);
    }

    public List<Product> findProduct(String category, String manufacturer, String priceFrom, String priceTo) {
        System.out.println("category " + category);
        System.out.println("manufacturer " + manufacturer);
        System.out.println("priceFrom " + priceFrom);
        System.out.println("priceTo " + priceTo);
        List<Product> productList = productRepository.findAll();

        if (!category.isEmpty()) {
            productList.removeIf(product -> !product.getCategory().equals(category));
        }
        if (!manufacturer.isEmpty()) {
            productList.removeIf(product -> !product.getCategory().equals(category));
        }
        if (!priceFrom.isEmpty()) {
            productList.removeIf(product -> product.getPrice() < Double.parseDouble(priceFrom));
        }
        if (!priceTo.isEmpty()) {
            productList.removeIf(product -> product.getPrice() > Double.parseDouble(priceTo));
        }

        return productList;

    }
}
