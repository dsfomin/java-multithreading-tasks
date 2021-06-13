package Module2.Part1;

import Module2.Letter.DAOGeneric;
import Module2.Letter.ExtendedUser;
import Module2.Letter.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class DAOTask4 extends UnicastRemoteObject implements DAOGeneric {

    private Connection con;
    private Statement stmt;

    public DAOTask4() throws RemoteException {
        String url = "jdbc:mysql://localhost:3306/letters";
        String userName = "root";
        String password = "asdfg567";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            con = DriverManager.getConnection(url, userName, password);
            stmt = con.createStatement();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws SQLException, RemoteException {
        DAOTask4 dao = new DAOTask4();
        //System.out.println(dao.findUserWSmallestLetterText());
        //dao.getUsersCountOfReceivedAndSentLetters().forEach(System.out::println);
        System.out.println(dao.sendLetterFromUserWLetterToAll(3L, 5L));
    }

    @Override
    public User findUserWSmallestLetterText() throws SQLException, RemoteException {
        User result = new User();
        long userId = 0;
        int minTextLength = Integer.MAX_VALUE;

        String sql1 = "SELECT * FROM letter";

        ResultSet rs = null;

        try {
            con.setAutoCommit(false);
            rs = stmt.executeQuery(sql1);

            while (rs.next()) {
                int currTextLength = rs.getString("text").length();

                if (currTextLength < minTextLength) {
                    minTextLength = currTextLength;
                    userId = rs.getLong("sender_id");
                }
            }

            String sql2 = "SELECT * FROM user WHERE id = '" + userId + "'";
            rs = stmt.executeQuery(sql2);

            if (rs.next()) {
                String surname = rs.getString("surname");
                String name = rs.getString("name");
                LocalDate birthday = rs.getObject("birthday", LocalDate.class);

                result = new User(userId, surname, name, birthday);
            }

            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            con.rollback();
        } finally {
            if (rs != null) {
                rs.close();
            }
            con.setAutoCommit(true);
        }

        return result;
    }

    @Override
    public List<ExtendedUser> getUsersCountOfReceivedAndSentLetters() throws SQLException, RemoteException {
        List<ExtendedUser> result = new ArrayList<>();
        String sql1 = "SELECT * FROM user";

        ResultSet rs = null;
        try {
            con.setAutoCommit(false);
            rs = stmt.executeQuery(sql1);

            while (rs.next()) {
                long id = rs.getLong("id");
                String username = rs.getString("surname");
                String name = rs.getString("name");
                LocalDate birthday = rs.getObject("birthday", LocalDate.class);
                User userFromDb = new User(id, username, name, birthday);
                result.add(new ExtendedUser(userFromDb, 0, 0));
            }

            String sql2 = "SELECT * FROM letter";
            rs = stmt.executeQuery(sql2);

            while (rs.next()) {
                long senderId = rs.getLong("sender_id");
                long recipientId = rs.getLong("recipient_id");

                for (ExtendedUser extendedUser : result) {
                    if (extendedUser.getUser().getId() == senderId) {
                        extendedUser.setCountSentLetters(extendedUser.getCountSentLetters() + 1);
                    }
                    if (extendedUser.getUser().getId() == recipientId) {
                        extendedUser.setCountReceivedLetters(extendedUser.getCountReceivedLetters() + 1);
                    }
                }
            }

            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            con.rollback();
        } finally {
            if (rs != null) {
                rs.close();
            }
            con.setAutoCommit(true);
        }
        return result;
    }

    @Override
    public List<User> getUsersThatReceivedLetter(String theme) throws SQLException, RemoteException {
        List<User> result = new ArrayList<>();

        String sql1 = "SELECT * FROM letter WHERE theme='" + theme + "'";
        Set<Long> ids = new TreeSet<>();

        ResultSet rs = null;
        try {
            con.setAutoCommit(false);

            rs = stmt.executeQuery(sql1);

            while (rs.next()) {
                long recipientId = rs.getLong("recipient_id");

                ids.add(recipientId);
            }

            String sql2 = "SELECT * FROM user";
            rs = stmt.executeQuery(sql2);

            while (rs.next()) {
                long id = rs.getLong("id");

                if (ids.contains(id)) {
                    String username = rs.getString("surname");
                    String name = rs.getString("name");
                    LocalDate birthday = rs.getObject("birthday", LocalDate.class);
                    result.add(new User(id, username, name, birthday));
                }
            }

            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            con.rollback();
        } finally {
            if (rs != null) {
                rs.close();
            }
            con.setAutoCommit(true);
        }
        return result;
    }

    @Override
    public List<User> getUsersThatNotReceivedLetter(String theme) throws RemoteException, SQLException {
        List<User> result = new ArrayList<>();

        String sql1 = "SELECT * FROM letter WHERE theme='" + theme + "'";
        Set<Long> ids = new TreeSet<>();

        ResultSet rs = null;
        try {
            con.setAutoCommit(false);

            rs = stmt.executeQuery(sql1);

            while (rs.next()) {
                long recipientId = rs.getLong("recipient_id");

                ids.add(recipientId);
            }

            String sql2 = "SELECT * FROM user";
            rs = stmt.executeQuery(sql2);

            while (rs.next()) {
                long id = rs.getLong("id");

                if (!ids.contains(id)) {
                    String username = rs.getString("surname");
                    String name = rs.getString("name");
                    LocalDate birthday = rs.getObject("birthday", LocalDate.class);
                    result.add(new User(id, username, name, birthday));
                }
            }

            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            con.rollback();
        } finally {
            if (rs != null) {
                rs.close();
            }
            con.setAutoCommit(true);
        }
        return result;
    }

    @Override
    public int sendLetterFromUserWLetterToAll(Long userId, Long letterId) throws RemoteException, SQLException {
        int response = 1;
        long maxLetterId = 0;
        String theme = null;
        String text = null;

        List<Long> users = new ArrayList<>();

        String sql1 = "SELECT * FROM user";
        ResultSet rs = null;
        try {
            con.setAutoCommit(false);

            rs = stmt.executeQuery(sql1);

            while (rs.next()) {
                long id = rs.getLong("id");
                users.add(id);
            }

            users.remove(userId);

            String sql2 = "SELECT * FROM letter";

            rs = stmt.executeQuery(sql2);

            while (rs.next()) {
                long id = rs.getLong("id");
                if (id > maxLetterId) maxLetterId = id;

                if (id == letterId) {
                    theme = rs.getString("theme");
                    text = rs.getString("text");
                }
            }

            String sql3 = "INSERT INTO letter VALUES(?,?,?,?,?,?)";
            PreparedStatement statement = con.prepareStatement(sql3);

            for (Long id : users) {
                statement.setString(1, String.valueOf(++maxLetterId));
                statement.setString(2, String.valueOf(userId));
                statement.setString(3, String.valueOf(id));
                statement.setString(4, theme);
                statement.setString(5, text);
                statement.setString(6, String.valueOf(LocalDateTime.now()));

                statement.addBatch();
            }

            statement.executeBatch();

            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            con.rollback();
            response = 0;
        } finally {
            if (rs != null) {
                rs.close();
            }
            con.setAutoCommit(true);
        }
        return response;
    }
}
