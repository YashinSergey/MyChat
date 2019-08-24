package swing;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;


class LoginDialog extends JDialog {

    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private final Network network;
    private Timer timer;

    private boolean connected;

    LoginDialog(Frame parent, Network network) {
        super(parent, "Login", true);

        this.network = network;
        this.connected = false;

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();

        cs.fill = GridBagConstraints.HORIZONTAL;

        JLabel lbUsername = new JLabel("Username: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(lbUsername, cs);

        tfUsername = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(tfUsername, cs);

        JLabel lbPassword = new JLabel("Password: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(lbPassword, cs);

        pfPassword = new JPasswordField(20);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(pfPassword, cs);
        panel.setBorder(new LineBorder(Color.GRAY));

        JButton btnLogin = new JButton("Login");
        JButton btnCancel = new JButton("Cancel");

        timer = new Timer(30000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.dispose();
            }
        });

        JPanel bp = new JPanel();
        bp.add(btnLogin);
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addListener();
            }
        });

        KeyListener keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()== KeyEvent.VK_ENTER) {
                    addListener();
                }
            }
        };
        pfPassword.addKeyListener(keyListener);
        tfUsername.addKeyListener(keyListener);

        bp.add(btnCancel);
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    boolean isConnected() {
        return connected;
    }

    private void addListener(){
        try {
            network.authorize(tfUsername.getText(), String.valueOf(pfPassword.getPassword()));
            connected = true;
        } catch (AuthException ex) {
            JOptionPane.showMessageDialog(LoginDialog.this,
                    "Ошибка авторизации",
                    "Авторизация",
                    JOptionPane.ERROR_MESSAGE);
            timer.start();
            return;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(LoginDialog.this,
                    "Ошибка сети",
                    "Авторизация",
                    JOptionPane.ERROR_MESSAGE);
            timer.start();
            return;
        }
        dispose();
    }
}
