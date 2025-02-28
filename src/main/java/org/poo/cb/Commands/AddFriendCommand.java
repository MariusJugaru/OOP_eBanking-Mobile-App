package org.poo.cb.Commands;

import org.poo.cb.Database;
import org.poo.cb.User;

import java.util.ArrayList;

public class AddFriendCommand implements Command{
    private Database database;
    private String user;
    private String friend;
    private CommandHistory history;


    public AddFriendCommand(Database database, String user, String friend, CommandHistory history) {
        this.database = database;
        this.user = user;
        this.history = history;
        this.friend = friend;
    }

    @Override
    public void execute() {
        User thisUser = this.database.getUsers().get(this.user);
        if (thisUser == null) {
            System.out.println("User with " + this.user + " doesn't exist");
            return;
        }

        User thatUser = this.database.getUsers().get(this.friend);
        if (thatUser == null) {
            System.out.println("User with " + this.friend + " doesn't exist");
            return;
        }

        ArrayList<User> thisFriendList = thisUser.getFriends();
        ArrayList<User> thatFriendList = thatUser.getFriends();
        if (thisFriendList.contains(thatUser)) {
            System.out.println("User with " + this.friend + " is already a friend");
            return;
        }

        thisFriendList.add(thatUser);
        thatFriendList.add(thisUser);

        history.push(this);
    }

    @Override
    public void undo() {
        User thisUser = this.database.getUsers().get(this.user);
        User thatUser = this.database.getUsers().get(this.friend);
        ArrayList<User> thisFriendList = thisUser.getFriends();
        ArrayList<User> thatFriendList = thatUser.getFriends();

        thisFriendList.remove(thatUser);
        thatFriendList.remove(thisUser);
    }
}
