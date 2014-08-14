package com.vmexample.vendingmachine.internal.product.service;

import java.util.List;
import java.util.Set;

import com.vmexample.vendingmachine.internal.product.NoProductsFoundException;
import com.vmexample.vendingmachine.internal.product.Product;
import com.vmexample.vendingmachine.internal.product.ProductCode;
import com.vmexample.vendingmachine.internal.product.ProductNotFoundException;
import com.vmexample.vendingmachine.internal.product.UnableToAddProductException;

public interface ProductService {
	public Set<Product> getAvailableProducts() throws NoProductsFoundException;
	public Boolean checkIfProductAvailable(ProductCode productCode);
	public int getProductCount(ProductCode productCode);
	public Product buyProduct(ProductCode productCode) throws ProductNotFoundException;
	public void addProduct(Product product) throws UnableToAddProductException;
	public void addProducts(List<Product> products) throws UnableToAddProductException;
	public Product getProduct(ProductCode productCode) throws ProductNotFoundException;

}
