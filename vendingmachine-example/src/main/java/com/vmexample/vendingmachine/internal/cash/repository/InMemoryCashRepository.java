package com.vmexample.vendingmachine.internal.cash.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.vmexample.vendingmachine.internal.cash.CurrencyCode;
import com.vmexample.vendingmachine.internal.cash.MoneyEntity;
import com.vmexample.vendingmachine.internal.cash.NotEnoughMoneyLeftException;
import com.vmexample.vendingmachine.internal.cash.UnableToAddCashException;

@Repository
public class InMemoryCashRepository implements CashRepository {

	private final Map<CurrencyCode, List<MoneyEntity>> cashMap;

	
	public InMemoryCashRepository() {
		this.cashMap = new EnumMap<>(CurrencyCode.class);
	}

	@Override
	public synchronized void depositCash(MoneyEntity money) throws UnableToAddCashException {
		
		try {
			List<MoneyEntity> moneyList = cashMap.get(money.getCurrencyCode());
			if(moneyList != null){
				moneyList.add(money);
			}
			else {
				moneyList = new ArrayList<>();
				moneyList.add(money);
				cashMap.put(money.getCurrencyCode(), moneyList);
			}
		} catch (Exception e) {
				throw new UnableToAddCashException(e);
		}

	}

	@Override
	public synchronized void depositCash(List<MoneyEntity> moneyList) throws UnableToAddCashException {
		
		List<MoneyEntity> addedMoneyList = new ArrayList<>();
		try {
			for(MoneyEntity money : moneyList)
			{
				depositCash(money);
				addedMoneyList.add(money);
			}
		} catch (Exception e) {
			removeMoneyEntities(addedMoneyList);
			throw new UnableToAddCashException(e);
		}

	}
	
	private void removeMoneyEntities(List<MoneyEntity> moneyList)
	{
		try {
			for(MoneyEntity money : moneyList)
			{
			    if(cashMap.containsKey(money.getCurrencyCode()))	
			    	cashMap.get(money.getCurrencyCode()).remove(money);
			    	
			}
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	@Override
	public synchronized Integer getNumberOfCurrrencyEntities(CurrencyCode currencyCode) {
		
		int numberOfCurrencyEntities = 0;
		List<MoneyEntity> moneyList = cashMap.get(currencyCode);
		if(moneyList != null)
			numberOfCurrencyEntities = moneyList.size();
		return numberOfCurrencyEntities;
	}

	@Override
	public synchronized BigDecimal getTotalCurrencyValue(CurrencyCode currencyCode) {
		
		Integer numberOfEntities = getNumberOfCurrrencyEntities(currencyCode);
		BigDecimal totalValue = new BigDecimal(currencyCode.value() * numberOfEntities);
		return totalValue;
	}

	@Override
	public synchronized MoneyEntity debitCash(CurrencyCode currencyCode) throws NotEnoughMoneyLeftException {
		
		MoneyEntity money = null;
		if(checkIfEnoughCurrencyExists(currencyCode, 0))
			money = cashMap.get(currencyCode).remove(0);
		return money;
	}

	@Override
	public synchronized Map<CurrencyCode, List<MoneyEntity>> debitCash(
			Map<CurrencyCode, Integer> cashRequestMap)  throws NotEnoughMoneyLeftException {
		
		/*
		 * make Sure there is enough money first before taking anything
		 */
		checkIfEnoughCurrencyExists(cashRequestMap);
		
		/*
		 * Now take all the money at once
		 */
		Map<CurrencyCode, List<MoneyEntity>> moneyMap= new EnumMap<>(CurrencyCode.class);
		for(CurrencyCode currencyCode : cashRequestMap.keySet())
		{
			moneyMap.put(currencyCode, getCashList(currencyCode,cashRequestMap.get(currencyCode)));
		}
		
		return moneyMap;
	}
	
	private List<MoneyEntity> getCashList(CurrencyCode currencyCode,Integer currencyCount) throws NotEnoughMoneyLeftException
	{
		List<MoneyEntity> cashList = new ArrayList<>();
		for(int count = 0 ; count < currencyCount; count++)
		{
			cashList.add(debitCash(currencyCode));
		}
		return cashList;
		
	}
	private boolean checkIfEnoughCurrencyExists(Map<CurrencyCode, Integer> cashRequestMap)
			throws NotEnoughMoneyLeftException {
		
		boolean enoughCurrencyExists = false;	
		for(CurrencyCode currencyCode : cashRequestMap.keySet())
		{
			checkIfEnoughCurrencyExists(currencyCode, cashRequestMap.get(currencyCode));
		}
		enoughCurrencyExists = true;
		return enoughCurrencyExists;
	}
	
	private boolean checkIfEnoughCurrencyExists(CurrencyCode currencyCode, Integer currencyCount) 
			throws NotEnoughMoneyLeftException
	{
		boolean enoughCurrencyExists = false;
		Integer numberOfEntities = getNumberOfCurrrencyEntities(currencyCode);
		if(numberOfEntities < currencyCount)
			throw new NotEnoughMoneyLeftException("Not Enough Money left");
		enoughCurrencyExists = true;
		return enoughCurrencyExists;
	}

}
