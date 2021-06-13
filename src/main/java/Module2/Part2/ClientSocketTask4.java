package Module2.Part2;

import Module2.Letter.ExtendedUser;
import Module2.Letter.User;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientSocketTask4 {
    private static ObjectOutputStream out = null;
    private static ObjectInputStream in = null;

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("127.0.0.1", 9090);
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());

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

    private static void handleOperation(String[] arg) throws IOException, ClassNotFoundException {
        switch (Integer.parseInt(arg[0])) {
            case 1: {
                out.writeInt(1);
                out.flush();
                User user = (User) in.readObject();
                System.out.println("User with smallest letter text is: " + user);
                break;
            }
            case 2: {
                out.writeInt(2);
                out.flush();
                List<ExtendedUser> users = (List<ExtendedUser>) in.readObject();
                users.forEach(extendedUser -> {
                    System.out.println("User: " + extendedUser.getUser());
                    System.out.println("Count of user sent letters: " + extendedUser.getCountSentLetters());
                    System.out.println("Count of user received letters: " + extendedUser.getCountReceivedLetters());
                });
                break;
            }
            case 3: {
                out.writeInt(3);
                out.writeObject(arg[1]);
                out.flush();
                List<User> users = (List<User>) in.readObject();
                users.forEach(System.out::println);
                break;
            }
            case 4: {
                out.writeInt(4);
                out.writeObject(arg[1]);
                out.flush();
                List<User> users = (List<User>) in.readObject();
                users.forEach(System.out::println);
                break;
            }
            case 5: {
                out.writeInt(4);
                out.writeObject(Integer.parseInt(arg[1]));
                out.writeObject(Integer.parseInt(arg[2]));
                out.flush();
                int response = in.readInt();
                if (response == 1) {
                    System.out.println("OK!");
                } else {
                    System.out.println("Failed!");
                }
                break;
            }
            default: {
                System.out.println("Unknown operation");
            }
        }
    }
}
