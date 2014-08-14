package com.vmexample.vendingmachine.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmexample.vendingmachine.VendingMachineOutput;
import com.vmexample.vendingmachine.internal.cash.CurrencyCode;
import com.vmexample.vendingmachine.internal.cash.MoneyEntity;
import com.vmexample.vendingmachine.internal.cash.NotEnoughMoneyLeftException;
import com.vmexample.vendingmachine.internal.cash.UnableToAddCashException;
import com.vmexample.vendingmachine.internal.cash.service.CashService;
import com.vmexample.vendingmachine.internal.product.NoProductsFoundException;
import com.vmexample.vendingmachine.internal.product.Product;
import com.vmexample.vendingmachine.internal.product.ProductCode;
import com.vmexample.vendingmachine.internal.product.ProductNotFoundException;
import com.vmexample.vendingmachine.internal.product.UnableToAddProductException;
import com.vmexample.vendingmachine.internal.product.service.ProductService;

@Service
public class DefaultVendingMachineService implements VendingMachineService {

	private final CashService cashService;
	private final ProductService productService;
	
	private static final Object lock = new Object();
	
    @Autowired
	public DefaultVendingMachineService(CashService cashService,
			ProductService productService) {
		this.cashService = cashService;
		this.productService = productService;
	}

	@Override
	public Set<Product> getAvailableProducts() throws NoProductsFoundException {
		Set<Product> products =  productService.getAvailableProducts();
		return products;
	}

	@Override
	public  VendingMachineOutput buyProduct(ProductCode productCode,
			Map<CurrencyCode, List<MoneyEntity>> cashMap)
			throws ProductNotFoundException, NotEnoughMoneyLeftException, UnableToAddCashException {
		
		VendingMachineOutput vendingMachineOutput = null;
		if(productService.checkIfProductAvailable(productCode))
		{
			synchronized(lock){
				Map<CurrencyCode,List<MoneyEntity>> cashChange = cashService.depositAndDispatchChange(productService.getProduct(productCode).getPrice(), Collections.unmodifiableMap(cashMap));
				Product product = productService.buyProduct(productCode);
				vendingMachineOutput = new VendingMachineOutput(product,cashChange);
			}
			
		}
		return vendingMachineOutput;
	}

	@Override
	public void addProduct(Product product) throws UnableToAddProductException {
		productService.addProduct(product);
	}

	@Override
	public void addProducts(List<Product> products) throws UnableToAddProductException {
		productService.addProducts(products);
	}

	@Override
	public void depositCash(MoneyEntity money) throws UnableToAddCashException {
		cashService.depositCash(money);
		
	}

	@Override
	public void depositCash(List<MoneyEntity> money) throws UnableToAddCashException {
		cashService.depositCash(money);
		
	}

}
