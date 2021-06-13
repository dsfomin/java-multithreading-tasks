package Module2.Letter;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Letter implements Serializable {
    private Long id;
    private User sender;
    private User recipient;
    private String theme;
    private String text;
    private LocalDateTime sendingTime;

    public Letter(User sender, User recipient, String theme, String text, LocalDateTime sendingTime) {
        this.sender = sender;
        this.recipient = recipient;
        this.theme = theme;
        this.text = text;
        this.sendingTime = sendingTime;
    }


    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getSendingTime() {
        return sendingTime;
    }

    public void setSendingTime(LocalDateTime sendingTime) {
        this.sendingTime = sendingTime;
    }
}
