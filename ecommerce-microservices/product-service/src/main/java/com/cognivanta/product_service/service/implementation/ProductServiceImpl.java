package com.cognivanta.product_service.service.implementation;

import com.cognivanta.product_service.repository.ProductRepository;
import com.cognivanta.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
}
