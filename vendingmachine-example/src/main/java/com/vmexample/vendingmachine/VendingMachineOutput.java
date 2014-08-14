package com.vmexample.vendingmachine;

import java.util.List;
import java.util.Map;

import com.vmexample.vendingmachine.internal.cash.CurrencyCode;
import com.vmexample.vendingmachine.internal.cash.MoneyEntity;
import com.vmexample.vendingmachine.internal.product.Product;

public class VendingMachineOutput {

	private final Product product;
	private final  Map<CurrencyCode, List<MoneyEntity>> cashChange;
	
	
	public VendingMachineOutput(Product product,
			Map<CurrencyCode, List<MoneyEntity>> cashChange) {
		super();
		this.product = product;
		this.cashChange = cashChange;
	}


	public Product getProduct() {
		return product;
	}


	public Map<CurrencyCode, List<MoneyEntity>> getCashChange() {
		return cashChange;
	}

	


}
