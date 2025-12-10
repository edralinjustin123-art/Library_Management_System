package Library;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {

    private User user;
    private BookDAO bookDAO = new BookDAO();
    private JTable table;
    private DefaultTableModel model;
    private Font garamondFont = new Font("Garamond", Font.PLAIN, 14);

    public MainFrame(User user) {
        this.user = user;

        setTitle("Library System");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // ===== TOP PANEL =====
        JPanel top = new JPanel();
        top.setLayout(null);
        top.setBounds(0, 0, 900, 50);
        top.setBackground(new Color(0xd9a7c7d));

        JTextField search = new JTextField(20);
        search.setBounds(10, 10, 180, 30);
        search.setFont(garamondFont);
        top.add(search);

        JButton btnSearch = new JButton("Search");
        btnSearch.setBackground(new Color(0xC3B091));
        btnSearch.setBounds(200, 10, 80, 30);
        btnSearch.setFont(garamondFont);
        top.add(btnSearch);

        JButton btnBorrow = new JButton("Borrow");
        btnBorrow.setBackground(new Color(0xC3B091));
        btnBorrow.setBounds(590, 10, 80, 30);
        btnBorrow.setFont(garamondFont);
        top.add(btnBorrow);

        JButton btnProfile = new JButton("Profile");
        btnProfile.setBackground(new Color(0xC3B091));
        btnProfile.setBounds(690, 10, 80, 30);
        btnProfile.setFont(garamondFont);
        top.add(btnProfile);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBackground(new Color(0xC3B091));
        btnLogout.setBounds(790, 10, 80, 30);
        btnLogout.setFont(garamondFont);
        top.add(btnLogout);

        add(top);

        // ===== TABLE =====
        model = new DefaultTableModel(
                new Object[]{"Book ID", "Title", "Author", "Available"},
                0
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
        scrollPane.setBounds(0, 50, 900, 500);
        scrollPane.getViewport().setBackground(new Color(0xd9a7c7d));
        scrollPane.setBackground(new Color(0xd9a7c7d));

        add(scrollPane);

        getContentPane().setBackground(new Color(0xd9a7c7d));

        loadBooks();

        // ===== BUTTON ACTIONS =====

        // 🔍 FIXED SEARCH FEATURE
        btnSearch.addActionListener(e -> {
            List<Book> result = bookDAO.searchBooks(search.getText());
            refreshTable(result);
        });

        btnBorrow.addActionListener(e -> borrowBook());

        btnProfile.addActionListener(e -> {
            this.dispose();
            new ProfileFrame(user).setVisible(true);
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
    }

    private void loadBooks() {
        List<Book> list = bookDAO.listAll();
        refreshTable(list);
    }

    private void refreshTable(List<Book> list) {
        model.setRowCount(0);
        for (Book b : list) {
            model.addRow(new Object[]{
                    b.getId(),
                    b.getTitle(),
                    b.getAuthor(),
                    b.getAvailable()
            });
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

        JOptionPane.showMessageDialog(
                this,
                ok ? "Borrowed successfully!" : "Book unavailable!"
        );

        loadBooks();
    }
}
