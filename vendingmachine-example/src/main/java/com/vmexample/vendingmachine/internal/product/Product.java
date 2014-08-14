package com.vmexample.vendingmachine.internal.product;

import com.vmexample.vendingmachine.internal.cash.Money;

public class Product {


	private final ProductCode productCode;
	private final String productName;
	private final Money price;
	
	public Product(ProductCode productCode, Money price) {
		super();
		this.productCode = productCode;
		this.productName = productCode.value();
		this.price = price;
	}
	public ProductCode getProductCode() {
		return productCode;
	}

	public String getProductName() {
		return productName;
	}

	public Money getPrice() {
		return price;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result
				+ ((productCode == null) ? 0 : productCode.hashCode());
		result = prime * result
				+ ((productName == null) ? 0 : productName.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (productCode != other.productCode)
			return false;
		if (productName == null) {
			if (other.productName != null)
				return false;
		} else if (!productName.equals(other.productName))
			return false;
		return true;
	}

	
	

}
