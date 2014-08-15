package com.vmexample.vendingmachine.internal.product.repository;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.stereotype.Repository;

import com.vmexample.vendingmachine.client.Messages;
import com.vmexample.vendingmachine.internal.product.NoProductsFoundException;
import com.vmexample.vendingmachine.internal.product.Product;
import com.vmexample.vendingmachine.internal.product.ProductCode;
import com.vmexample.vendingmachine.internal.product.ProductNotFoundException;
import com.vmexample.vendingmachine.internal.product.UnableToAddProductException;

@Repository
public class InMemoryProductRepository implements ProductRepository {

	private final Map<ProductCode, List<Product>> productMap;
	private final ReadWriteLock  lock;
	private final Lock readLock, writeLock;
	
	public InMemoryProductRepository() {
		productMap = new EnumMap<>(ProductCode.class);
		lock = new ReentrantReadWriteLock();
	    readLock = lock.readLock();
	    writeLock = lock.writeLock();		
	}

	public  Set<Product> getAvailableProducts() throws NoProductsFoundException
	{
		
		Set<Product> productSet = new HashSet<>();
		try {
			readLock.lock();
			for (ProductCode productCode : productMap.keySet()) {
				try {
					productSet.add(getProduct(productCode));
				} catch (Exception ex) {
					//Do Nothing.
				}

			}
			if (productSet.size() == 0)
				throw new NoProductsFoundException(Messages.getString(Messages.NO_PRODUCT_FOUND)); //$NON-NLS-1$
		} 
		
		finally {
			readLock.unlock();
		}
		return productSet;
		
	}

	public Boolean checkIfProductAvailable(ProductCode productCode) {
		
		Integer productCount = 0;
	    try {
	    	readLock.lock();
	    	productCount = 	getProductCount(productCode);
		}
	    finally {
	    	readLock.unlock();
	    }
		
		return productCount > 0 ? true : false;
	}

	public  Integer getProductCount(ProductCode productCode) {
		int productCount = 0;
		try {
			readLock.lock();
			List<Product> productList = productMap.get(productCode);
			if(productList != null)
				productCount = productList.size();
		}
		finally {
			readLock.unlock();
		}
		return productCount;
	}

	public  Product buyProduct(ProductCode productCode) throws ProductNotFoundException {
		Product product = null;
		try {
			writeLock.lock();
			List<Product> productList = productMap.get(productCode);
			if(productList == null || productList.size() == 0)
				throw new ProductNotFoundException();
			product = productList.remove(0);
		}
		finally {
			writeLock.unlock();
		}
		return product;
	}
	
	public  Product getProduct(ProductCode productCode) throws ProductNotFoundException {
		Product product = null;
		try {
			readLock.lock();
			List<Product> productList = productMap.get(productCode);
			if(productList == null || productList.size() == 0)
				throw new ProductNotFoundException();
			product =  productList.get(0);
		}
		finally {
			readLock.unlock();
		}
		return product;
	}

	public  void addProduct(Product product)  throws UnableToAddProductException {
		try {
			writeLock.lock();
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
			throw new UnableToAddProductException( Messages.getString(Messages.UNABLE_TO_ADD_PRODUCT), ex); //$NON-NLS-1$
		}
		finally {
			writeLock.unlock();
		}

	}

	@Override
	public  void addProducts(List<Product> products) throws UnableToAddProductException {
		
		List<Product> addedProducts = new ArrayList<>();
		try {
			writeLock.lock();
			for(Product product : products)
			{
				addProduct(product);
				addedProducts.add(product);
			}
		} catch (Exception e) {
			throw new UnableToAddProductException(e);
		}
		finally {
			if(addedProducts.size() > 0)
				removeProducts(addedProducts);
			writeLock.unlock();
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
