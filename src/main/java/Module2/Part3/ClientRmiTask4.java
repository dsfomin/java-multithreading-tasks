package Module2.Part3;

import Module2.Letter.DAOGeneric;
import Module2.Letter.ExtendedUser;
import Module2.Letter.User;
import Module2.Part1.DAOTask4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public class ClientRmiTask4 {
    private final static String URL = "//localhost:123/serverRMI";

    public ClientRmiTask4() throws RemoteException, NotBoundException, MalformedURLException {
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Client Menu: \n" +
                "1. findUserWSmallestLetterText \n" +
                "2. getUsersCountOfReceivedAndSentLetters \n" +
                "3. getUsersThatReceivedLetter 'letter_id' \n" +
                "4. getUsersThatNotReceivedLetter 'letter_id' \n" +
                "5. sendLetterFromUserWLetterToAll 'user_id' 'letter_id' \n");
        try {
            while (true) {
                System.out.println("> ");
                String operation = keyboard.readLine();
                String[] arg = operation.split(" ");
                handleOperation(arg);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void handleOperation(String[] arg) throws IOException, ClassNotFoundException, SQLException, NotBoundException {
        DAOGeneric serverRMI = (DAOGeneric) Naming.lookup(URL);
        switch (Integer.parseInt(arg[0])) {
            case 1: {
                User user = serverRMI.findUserWSmallestLetterText();
                System.out.println("User with smallest letter text is: " + user);
                break;
            }
            case 2: {
                List<ExtendedUser> users = serverRMI.getUsersCountOfReceivedAndSentLetters();
                users.forEach(extendedUser -> {
                    System.out.println("User: " + extendedUser.getUser());
                    System.out.println("Count of user sent letters: " + extendedUser.getCountSentLetters());
                    System.out.println("Count of user received letters: " + extendedUser.getCountReceivedLetters());
                });
                break;
            }
            case 3: {
                List<User> users = serverRMI.getUsersThatReceivedLetter(arg[1]);
                users.forEach(System.out::println);
                break;
            }
            case 4: {
                List<User> users = serverRMI.getUsersThatNotReceivedLetter(arg[1]);
                users.forEach(System.out::println);
                break;
            }
            case 5: {
                break;
            }
            default: {
                System.out.println("Unknown operation");
            }
        }
    }
}
