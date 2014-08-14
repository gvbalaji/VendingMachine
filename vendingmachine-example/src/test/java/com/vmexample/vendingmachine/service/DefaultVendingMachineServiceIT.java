package com.vmexample.vendingmachine.service;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.vmexample.vendingmachine.VendingMachineOutput;
import com.vmexample.vendingmachine.client.Messages;
import com.vmexample.vendingmachine.internal.cash.CurrencyCode;
import com.vmexample.vendingmachine.internal.cash.Money;
import com.vmexample.vendingmachine.internal.cash.MoneyEntity;
import com.vmexample.vendingmachine.internal.cash.NotEnoughMoneyLeftException;
import com.vmexample.vendingmachine.internal.cash.UnableToAddCashException;
import com.vmexample.vendingmachine.internal.product.NoProductsFoundException;
import com.vmexample.vendingmachine.internal.product.Product;
import com.vmexample.vendingmachine.internal.product.ProductCode;
import com.vmexample.vendingmachine.internal.product.ProductNotFoundException;
import com.vmexample.vendingmachine.internal.product.UnableToAddProductException;

public class DefaultVendingMachineServiceIT {

	private VendingMachineService vendingMachine;
	private AbstractApplicationContext context;
	
	public DefaultVendingMachineServiceIT() {
	}

	@Before
	public void setUp() throws Exception {
		context = new ClassPathXmlApplicationContext("classpath:/com/vmexample/vendingmachine/ApplicationContext.xml"); //$NON-NLS-1$
		vendingMachine = context.getBean(VendingMachineService.class);
	}

	@After
	public void tearDown() throws Exception {
		context.close();
	}

	
	@Test
	public void depositCashHappyPath()
	{
		for(int count = 0 ; count < 100; count++)
		{
			try {
				MoneyEntity money = new MoneyEntity(CurrencyCode.QUARTER);
				vendingMachine.depositCash(money);
				
			} catch (UnableToAddCashException e) {
				fail("Unable to deposit Cash");
			}
		}
		
	}
	
	@Test
	public void addProductHappyPath()
	{
		Product product = new Product(ProductCode.COKE,new Money(new BigDecimal(75),Currency.getInstance(Locale.getDefault())));
		for(int count = 0 ; count < 10; count++)
		{
				try {
					vendingMachine.addProduct(product);
					Set<Product> productSet = vendingMachine.getAvailableProducts();
					assert(productSet.size() == 1);
					assert(productSet.contains(product));
					
				} catch (UnableToAddProductException e) {
					fail("Unable to add product");
				}	
				catch (NoProductsFoundException e) {
					fail("Product was not addded");
				}	
		}
	}
	
	@Test
	public void buyProductHappyPath()
	{
		Map<CurrencyCode, List<MoneyEntity>> cashMap = new EnumMap<>(CurrencyCode.class);
		MoneyEntity money = new MoneyEntity(CurrencyCode.DOLLAR);
		List<MoneyEntity> moneyEntityList = new ArrayList<>();
		moneyEntityList.add(money);
		cashMap.put(CurrencyCode.DOLLAR, moneyEntityList);
		
		try {
			depositCashHappyPath();
			addProductHappyPath();
			VendingMachineOutput output = vendingMachine.buyProduct(ProductCode.COKE, cashMap);
			if(output != null)
		    {
		    	for(CurrencyCode currencyCode: output.getCashChange().keySet())
		    	{
		    		//print(Messages.getString("VendingMachine.ChangeTitle") + currencyCode.name() + Messages.getString("VendingMachine.MessageDelimitter") + output.getCashChange().get(currencyCode).size()); //$NON-NLS-1$ //$NON-NLS-2$
		    	}
		    }
			assertNotNull(output);
			assertEquals(ProductCode.COKE,output.getProduct().getProductCode());
		} catch (ProductNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotEnoughMoneyLeftException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnableToAddCashException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
