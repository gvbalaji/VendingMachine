package com.vmexample.vendingmachine.internal.cash.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.stereotype.Repository;

import com.vmexample.vendingmachine.client.Messages;
import com.vmexample.vendingmachine.internal.cash.CurrencyCode;
import com.vmexample.vendingmachine.internal.cash.MoneyEntity;
import com.vmexample.vendingmachine.internal.cash.NotEnoughMoneyLeftException;
import com.vmexample.vendingmachine.internal.cash.UnableToAddCashException;

@Repository
public class InMemoryCashRepository implements CashRepository {

	private final Map<CurrencyCode, List<MoneyEntity>> cashMap;
	private final ReadWriteLock  lock;
	private final Lock readLock, writeLock;

	
	public InMemoryCashRepository() {
		this.cashMap = new EnumMap<>(CurrencyCode.class);
		lock = new ReentrantReadWriteLock();
	    readLock = lock.readLock();
	    writeLock = lock.writeLock();
	}

	@Override
	public  void depositCash(MoneyEntity money) throws UnableToAddCashException {
		
		try {
			writeLock.lock();
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
				throw new UnableToAddCashException(Messages.getString(Messages.UNABLE_TO_DEPOSIT_CASH), e); //$NON-NLS-1$
		}
		finally {
			writeLock.unlock();
		}

	}

	@Override
	public  void depositCash(List<MoneyEntity> moneyList) throws UnableToAddCashException {
		
		List<MoneyEntity> addedMoneyList = new ArrayList<>();
		try {
			writeLock.lock();
			for(MoneyEntity money : moneyList)
			{
				depositCash(money);
				addedMoneyList.add(money);
			}
		} catch (Exception e) {
			throw new UnableToAddCashException(e);
		}
		finally {
			
			if(addedMoneyList.size() > 0)
				removeMoneyEntities(addedMoneyList);
			writeLock.unlock();
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
	public  Integer getNumberOfCurrrencyEntities(CurrencyCode currencyCode) {
		
		int numberOfCurrencyEntities = 0;
		try {
			readLock.lock();		
			List<MoneyEntity> moneyList = cashMap.get(currencyCode);
			if(moneyList != null)
				numberOfCurrencyEntities = moneyList.size();
		}
		finally {
			readLock.unlock();
		}
		return new Integer(numberOfCurrencyEntities);
	}

	@Override
	public BigDecimal getTotalCurrencyValue(CurrencyCode currencyCode) {
		
		Integer numberOfEntities = 0;
		try {
			readLock.lock();	
			numberOfEntities = getNumberOfCurrrencyEntities(currencyCode);
		}
		finally {
			readLock.unlock();
		}
		return new BigDecimal(currencyCode.value() * numberOfEntities);
	}

	@Override
	public  MoneyEntity debitCash(CurrencyCode currencyCode) throws NotEnoughMoneyLeftException {
		
		MoneyEntity money = null;
		try {
			writeLock.lock();
			if(checkIfEnoughCurrencyExists(currencyCode, 0))
				money = cashMap.get(currencyCode).remove(0);
		}
		finally {
			writeLock.unlock();
		}		
		return money;
	}

	@Override
	public  Map<CurrencyCode, List<MoneyEntity>> debitCash(
			Map<CurrencyCode, Integer> cashRequestMap)  throws NotEnoughMoneyLeftException {
		
		Map<CurrencyCode, List<MoneyEntity>> moneyMap= new EnumMap<>(CurrencyCode.class);
		try {
			writeLock.lock();
			/*
			 * make Sure there is enough money first before taking anything
			 */
			checkIfEnoughCurrencyExists(cashRequestMap);
			
			/*
			 * Now take all the money at once
			 */
			
			for(CurrencyCode currencyCode : cashRequestMap.keySet())
			{
				moneyMap.put(currencyCode, getCashList(currencyCode,cashRequestMap.get(currencyCode)));
			}
		}
		finally {
			writeLock.unlock();
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
			throw new NotEnoughMoneyLeftException(Messages.getString(Messages.NOT_ENOUGH_MONEY_LEFT)); //$NON-NLS-1$
		enoughCurrencyExists = true;
		return enoughCurrencyExists;
	}

}
