import domain.CurrencyType;
import domain.MoneyModel;
import domain.TransactionModel;
import seed.SeedInitializer;
import services.SavingsManagerService;
import services.TransactionManagerService;

import static seed.AccountsSeedData.*;

public class BankingApplication {

    public static void main(String[] args) {
        System.out.println("[SYSTEM] Initialize Application \n");
        SeedInitializer.seedData();
        System.out.println("[SYSTEM] Running Application \n\n");

        // TRANSACTION MANAGER FUNCTIONALITY

        TransactionManagerService transactionManagerServiceInstance = new TransactionManagerService();
        SavingsManagerService savingsManagerServiceInstance = new SavingsManagerService();

        //Transfer funds from checkingAccountA to checkingAccountB
        System.out.println("[Transaction Manager] 1. " + transactionManagerServiceInstance.checkFunds(checkingAccountA.getId()));
        System.out.println("[Transaction Manager] 2. " + transactionManagerServiceInstance.checkFunds(checkingAccountB.getId()));

        TransactionModel transaction1 = transactionManagerServiceInstance.transfer(
                checkingAccountA.getId(),
                checkingAccountB.getId(),
                new MoneyModel(20, CurrencyType.RON)
        );

        System.out.println("[Transaction Manager] 3. " + transaction1);
        System.out.println("[Transaction Manager] 4. " + transactionManagerServiceInstance.checkFunds(checkingAccountA.getId()));
        System.out.println("[Transaction Manager] 5. " + transactionManagerServiceInstance.checkFunds(checkingAccountB.getId()));


        System.out.println("\n------------------------------------\n");

        //Withdraw funds from an Account
        System.out.println("\nWithdraw funds from an Account");
        MoneyModel withdrawalAmount = new MoneyModel(40, CurrencyType.RON);

        System.out.println("[Transaction Manager] 1. Current balance: " + transactionManagerServiceInstance.checkFunds(checkingAccountA.getId()));
        System.out.println("[Transaction Manager] 2. Withdrawal requested: " + withdrawalAmount.getAmount() + " " + withdrawalAmount.getCurrency());
        TransactionModel transaction2 = transactionManagerServiceInstance.withdraw(
                checkingAccountA.getId(),
                withdrawalAmount
        );
        if (transaction2 != null) {


            System.out.println("[Transaction Manager] 3. Withdrawal successful: " + transaction2.getAmount().getAmount() + " " + transaction2.getAmount().getCurrency());
        }
        System.out.println("[Transaction Manager] 3. " + transaction2);
        System.out.println("[Transaction Manager] 4. Updated balance: " + transactionManagerServiceInstance.checkFunds(checkingAccountA.getId()));

        System.out.println("\nDifferent currencies scenario");

        System.out.println("[Transaction Manager] 1. " + transactionManagerServiceInstance.checkFunds(checkingAccountC.getId()));
        System.out.println("[Transaction Manager] 2. " + transactionManagerServiceInstance.checkFunds(checkingAccountB.getId()));

        TransactionModel transaction4 = transactionManagerServiceInstance.transfer(
                checkingAccountC.getId(),
                checkingAccountB.getId(),
                new MoneyModel(10, CurrencyType.EUR)
        );

        System.out.println("[Transaction Manager] 3. " + transaction4);
        System.out.println("[Transaction Manager] 4. " + transactionManagerServiceInstance.checkFunds(checkingAccountC.getId()));
        System.out.println("[Transaction Manager] 5. " + transactionManagerServiceInstance.checkFunds(checkingAccountB.getId()));

        System.out.println("\n------------------------------------\n");

        // SAVINGS MANAGER FUNCTIONALITY
        System.out.println("\nQuarterly interest");

        System.out.println("[Saving Manager] 1. " + transactionManagerServiceInstance.checkFunds(savingsAccountA.getId()));
        System.out.println("[Saving Manager] 4. " + transactionManagerServiceInstance.checkFunds(savingsAccountB.getId()));
        System.out.println("[Saving Manager] 5. " + transactionManagerServiceInstance.checkFunds(checkingAccountA.getId()));

        savingsManagerServiceInstance.passTime();
        System.out.println("[Saving Manager] 2. " + transactionManagerServiceInstance.checkFunds(savingsAccountA.getId()));
        System.out.println("[Saving Manager] 4. " + transactionManagerServiceInstance.checkFunds(savingsAccountB.getId()));

        savingsManagerServiceInstance.passTime();
        System.out.println("[Saving Manager] 3. " + transactionManagerServiceInstance.checkFunds(savingsAccountA.getId()));
        System.out.println("[Saving Manager] 4. " + transactionManagerServiceInstance.checkFunds(savingsAccountB.getId()));

        savingsManagerServiceInstance.passTime();
        System.out.println("[Saving Manager] 3. " + transactionManagerServiceInstance.checkFunds(savingsAccountA.getId()));
        System.out.println("[Saving Manager] 4. " + transactionManagerServiceInstance.checkFunds(savingsAccountB.getId()));

        savingsManagerServiceInstance.passTime();
        System.out.println("[Saving Manager] 3. " + transactionManagerServiceInstance.checkFunds(savingsAccountA.getId()));
        System.out.println("[Saving Manager] 4. " + transactionManagerServiceInstance.checkFunds(savingsAccountB.getId()));
        System.out.println("[Saving Manager] 5. " + transactionManagerServiceInstance.checkFunds(checkingAccountA.getId()));

        System.out.println("\n[SYSTEM] Application closed\n");
    }
}