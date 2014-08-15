package com.vmexample.vendingmachine.client;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.vmexample.vendingmachine.VendingMachineOutput;
import com.vmexample.vendingmachine.internal.cash.CurrencyCode;
import com.vmexample.vendingmachine.internal.cash.Money;
import com.vmexample.vendingmachine.internal.cash.MoneyEntity;
import com.vmexample.vendingmachine.internal.cash.NotEnoughMoneyLeftException;
import com.vmexample.vendingmachine.internal.cash.UnableToAddCashException;
import com.vmexample.vendingmachine.internal.product.Product;
import com.vmexample.vendingmachine.internal.product.ProductCode;
import com.vmexample.vendingmachine.internal.product.ProductNotFoundException;
import com.vmexample.vendingmachine.internal.product.UnableToAddProductException;
import com.vmexample.vendingmachine.service.VendingMachineService;

public class VendingMachine {

	private VendingMachineService vendingMachine;
	private AbstractApplicationContext context;
	
	
	public VendingMachine() {
		
	}
	
	public static void main(String [] args)
	{
		VendingMachine vendingMachine = new VendingMachine();
		vendingMachine.setUp();
		vendingMachine.addProduct();
		vendingMachine.depositCash();
		vendingMachine.buyProduct();
		vendingMachine.closeContext();
	}
	
	public void setUp()
	{
		context = new ClassPathXmlApplicationContext("classpath:/com/vmexample/vendingmachine/ApplicationContext.xml"); //$NON-NLS-1$
		vendingMachine = context.getBean(VendingMachineService.class);
	}
	
	public void depositCash()
	{
		for(int count = 0 ; count < 100; count++)
		{
			try {
				MoneyEntity money = new MoneyEntity(CurrencyCode.QUARTER);
				vendingMachine.depositCash(money);
				print(Messages.getString(Messages.MONEY_ADDED)); //$NON-NLS-1$
			} catch (UnableToAddCashException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void addProduct()
	{
		Product product = new Product(ProductCode.COKE,new Money(new BigDecimal(75),Currency.getInstance(Locale.getDefault())));
		for(int count = 0 ; count < 10; count++)
		{
				try {
					vendingMachine.addProduct(product);
					print(Messages.getString(Messages.PRODUCT_ADDED)); //$NON-NLS-1$
				} catch (UnableToAddProductException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
		}
	}
	

	public void buyProduct()
	{
		Map<CurrencyCode, List<MoneyEntity>> cashMap = new EnumMap<>(CurrencyCode.class);
		MoneyEntity money = new MoneyEntity(CurrencyCode.DOLLAR);
		List<MoneyEntity> moneyEntityList = new ArrayList<>();
		moneyEntityList.add(money);
		cashMap.put(CurrencyCode.DOLLAR, moneyEntityList);
		
		try {
			VendingMachineOutput output = vendingMachine.buyProduct(ProductCode.COKE, cashMap);
			print(Messages.getString(Messages.VENDING_MACHINE_PRODUCT_BOUGHT)); //$NON-NLS-1$
		    if(output != null)
		    {
		    	print(Messages.getString(Messages.VENDING_MACHINE_PRODUCT_TITLE) + output.getProduct().getProductName()); //$NON-NLS-1$
		    	for(CurrencyCode currencyCode: output.getCashChange().keySet())
		    	{
		    		print(Messages.getString(Messages.VENDING_MACHINE_CHANGE_TITLE) + currencyCode.name() + Messages.getString(Messages.MESSAGE_DELIMITTER) + output.getCashChange().get(currencyCode).size()); //$NON-NLS-1$ //$NON-NLS-2$
		    	}
		    }
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
	
	private void print(String string) {
		System.out.println(string);
	}

	public void closeContext()
	{
		context.close();
	}

}
