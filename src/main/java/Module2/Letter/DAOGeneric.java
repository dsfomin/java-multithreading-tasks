package Module2.Letter;

import Module2.Letter.ExtendedUser;
import Module2.Letter.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface DAOGeneric extends Remote {
    /**
     * Find the user whose letter text length is the smallest.
     *
     * @return user
     */
    User findUserWSmallestLetterText() throws RemoteException, SQLException;

    /**
     * Find users, as well as the number of received
     * and sent the letters by them.
     *
     * @return extended user
     */
    List<ExtendedUser> getUsersCountOfReceivedAndSentLetters() throws RemoteException, SQLException;

    /**
     * Find users who have received at least one
     * letter with a given letter theme.
     *
     * @return user
     */
    List<User> getUsersThatReceivedLetter(String theme) throws RemoteException, SQLException;

    /**
     * Find users who didn't received
     * letter with a given letter theme.
     *
     * @return user
     */
    List<User> getUsersThatNotReceivedLetter(String theme) throws RemoteException, SQLException;

    /**
     * Send a letter from a given user with a given letter to all users.
     *
     */
    int sendLetterFromUserWLetterToAll(Long userId, Long letterId) throws RemoteException, SQLException;
}
