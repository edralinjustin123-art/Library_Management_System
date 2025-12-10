package Library;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ProfileFrame extends JFrame {

    private User user;
    private JTable table;
    private DefaultTableModel model;

    public ProfileFrame(User user) {
        this.user = user;

        setTitle("Profile - " + user.getUsername());
        setSize(800, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top panel: user info
        JPanel topPanel = new JPanel(new GridLayout(3, 1));
        topPanel.add(new JLabel("Username: " + user.getUsername()));
        topPanel.add(new JLabel("Fullname: " + user.getFullname()));
        add(topPanel, BorderLayout.NORTH);

        // Table for borrowed books
        model = new DefaultTableModel(new Object[]{"Transaction ID", "Book Title", "Borrow Date", "Due Date", "Returned"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottom panel
        JPanel bottom = new JPanel();
        JButton btnReturn = new JButton("Return Selected Book");
        JButton btnReturnMain = new JButton("Return to Main Menu");
        JButton btnLogout = new JButton("Log-out");

        bottom.add(btnReturn);
        bottom.add(btnReturnMain);
        bottom.add(btnLogout);
        add(bottom, BorderLayout.SOUTH);

        // Load borrowed books
        loadBorrowedBooks();

        // Actions
        btnReturn.addActionListener(e -> returnSelectedBook());

        btnReturnMain.addActionListener(e -> {
            this.dispose();
            new MainFrame(user).setVisible(true);
        });

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Log-out", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                new LoginFrame().setVisible(true);
            }
        });
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
