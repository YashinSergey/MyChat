package swing;

/**
 * Интерфейс для взаимодействия класса сети {@link Network}
 * с пользовательским интерфейсом на
 */
public interface MessageSender {

    /**
     * Метод вызывается классом сети при получении нового сообщения
     * @param msg новое сообщение
     */
    void submitMessage(Message msg);

    void addUser(String user);

    // TODO добавить метод для оповещения о новом пользователе
}
