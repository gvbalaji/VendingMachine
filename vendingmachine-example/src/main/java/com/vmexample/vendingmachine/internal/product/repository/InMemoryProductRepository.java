package com.vmexample.vendingmachine.internal.product.repository;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.vmexample.vendingmachine.internal.product.NoProductsFoundException;
import com.vmexample.vendingmachine.internal.product.Product;
import com.vmexample.vendingmachine.internal.product.ProductCode;
import com.vmexample.vendingmachine.internal.product.ProductNotFoundException;
import com.vmexample.vendingmachine.internal.product.UnableToAddProductException;

@Repository
public class InMemoryProductRepository implements ProductRepository {

	private final Map<ProductCode, List<Product>> productMap;
	
	public InMemoryProductRepository() {
		productMap = new EnumMap<>(ProductCode.class);
	}

	public synchronized Set<Product> getAvailableProducts() throws NoProductsFoundException
	{
		
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
		if(productSet.size() == 0)
			throw new NoProductsFoundException("No products found");
		return productSet;
		
	}

	public synchronized Boolean checkIfProductAvailable(ProductCode productCode) {
		
		return getProductCount(productCode) > 0 ? true : false;
	}

	public synchronized Integer getProductCount(ProductCode productCode) {
		int productCount = 0;
		List<Product> productList = productMap.get(productCode);
		if(productList != null)
			productCount = productList.size();
		return productCount;
	}

	public synchronized Product buyProduct(ProductCode productCode) throws ProductNotFoundException {
		List<Product> productList = productMap.get(productCode);
		if(productList == null || productList.size() == 0)
			throw new ProductNotFoundException();
		return productList.remove(0);
	}
	
	public synchronized Product getProduct(ProductCode productCode) throws ProductNotFoundException {
		List<Product> productList = productMap.get(productCode);
		if(productList == null || productList.size() == 0)
			throw new ProductNotFoundException();
		final Product product =  productList.get(0);
		return product;
	}

	public synchronized void addProduct(Product product)  throws UnableToAddProductException {
		try {
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
		catch (Exception ex)
		{
			throw new UnableToAddProductException(ex);
		}

	}

	@Override
	public synchronized void addProducts(List<Product> products) throws UnableToAddProductException {
		
		List<Product> addedProducts = new ArrayList<>();
		try {
			for(Product product : products)
			{
				addProduct(product);
				addedProducts.add(product);
			}
		} catch (Exception e) {
			removeProducts(addedProducts);
			throw new UnableToAddProductException(e);
		}
		
	}
	
	private void removeProducts (List<Product> products)
	{
		try {
			for(Product product : products)
			{
				if(productMap.containsKey(product.getProductCode()))
				{
					productMap.get(product.getProductCode()).remove(product);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
