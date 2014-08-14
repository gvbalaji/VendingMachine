package com.vmexample.vendingmachine.internal.cash.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.vmexample.vendingmachine.internal.cash.MoneyEntity;
import com.vmexample.vendingmachine.internal.cash.CurrencyCode;
import com.vmexample.vendingmachine.internal.cash.NotEnoughMoneyLeftException;
import com.vmexample.vendingmachine.internal.cash.UnableToAddCashException;

public interface CashRepository {

	public void depositCash (MoneyEntity money) throws UnableToAddCashException;
	public void depositCash (List<MoneyEntity> money) throws UnableToAddCashException;
	public Integer getNumberOfCurrrencyEntities(CurrencyCode currencyCode) ;
	public BigDecimal getTotalCurrencyValue(CurrencyCode currencyCode);
	public MoneyEntity debitCash(CurrencyCode currencyCode) throws NotEnoughMoneyLeftException;
	public Map<CurrencyCode,List<MoneyEntity>> debitCash(Map<CurrencyCode,Integer> cashRequestMap) throws NotEnoughMoneyLeftException;
	
}
