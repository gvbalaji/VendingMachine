package com.vmexample.vendingmachine.internal.product;

import java.math.BigDecimal;

public class Product {


	private final ProductCode productCode;
	private final String productName;
	private final BigDecimal price;
	
	public Product(ProductCode productCode, BigDecimal price) {
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

	public BigDecimal getPrice() {
		return price;
	}

	

}
