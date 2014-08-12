package com.vmexample.vendingmachine.internal.product.service;

import java.util.List;
import java.util.Set;

import com.vmexample.vendingmachine.internal.product.Product;
import com.vmexample.vendingmachine.internal.product.ProductCode;
import com.vmexample.vendingmachine.internal.product.ProductNotFoundException;
import com.vmexample.vendingmachine.internal.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;


public class DefaultProductService implements ProductService {

	final ProductRepository productRepository;
	
	@Autowired
	public DefaultProductService(ProductRepository productRepository) {
		super();
		this.productRepository = productRepository;
	}

	@Override
	public Set<Product> getAvailableProducts() {
		
		return productRepository.getAvailableProducts();
	}

	@Override
	public Boolean checkIfProductAvailable(ProductCode productCode) {
		
		return productRepository.checkIfProductAvailable(productCode);
	}

	@Override
	public int getProductCount(ProductCode productCode) {
		
		return productRepository.getProductCount(productCode);
	}

	@Override
	public Product buyProduct(ProductCode productCode)
			throws ProductNotFoundException {
		return productRepository.buyProduct(productCode);
	}

	@Override
	public void addProduct(Product product) {
		productRepository.addProduct(product);

	}

	@Override
	public void addProducts(List<Product> products) {
		productRepository.addProducts(products);

	}

}
