package com.vmexample.vendingmachine.internal.product.service;

import java.util.List;
import java.util.Set;

import com.vmexample.vendingmachine.internal.product.NoProductsFoundException;
import com.vmexample.vendingmachine.internal.product.Product;
import com.vmexample.vendingmachine.internal.product.ProductCode;
import com.vmexample.vendingmachine.internal.product.ProductNotFoundException;
import com.vmexample.vendingmachine.internal.product.UnableToAddProductException;
import com.vmexample.vendingmachine.internal.product.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultProductService implements ProductService {

	final ProductRepository productRepository;
	
	@Autowired
	public DefaultProductService(ProductRepository productRepository) {
		super();
		this.productRepository = productRepository;
	}

	@Override
	public Set<Product> getAvailableProducts() throws NoProductsFoundException {
		
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
	public void addProduct(Product product) throws UnableToAddProductException{
		productRepository.addProduct(product);

	}

	@Override
	public void addProducts(List<Product> products) throws UnableToAddProductException {
		productRepository.addProducts(products);

	}

	@Override
	public Product getProduct(ProductCode productCode)
			throws ProductNotFoundException {
		// TODO Auto-generated method stub
		return productRepository.getProduct(productCode);
	}

}
