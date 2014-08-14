package com.vmexample.vendingmachine.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vmexample.vendingmachine.VendingMachineOutput;
import com.vmexample.vendingmachine.internal.cash.CurrencyCode;
import com.vmexample.vendingmachine.internal.cash.MoneyEntity;
import com.vmexample.vendingmachine.internal.cash.NotEnoughMoneyLeftException;
import com.vmexample.vendingmachine.internal.cash.UnableToAddCashException;
import com.vmexample.vendingmachine.internal.product.NoProductsFoundException;
import com.vmexample.vendingmachine.internal.product.Product;
import com.vmexample.vendingmachine.internal.product.ProductCode;
import com.vmexample.vendingmachine.internal.product.ProductNotFoundException;
import com.vmexample.vendingmachine.internal.product.UnableToAddProductException;

public interface VendingMachineService {
	
	public Set<Product> getAvailableProducts() throws NoProductsFoundException;
	public VendingMachineOutput buyProduct(ProductCode productCode, Map<CurrencyCode,List<MoneyEntity>> cashMap) throws ProductNotFoundException, NotEnoughMoneyLeftException, UnableToAddCashException;

	public void addProduct(Product product) throws UnableToAddProductException;
	public void addProducts(List<Product> products) throws UnableToAddProductException;
	
	public void depositCash (MoneyEntity money) throws UnableToAddCashException;
	public void depositCash (List<MoneyEntity> money) throws UnableToAddCashException;
}
