package Module2.Letter;

import java.io.Serializable;

public class ExtendedUser implements Serializable {
    private User user;
    private Integer countReceivedLetters;
    private Integer countSentLetters;

    public ExtendedUser(User user, Integer countReceivedLetters, Integer countSentLetters) {
        this.user = user;
        this.countReceivedLetters = countReceivedLetters;
        this.countSentLetters = countSentLetters;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getCountReceivedLetters() {
        return countReceivedLetters;
    }

    public void setCountReceivedLetters(Integer countReceivedLetters) {
        this.countReceivedLetters = countReceivedLetters;
    }

    public Integer getCountSentLetters() {
        return countSentLetters;
    }

    public void setCountSentLetters(Integer countSentLetters) {
        this.countSentLetters = countSentLetters;
    }

    @Override
    public String toString() {
        return "ExtendedUser{" +
                "user=" + user +
                ", countReceivedLetters=" + countReceivedLetters +
                ", countSentLetters=" + countSentLetters +
                '}';
    }
}
