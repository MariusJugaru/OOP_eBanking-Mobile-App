package org.poo.cb.Commands;

import org.poo.cb.Accounts.Account;
import org.poo.cb.Database;
import org.poo.cb.User;

public class BuyPremiumCommand implements Command{
    private Database database;
    private String user;
    private CommandHistory history;

    public BuyPremiumCommand(Database database, String user, CommandHistory history) {
        this.database = database;
        this.user = user;
        this.history = history;
    }

    @Override
    public void execute() {
        User user = this.database.getUsers().get(this.user);
        if (user == null) {
            System.out.println("User with " + this.user + " doesn't exist");
            return;
        }

        Account account = (Account) user.searchPortolio("USD");
        if (account.getSum() < 100) {
            System.out.println("Insufficient amount in account for buying premium option");
            return;
        }

        if (user.isPremium()) {
            System.out.println("User is already a premium user");
            return;
        }

        account.setSum(account.getSum() - 100);
        user.setPremium(true);

        history.push(this);
    }

    @Override
    public void undo() {

    }
}
