package Library;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {

    private User user;
    private BookDAO bookDAO = new BookDAO();
    private JTable table;
    private DefaultTableModel model;

    public MainFrame(User user) {
        this.user = user;

        setTitle("Library System - Welcome " + user.getFullname());
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Top panel
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField search = new JTextField(20);
        JButton btnSearch = new JButton("Search");
        JButton btnProfile = new JButton("Profile");
        JButton btnLogout = new JButton("Log-out");

        top.add(new JLabel("Search book:"));
        top.add(search);
        top.add(btnSearch);
        top.add(btnProfile);
        top.add(btnLogout);
        add(top, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel(new Object[]{"Book ID", "Title", "Author", "Available"}, 0);
        table = new JTable(model);
        loadBooks();
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottom panel
        JPanel bottom = new JPanel();
        JButton btnBorrow = new JButton("Borrow Selected Book");
        bottom.add(btnBorrow);
        add(bottom, BorderLayout.SOUTH);

        // Actions
        btnSearch.addActionListener(e -> {
            List<Book> result = bookDAO.searchTitle(search.getText());
            refreshTable(result);
        });

        btnBorrow.addActionListener(e -> borrowBook());

        btnProfile.addActionListener(e -> {
            this.dispose();
            new ProfileFrame(user).setVisible(true);
        });

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Log-out", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                new LoginFrame().setVisible(true);
            }
        });
    }

    private void loadBooks() {
        List<Book> list = bookDAO.listAll();
        refreshTable(list);
    }

    private void refreshTable(List<Book> list) {
        model.setRowCount(0);
        for (Book b : list) {
            model.addRow(new Object[]{b.getId(), b.getTitle(), b.getAuthor(), b.getAvailable()});
        }
    }

    private void borrowBook() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a book to borrow!");
            return;
        }

        int bookId = (int) model.getValueAt(row, 0);
        boolean ok = bookDAO.borrow(user.getId(), bookId);

        JOptionPane.showMessageDialog(this, ok ? "Borrowed successfully!" : "Book unavailable!");
        loadBooks();
    }
}
