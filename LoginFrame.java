package Library;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    public static void main(String[] args) {
        new LoginFrame().setVisible(true);
    }

    JTextField txtUser;
    JPasswordField txtPass;

    UserDAO userDAO = new UserDAO();

    public LoginFrame() {

        setTitle("LOG IN");
        setSize(420, 420);
        getContentPane().setBackground(new Color(0xd9a7c7d));
        setLocationRelativeTo(null);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        txtUser = new JTextField();
        txtUser.setBounds(45, 130, 310, 30);
        txtUser.setFont(new Font("Garamond", Font.BOLD, 17));
        add(txtUser);

        txtPass = new JPasswordField();
        txtPass.setBounds(45, 190, 310, 30);
        txtPass.setFont(new Font("Garamond", Font.BOLD, 17));
        add(txtPass);

        JLabel LIBRARY = new JLabel();
        LIBRARY.setText("LIBRARY");
        LIBRARY.setBounds(90, 10, 300, 60);
        LIBRARY.setFont(new Font("Garamond", Font.BOLD, 50));
        add(LIBRARY);

        JLabel LOGIN = new JLabel();
        LOGIN.setText("LOG IN");
        LOGIN.setBounds(160, 40, 200, 60);
        LOGIN.setFont(new Font("Garamond", Font.BOLD, 17));
        add(LOGIN);

        JLabel usrnm = new JLabel();
        usrnm.setText("USERNAME:");
        usrnm.setBounds(45, 90, 200, 60);
        usrnm.setForeground(Color.BLACK);
        usrnm.setFont(new Font("Garamond", Font.BOLD, 15));
        add(usrnm);

        JLabel pswrd = new JLabel();
        pswrd.setText("PASSWORD:");
        pswrd.setBounds(45, 150, 150, 60);
        pswrd.setForeground(Color.BLACK);
        pswrd.setFont(new Font("Garamond", Font.BOLD, 15));
        add(pswrd);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(80, 250, 90, 50);
        btnLogin.setFont(new Font("Garamond", Font.BOLD, 15));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(new Color(0xC3B091));
        add(btnLogin);

        JButton btnReg = new JButton("Register");
        btnReg.setBounds(220, 250, 90, 50);
        btnReg.setFont(new Font("Garamond", Font.BOLD, 15));
        btnReg.setForeground(Color.WHITE);
        btnReg.setBackground(new Color(0xC3B091));
        add(btnReg);

        btnLogin.addActionListener(e -> login());
        btnReg.addActionListener(e -> register());
    }

    private void login() {
        String u = txtUser.getText();
        String p = new String(txtPass.getPassword());

        User user = userDAO.login(u, p);

        if (user != null) {
            JOptionPane.showMessageDialog(this, "Welcome " + user.getFullname());
            new MainFrame(user).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password");
        }
    }

    private void register() {
        String u = txtUser.getText();
        String p = new String(txtPass.getPassword());

        if (u.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill all fields");
            return;
        }

        User newUser = new User(u, p);

        if (userDAO.register(newUser)) {
            JOptionPane.showMessageDialog(this, "Registration successful!");
        } else {
            JOptionPane.showMessageDialog(this, "Username already exists");
        }
    }


}
