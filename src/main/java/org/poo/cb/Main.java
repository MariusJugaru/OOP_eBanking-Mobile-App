package org.poo.cb;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.poo.cb.Accounts.AccountFactory;
import org.poo.cb.Commands.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    static String antetResources = "src/main/resources/";

    public static List<String[]> readData(String fileName) {
        List<String[]> allData;
        try {
            FileReader filereader = new FileReader(antetResources + fileName);

            CSVReader csvReader = new CSVReaderBuilder(filereader).build();
            allData = csvReader.readAll();

            filereader.close();
            csvReader.close();
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }

        return allData;
    }
    public static void main(String[] args) {
        if(args == null) {
            System.out.println("Running Main");
            return;
        }

        // clear the database
        Database.clearDatabase();

        // create the database
        Database database = Database.Instance();
        database.setExchangeRates(readData(args[0]));
        database.setStockValues(readData(args[1]));

        // create command history
        CommandHistory history = new CommandHistory();

        try {
            File commands = new File(antetResources + args[2]);
            Scanner scanner = new Scanner(commands);

            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();

                // get command
                String[] splitString = data.split(" ");
                String command = splitString[0] + " " + splitString[1];

                String email, firstname, lastname, address, emailFriend, currency, company;
                String src, dst;
                Float amount;

                switch (command) {
                    case "CREATE USER":
                        email = splitString[2];
                        firstname = splitString[3];
                        lastname = splitString[4];
                        address = "";
                        for (int i = 5; i < splitString.length - 1; i++)
                            address = address + splitString[i] + " ";
                        address = address + splitString[splitString.length - 1];

                        database.addUser(email, firstname, lastname, address);
                        break;
                    case "ADD FRIEND":
                        email = splitString[2];
                        emailFriend = splitString[3];

                        AddFriendCommand addFriend = new AddFriendCommand(Database.Instance(), email, emailFriend, history);
                        addFriend.execute();
                        break;
                    case "ADD ACCOUNT":
                        email = splitString[2];
                        currency = splitString[3];

                        AddAccountCommand addAccount = new AddAccountCommand(Database.Instance(), email, currency, AccountFactory.Instance(), history);
                        addAccount.execute();
                        break;
                    case "ADD MONEY":
                        email = splitString[2];
                        currency = splitString[3];
                        amount = Float.valueOf(splitString[4]);

                        database.addMoney(email, currency, amount);
                        break;
                    case "EXCHANGE MONEY":
                        email = splitString[2];
                        src = splitString[3];
                        dst = splitString[4];
                        amount = Float.valueOf(splitString[5]);

                        ExchangeMoneyCommand exchangeMoney = new ExchangeMoneyCommand(Database.Instance(), email, src,
                                dst, amount, Database.Instance().getExchangeRates(), history);
                        exchangeMoney.execute();
                        break;
                    case "TRANSFER MONEY":
                        email = splitString[2];
                        emailFriend = splitString[3];
                        currency = splitString[4];
                        amount = Float.valueOf(splitString[5]);

                        TransferMoneyCommand transferMoney = new TransferMoneyCommand(Database.Instance(),
                                email, emailFriend, currency, amount, history);
                        transferMoney.execute();
                        break;
                    case "BUY STOCKS":
                        email = splitString[2];
                        company = splitString[3];
                        amount = Float.parseFloat(splitString[4]);

                        BuyStocksCommand buyStocks = new BuyStocksCommand(Database.Instance(), email, company, amount, history);
                        buyStocks.execute();

                        break;
                    case "RECOMMEND STOCKS":
                        System.out.println(database.recommendStocks());
                        break;
                    case "LIST USER":
                        email = splitString[2];

                        database.listUser(email);
                        break;
                    case "LIST PORTFOLIO":
                        email = splitString[2];

                        database.listPortfolio(email);
                        break;
                    case "BUY PREMIUM":
                        email = splitString[2];

                        BuyPremiumCommand buyPremium = new BuyPremiumCommand(Database.Instance(), email, history);
                        buyPremium.execute();
                        break;
                    case "UNDO COMMAND":
                        if (!history.isEmpty()) {
                            Command lastCommand = history.pop();
                            lastCommand.undo();
                        }
                        break;
                    default:
                        break;
                }
            }
            System.out.println();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }



    }
}