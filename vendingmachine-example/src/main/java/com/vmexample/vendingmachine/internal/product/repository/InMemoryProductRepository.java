package com.vmexample.vendingmachine.internal.product.repository;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vmexample.vendingmachine.internal.product.Product;
import com.vmexample.vendingmachine.internal.product.ProductCode;
import com.vmexample.vendingmachine.internal.product.ProductNotFoundException;

public class InMemoryProductRepository implements ProductRepository {

	private Map<ProductCode, List<Product>> productMap;
	
	public InMemoryProductRepository() {
		productMap = new EnumMap<>(ProductCode.class);
	}

	public Set<Product> getAvailableProducts() {
		
		Set<Product> productSet = new HashSet<>();
		for(ProductCode productCode : productMap.keySet())
		{
			try {
				productSet.add(getProduct(productCode));
			}
			catch (Exception ex)
			{
				//Do Nothing.
			}
			
		}
		return productSet;
		
	}

	public Boolean checkIfProductAvailable(ProductCode productCode) {
		
		return getProductCount(productCode) > 0 ? true : false;
	}

	public int getProductCount(ProductCode productCode) {
		int productCount = 0;
		List<Product> productList = productMap.get(productCode);
		if(productList != null)
			productCount = productList.size();
		return productCount;
	}

	public Product buyProduct(ProductCode productCode) throws ProductNotFoundException {
		List<Product> productList = productMap.get(productCode);
		if(productList == null || productList.size() == 0)
			throw new ProductNotFoundException();
		return productList.remove(0);
	}
	
	private Product getProduct(ProductCode productCode) throws ProductNotFoundException {
		List<Product> productList = productMap.get(productCode);
		if(productList == null || productList.size() == 0)
			throw new ProductNotFoundException();
		return productList.get(0);
	}

	public void addProduct(Product product) {
		if(productMap.containsKey(product.getProductCode()))
		{
			productMap.get(product.getProductCode()).add(product);
		}
		else {
		 List<Product> productList = new ArrayList<>();
		 productList.add(product);
		 productMap.put(product.getProductCode(), productList);	
		}

	}

	@Override
	public void addProducts(List<Product> products) {
		for(Product product : products)
			addProduct(product);
		
	}

}
