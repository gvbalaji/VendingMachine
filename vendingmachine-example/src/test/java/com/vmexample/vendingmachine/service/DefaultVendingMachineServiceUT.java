package com.vmexample.vendingmachine.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.vmexample.vendingmachine.internal.cash.CurrencyCode;
import com.vmexample.vendingmachine.internal.cash.Money;
import com.vmexample.vendingmachine.internal.cash.MoneyEntity;
import com.vmexample.vendingmachine.internal.cash.UnableToAddCashException;
import com.vmexample.vendingmachine.internal.cash.service.CashService;
import com.vmexample.vendingmachine.internal.product.Product;
import com.vmexample.vendingmachine.internal.product.ProductCode;
import com.vmexample.vendingmachine.internal.product.UnableToAddProductException;
import com.vmexample.vendingmachine.internal.product.service.ProductService;

public class DefaultVendingMachineServiceUT {

	
	public DefaultVendingMachineServiceUT() {
	
	}
    
	private  CashService cashService;
	private  ProductService productService;
	private DefaultVendingMachineService defaultVendingMachineService;
	@Before
	public void setUp() throws Exception {
		cashService = mock(CashService.class);
		productService = mock(ProductService.class);
		defaultVendingMachineService = new DefaultVendingMachineService(cashService,productService);
		
	}

	@Test
	public void productCanBeAdded() {
		Product product = new Product(ProductCode.COKE,new Money(new BigDecimal(75),Currency.getInstance(Locale.getDefault())));
		try {
			doNothing().when(productService).addProduct(product);
			defaultVendingMachineService.addProduct(product);
			
		} catch (UnableToAddProductException e) {
			fail("No exceptions should be thrown");
		}
		
	}
	
	@Test(expected = UnableToAddProductException.class)
	public void addProductThrowsException() throws UnableToAddProductException  {
		Product product = new Product(ProductCode.COKE,new Money(new BigDecimal(75),Currency.getInstance(Locale.getDefault())));
		
		doThrow(new UnableToAddProductException()).when(productService).addProduct(product);
		defaultVendingMachineService.addProduct(product);
		
	}
	
	@Test
	public void moneyCanBeDeposited()
	{
		MoneyEntity money = new MoneyEntity(CurrencyCode.QUARTER);
		try {
			doNothing().when(cashService).depositCash(money);
			defaultVendingMachineService.depositCash(money);
			
		} catch (UnableToAddCashException e) {
			fail("No exceptions should be thrown");
		}
		
	}
	
	@Test (expected = UnableToAddCashException.class)
	public void depositMoneyThrowsException() throws UnableToAddCashException
	{
		MoneyEntity money = new MoneyEntity(CurrencyCode.QUARTER);
		doThrow(new UnableToAddCashException()).when(cashService).depositCash(money);
		defaultVendingMachineService.depositCash(money);
	}
	
	

}
