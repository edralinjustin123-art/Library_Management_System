package Library;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class ProfileFrame extends JFrame {

    private User user;
    private JTable table;
    private DefaultTableModel model;
    private Font garamondFont = new Font("Garamond", Font.PLAIN, 14);

    public ProfileFrame(User user) {
        this.user = user;

        setTitle("Profile - " + user.getUsername());
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(null); // Flexible positioning

        // ===== TOP PANEL =====
        JPanel topPanel = new JPanel();
        topPanel.setLayout(null);
        topPanel.setBounds(0, 0, 900, 60);
        topPanel.setBackground(new Color(0xd9a7c7d));

        // User info - top left
        JLabel lblUsername = new JLabel("USERNAME: " + user.getUsername());
        lblUsername.setBounds(10, 5, 280, 25);
        lblUsername.setFont(new Font("Garamond", Font.BOLD, 20));
        topPanel.add(lblUsername);

        JLabel lblFullname = new JLabel("FULLNAME: " + user.getFullname());
        lblFullname.setBounds(10, 30, 280, 25);
        lblFullname.setFont(new Font("Garamond", Font.BOLD, 20));
        topPanel.add(lblFullname);

        // Buttons - top right
        JButton btnReturn = new JButton("Return Book");
        btnReturn.setBounds(415, 15, 140, 30);
        btnReturn.setBackground(new Color(0xC3B091));
        btnReturn.setFont(garamondFont);
        topPanel.add(btnReturn);

        JButton btnReturnMain = new JButton("Main Menu");
        btnReturnMain.setBounds(565, 15, 140, 30);
        btnReturnMain.setBackground(new Color(0xC3B091));
        btnReturnMain.setFont(garamondFont);
        topPanel.add(btnReturnMain);

        JButton btnLogout = new JButton("Log-out");
        btnLogout.setBounds(715, 15, 140, 30); // optional: adjust to fit spacing
        btnLogout.setBackground(new Color(0xC3B091));
        btnLogout.setFont(garamondFont);
        topPanel.add(btnLogout);

        add(topPanel);

        // ===== TABLE =====
        model = new DefaultTableModel(
                new Object[]{"Transaction ID", "Book Title", "Borrow Date", "Due Date", "Returned"}, 0
        );
        table = new JTable(model);
        table.setFont(garamondFont);
        table.setRowHeight(25);
        table.setBackground(new Color(0xC3B091));
        table.setForeground(Color.BLACK);

        JTableHeader header = table.getTableHeader();
        header.setFont(garamondFont);
        header.setBackground(new Color(0xC3B091));
        header.setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(0, 60, 900, 500);
        scrollPane.getViewport().setBackground(new Color(0xd9a7c7d));
        scrollPane.setBackground(new Color(0xd9a7c7d));
        add(scrollPane);

        getContentPane().setBackground(new Color(0xd9a7c7d));

        // ===== ACTIONS =====
        btnReturn.addActionListener(e -> returnSelectedBook());

        btnReturnMain.addActionListener(e -> {
            this.dispose();
            new MainFrame(user).setVisible(true);
        });

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to log out?",
                    "Log-out",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                new LoginFrame().setVisible(true);
            }
        });

        // Load borrowed books
        loadBorrowedBooks();
    }

    private void loadBorrowedBooks() {
        model.setRowCount(0);
        String sql = "SELECT t.id, b.title, t.borrow_date, t.due_date, t.return_date " +
                "FROM transactions t " +
                "JOIN books b ON t.book_id = b.book_id " +
                "WHERE t.user_id = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setInt(1, user.getId());
            ResultSet rs = p.executeQuery();

            while (rs.next()) {
                int transId = rs.getInt("id");
                String title = rs.getString("title");
                Date borrowDate = rs.getDate("borrow_date");
                Date dueDate = rs.getDate("due_date");
                boolean returned = rs.getDate("return_date") != null;

                model.addRow(new Object[]{transId, title, borrowDate, dueDate, returned ? "Yes" : "No"});
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void returnSelectedBook() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a book to return!");
            return;
        }

        int transId = (int) model.getValueAt(row, 0);
        BookDAO bookDAO = new BookDAO();
        boolean ok = bookDAO.returnBook(transId);

        JOptionPane.showMessageDialog(this, ok ? "Book returned successfully!" : "Failed to return book!");
        loadBorrowedBooks();
    }
}
