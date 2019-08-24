package swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow extends JFrame implements MessageSender {

    private JTextField textField;
    private JList<Message> messageList;
    private DefaultListModel<Message> messageListModel;
    private DefaultListModel<String> userListModel;
    private JList<String> userList;

    private Network network;

    MainWindow() {
        JFrame frame = new JFrame();
        frame.setTitle("Сетевой чат");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setBounds(200, 200, 500, 500);

        frame.setLayout(new BorderLayout());   // выбор компоновщика элементов

        messageListModel = new DefaultListModel<>();
        messageList = new JList<>(messageListModel);
        messageList.setCellRenderer(new MessageCellRenderer());

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(messageList, BorderLayout.SOUTH);
        panel.setBackground(messageList.getBackground());
        JScrollPane scrollPane = new JScrollPane(panel);
        frame.add(scrollPane, BorderLayout.CENTER);

        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        addUser("ivan");
        addUser("petr");
        addUser("julia");
        addUser("john");
        userList.setPreferredSize(new Dimension(100, 0));
        frame.add(userList, BorderLayout.WEST);

        textField = new JTextField();
        JButton button = new JButton("Отправить");
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userTo = userList.getSelectedValue();
                if (userTo == null) {
                    JOptionPane.showMessageDialog(MainWindow.this,
                            "Не указан получатель",
                            "Отправка сообщения",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String text = textField.getText();
                if (text == null || text.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(MainWindow.this,
                            "Нельзя отправить пустое сообщение",
                            "Отправка сообщения",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Message msg = new Message(network.getUsername(), userTo, text.trim());
                submitMessage(msg);
                textField.setText(null);
                textField.requestFocus();

                network.sendMessageToUser(msg);
            }
        });

        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()== KeyEvent.VK_ENTER) {
                    String userTo = userList.getSelectedValue();
                    if (userTo == null) {
                        JOptionPane.showMessageDialog(MainWindow.this,
                                "Не указан получатель",
                                "Отправка сообщения",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String text = textField.getText();
                    if (text == null || text.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(MainWindow.this,
                                "Нельзя отправить пустое сообщение",
                                "Отправка сообщения",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Message msg = new Message(network.getUsername(), userTo, text.trim());
                    submitMessage(msg);
                    textField.setText(null);
                    textField.requestFocus();

                    network.sendMessageToUser(msg);
                }
            }
        });

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent evt) {
                messageList.ensureIndexIsVisible(messageListModel.size() - 1);
            }
        });

        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(button, BorderLayout.EAST);
        panel.add(textField, BorderLayout.CENTER);

        frame.add(panel, BorderLayout.SOUTH);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (network != null) {
                    network.close();
                }
                super.windowClosing(e);
            }
        });

        frame.setVisible(true);

        network = new Network("localhost", 7777, this);

        LoginDialog loginDialog = new LoginDialog(frame, network);
        loginDialog.setVisible(true);

        if (!loginDialog.isConnected()) {
            System.exit(0);
        }
        
        frame.setTitle("Сетевой чат. Пользователь " + network.getUsername());
    }

    @Override
    public void submitMessage(Message msg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                messageListModel.add(messageListModel.size(), msg);
                messageList.ensureIndexIsVisible(messageListModel.size() - 1);
            }
        });
    }

    @Override
    public void addUser(String user) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                userListModel.add(userListModel.size(), user);
                userList.ensureIndexIsVisible(userListModel.size() - 1);
            }
        });
    }


}