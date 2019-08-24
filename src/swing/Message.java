package swing;

import java.time.LocalDate;

public class Message {

    private String userFrom;

    private String userTo;

    private String text;

    private LocalDate date;

    Message(String userFrom, String userTo, String text) {
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.text = text;
        this.date = LocalDate.now();
    }

    String getUserFrom() {
        return userFrom;
    }

    String getUserTo() {
        return userTo;
    }

    String getText() {
        return text;
    }

    public LocalDate getDate() {
        return date;
    }
}