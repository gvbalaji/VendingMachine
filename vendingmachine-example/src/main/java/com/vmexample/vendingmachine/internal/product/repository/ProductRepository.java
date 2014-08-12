package com.vmexample.vendingmachine.internal.product.repository;

import java.util.List;
import java.util.Set;

import com.vmexample.vendingmachine.internal.product.Product;
import com.vmexample.vendingmachine.internal.product.ProductCode;
import com.vmexample.vendingmachine.internal.product.ProductNotFoundException;

public interface ProductRepository {

	public Set<Product> getAvailableProducts();
	public Boolean checkIfProductAvailable(ProductCode productCode);
	public int getProductCount(ProductCode productCode);
	public Product buyProduct(ProductCode productCode) throws ProductNotFoundException;
	public void addProduct(Product product);
	public void addProducts(List<Product> products);
	
	
}
