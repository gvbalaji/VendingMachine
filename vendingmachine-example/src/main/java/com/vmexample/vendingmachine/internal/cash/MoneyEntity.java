package com.vmexample.vendingmachine.internal.cash;


public class MoneyEntity {
	
	private final  CurrencyCode currencyCode;
	private final Double totalValue;
	
	public MoneyEntity(CurrencyCode currencyCode) {
		super();
		this.currencyCode = currencyCode;
		totalValue = currencyCode.value();
	}

	public CurrencyCode getCurrencyCode() {
		return currencyCode;
	}

	public Double getTotalValue() {
		return totalValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((currencyCode == null) ? 0 : currencyCode.hashCode());
		result = prime * result
				+ ((totalValue == null) ? 0 : totalValue.hashCode());
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
		MoneyEntity other = (MoneyEntity) obj;
		if (currencyCode != other.currencyCode)
			return false;
		if (totalValue == null) {
			if (other.totalValue != null)
				return false;
		} else if (!totalValue.equals(other.totalValue))
			return false;
		return true;
	}
	
	
	

}
