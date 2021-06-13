package Module2.Part2;

import Module2.Letter.ExtendedUser;
import Module2.Letter.User;
import Module2.Part1.DAOTask4;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public class ClientHandlerTask4 implements Runnable {
    private Socket client = null;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private DAOTask4 dao = new DAOTask4();

    public ClientHandlerTask4(Socket clientSocket) throws RemoteException {
        try {
            client = clientSocket;
            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                int request = in.readInt();
                handleRequest(request);
            }
        } catch (IOException | SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleRequest(int request) throws SQLException, IOException, ClassNotFoundException {
        switch (request) {
            case 1: {
                User user = dao.findUserWSmallestLetterText();
                out.writeObject(user);
                out.flush();
                break;
            }
            case 2: {
                List<ExtendedUser> users = dao.getUsersCountOfReceivedAndSentLetters();
                out.writeObject(users);
                out.flush();
                break;
            }
            case 3: {
                String theme = (String) in.readObject();
                List<User> users = dao.getUsersThatReceivedLetter(theme);
                out.writeObject(users);
                out.flush();
                break;
            }
            case 4: {
                String theme = (String) in.readObject();
                List<User> users = dao.getUsersThatNotReceivedLetter(theme);
                out.writeObject(users);
                out.flush();
                break;
            }
            case 5: {
                Long userId = (Long) in.readObject();
                Long letterId = (Long) in.readObject();
                int response = dao.sendLetterFromUserWLetterToAll(userId, letterId);
                out.writeInt(response);
                out.flush();
                break;
            }
            default: {
                System.out.println("Unknown request");
            }
        }
    }
}
