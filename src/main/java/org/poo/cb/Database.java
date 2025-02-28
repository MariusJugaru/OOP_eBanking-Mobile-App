package org.poo.cb;

import org.poo.cb.Accounts.Account;
import org.poo.cb.Accounts.AccountFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Singleton design pattern. We only need one database
public class Database {
    private static Database databaseInstance;
    private final HashMap<String, User> users = new HashMap<>();
    private final AccountFactory accFactory = AccountFactory.Instance();
    private List<String[]> exchangeRates;
    private List<String[]> stockValues;

    public List<String[]> getExchangeRates() {
        return exchangeRates;
    }

    public List<String[]> getStockValues() {
        return stockValues;
    }

    public HashMap<String, User> getUsers() {
        return users;
    }

    public void setExchangeRates(List<String[]> exchangeRates) {
        this.exchangeRates = exchangeRates;
    }

    public void setStockValues(List<String[]> stockValues) {
        this.stockValues = stockValues;
    }

    private Database() { }

    public static Database Instance() {
        if (databaseInstance == null)
            databaseInstance = new Database();
        return databaseInstance;
    }

    public static void clearDatabase() {
        databaseInstance = null;
    }

    public void addUser(String email, String firstname, String lastname, String address) {
        if (this.users.get(email) != null) {
            System.out.println("User with " + email + " already exists");
            return;
        }

        User user = new UserBuilder()
                .withEmail(email)
                .withFirstname(firstname)
                .withLastname(lastname)
                .withAddress(address)
                .build();

        this.users.put(email, user);
    }

    public void addMoney(String email, String currencyType, float amount) {
        User user = this.users.get(email);
        Account account = (Account) user.searchPortolio(currencyType);

        account.setSum(account.getSum() + amount);
    }

    public void listUser(String email) {
        if (this.users.get(email) == null) {
            System.out.println("User with " + email + " doesn't exist");
            return;
        }

        String friends = "";
        User thisUser = this.users.get(email);
        ArrayList<User> friendList = thisUser.getFriends();

        if (friendList.size() != 0) {
            for (int i = 0; i < friendList.size() - 1; i++)
                friends += "\"" + friendList.get(i).getEmail() + "\",";
            friends += "\"" + friendList.get(friendList.size() - 1).getEmail() + "\"";
        }

        System.out.println("{\"email\":\"" + thisUser.getEmail() + "\"," +
                "\"firstname\":\"" + thisUser.getFirstname() + "\"," +
                "\"lastname\":\"" + thisUser.getLastname() + "\"," +
                "\"address\":\"" + thisUser.getAddress() + "\"," +
                "\"friends\":[" + friends + "]}");
    }

    public void listPortfolio(String email) {
        User user = this.users.get(email);
        ArrayList<Object> portfolio = user.getPortfolio();

        String stocks = null;
        String accounts = null;

        // iterate through the portfolio and add accounts and stock to their own "list" (string)
        for (Object obj:portfolio) {
            if (obj instanceof Account) {
                Account account = (Account) obj;

                if (accounts == null)
                    accounts = "{" + account.toString() + "}";
                else
                    accounts += ",{" + account.toString() + "}";
            }
            if (obj instanceof Stocks) {
                Stocks stock = (Stocks) obj;

                if (stocks == null)
                    stocks = "{" + stock.toString() + "}";
                else
                    stocks += ",{" + stock.toString() + "}";
            }
        }

        if (stocks == null)
            stocks = "";
        if (accounts == null)
            accounts = "";

        System.out.println("{\"stocks\":[" + stocks + "],\"accounts\":[" + accounts + "]}");
    }

    public String recommendStocks() {
        float SMA_five, SMA_ten;
        String recommendations = null;
        int c;

        for (int i = 1; i < this.stockValues.size(); i++) {
            SMA_five = 0f;
            SMA_ten = 0f;
            c = 0;
            String[] strings = this.stockValues.get(i);

            // sum of last 5 and 10 days
            for (int j = strings.length - 1; j > 0; j--) {
                if (c < 5)
                    SMA_five += Float.parseFloat(strings[j]);
                if (c < 10)
                    SMA_ten += Float.parseFloat(strings[j]);
                c++;
            }
            SMA_five /= 5;
            SMA_ten /= 10;

            // add a stock to recommendations
            if (SMA_five > SMA_ten) {
                if (recommendations == null)
                    recommendations = "\"stocksToBuy\":[\"" + strings[0] + "\"";
                else
                    recommendations += ",\"" + strings[0] + "\"";
            }
        }

        if (recommendations == null)
            recommendations = "\"stocksToBuy\":[]";
        else
            recommendations += "]";

        return "{" + recommendations + "}";
    }
}
