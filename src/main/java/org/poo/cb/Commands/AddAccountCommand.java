package org.poo.cb.Commands;

import org.poo.cb.Accounts.Account;
import org.poo.cb.Accounts.AccountFactory;
import org.poo.cb.Database;
import org.poo.cb.User;

import java.util.ArrayList;

public class AddAccountCommand implements Command{
    private Database database;
    private String user;
    private String currencyType;
    AccountFactory accFactory;
    private CommandHistory history;

    public AddAccountCommand(Database database, String user, String currencyType, AccountFactory accFactory, CommandHistory history) {
        this.database = database;
        this.user = user;
        this.currencyType = currencyType;
        this.accFactory = accFactory;
        this.history = history;
    }

    @Override
    public void execute() {
        User user = this.database.getUsers().get(this.user);
        if (user == null) {
            System.out.println("User with " + this.user + " doesn't exist");
            return;
        }

        if (user.searchPortolio(this.currencyType) != null) {
            System.out.println("Account in currency " + this.currencyType + " already exists for user");
            return;
        }

        Account account = accFactory.createAccount(currencyType);
        ArrayList<Object> portfolio = user.getPortfolio();
        portfolio.add(account);

        history.push(this);
    }

    @Override
    public void undo() {
        User user = this.database.getUsers().get(this.user);
        Account account = (Account) user.searchPortolio(this.currencyType);
        ArrayList<Object> portfolio = user.getPortfolio();
        portfolio.remove(account);
    }
}
