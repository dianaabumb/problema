package services;

import domain.*;
import repository.AccountsRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionManagerService {

    //Convert currency based on exchange rates
    public static double convertCurrency(double amount, CurrencyType fromCurrency, CurrencyType toCurrency) {
        double exchangeRONtoEUR = 0.20;
        double exchangeEURtoRON = 4.97;
        if (fromCurrency == CurrencyType.RON && toCurrency == CurrencyType.EUR) {
            return amount * exchangeRONtoEUR;
        } else if (fromCurrency == CurrencyType.EUR && toCurrency == CurrencyType.RON) {
            return amount * exchangeEURtoRON;
        } else if (fromCurrency == toCurrency) {
            return amount;
        }
        return amount;
    }

    //Transfer funds between accounts
    public TransactionModel transfer(String fromAccountId, String toAccountId, MoneyModel value) {
        AccountModel fromAccount = AccountsRepository.INSTANCE.get(fromAccountId);
        AccountModel toAccount = AccountsRepository.INSTANCE.get(toAccountId);

        double convertedValue = 0;

        if (fromAccount == null || toAccount == null) {
            throw new RuntimeException("Specified account does not exist");
        }
        if ((fromAccount.getAccountType() == AccountType.SAVINGS && toAccount.getAccountType() == AccountType.CHECKING) || (fromAccount.getAccountType() == AccountType.SAVINGS && toAccount.getAccountType() == AccountType.SAVINGS)) {
            throw new RuntimeException("You cannot perform the transfer between these types of accounts");
        }

        //Convert amount if currencies are different
        if (fromAccount.getBalance().getCurrency() != toAccount.getBalance().getCurrency()) {
            convertedValue = convertCurrency(value.getAmount(), fromAccount.getBalance().getCurrency(), toAccount.getBalance().getCurrency());
        }

        //Maximum amount limit for transfer 5000
        if (value.getAmount() > 5000) {
            throw new RuntimeException("You reached the maximum transfer amount");
        }

        //Minimum amount limit for transfer 10
        if (value.getAmount() < 2) {
            throw new RuntimeException("The minimum transfer requirement is not met");
        }

        //Check if sufficient funds for transfer
        if (fromAccount.getBalance().getAmount() < value.getAmount()) {
            throw new RuntimeException("Insufficient funds for withdrawal");
        }

        TransactionModel transaction = new TransactionModel(
                UUID.randomUUID(),
                fromAccountId,
                toAccountId,
                value,
                LocalDate.now()
        );

        fromAccount.getBalance().setAmount(fromAccount.getBalance().getAmount() - value.getAmount());
        fromAccount.getTransactions().add(transaction);

        toAccount.getBalance().setAmount(toAccount.getBalance().getAmount() + convertedValue);
        toAccount.getTransactions().add(transaction);

        return transaction;
    }

    //Withdraw funds from an account
    public TransactionModel withdraw(String accountId, MoneyModel amount) {
        AccountModel account = AccountsRepository.INSTANCE.get(accountId);

        if (account == null) {
            throw new RuntimeException("Specified account does not exist");
        }
        if (account.getBalance().getAmount() < amount.getAmount()) {
            throw new RuntimeException("Insufficient funds for withdrawal");
        }

        TransactionModel transaction = new TransactionModel(
                UUID.randomUUID(),
                accountId,
                null,
                amount,
                LocalDate.now()
        );

        account.getBalance().setAmount(account.getBalance().getAmount() - amount.getAmount());
        account.getTransactions().add(transaction);

        return transaction;
    }


    public MoneyModel checkFunds(String accountId) {
        if (!AccountsRepository.INSTANCE.exist(accountId)) {
            throw new RuntimeException("Specified account does not exist");
        }
        return AccountsRepository.INSTANCE.get(accountId).getBalance();
    }


    public List<TransactionModel> retrieveTransactions(String accountId) {
        if (!AccountsRepository.INSTANCE.exist(accountId)) {
            throw new RuntimeException("Specified account does not exist");
        }
        return new ArrayList<>(AccountsRepository.INSTANCE.get(accountId).getTransactions());
    }
}

