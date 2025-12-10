package Library;

import java.sql.*;
import java.util.*;

public class BookDAO {

    // List all books
    public List<Book> listAll() {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM books";

        try (Connection c = DBConnection.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {

            while (rs.next()) {
                Book b = new Book();
                b.setId(rs.getInt("book_id"));
                b.setTitle(rs.getString("title"));
                b.setAuthor(rs.getString("author"));
                b.setCategory(rs.getString("genre"));   // fixed column
                b.setAvailable(rs.getInt("quantity"));  // fixed column
                list.add(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Search books by title
    public List<Book> searchTitle(String q) {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title LIKE ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setString(1, "%" + q + "%");
            ResultSet rs = p.executeQuery();

            while (rs.next()) {
                Book b = new Book();
                b.setId(rs.getInt("book_id"));
                b.setTitle(rs.getString("title"));
                b.setAuthor(rs.getString("author"));
                b.setCategory(rs.getString("genre"));
                b.setAvailable(rs.getInt("quantity"));
                list.add(b);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Borrow a book
    public boolean borrow(int userId, int bookId) {

        String sqlCheck = "SELECT quantity FROM books WHERE book_id = ?";
        String sqlBorrow = "UPDATE books SET quantity = quantity - 1 WHERE book_id = ?";
        String sqlLog = "INSERT INTO transactions (user_id, book_id, borrow_date, due_date) VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 14 DAY))";

        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);

            // Check availability
            PreparedStatement p1 = c.prepareStatement(sqlCheck);
            p1.setInt(1, bookId);
            ResultSet rs = p1.executeQuery();

            if (!rs.next() || rs.getInt("quantity") <= 0) {
                c.rollback();
                return false;
            }

            // Update quantity
            PreparedStatement p2 = c.prepareStatement(sqlBorrow);
            p2.setInt(1, bookId);
            p2.executeUpdate();

            // Log transaction
            PreparedStatement p3 = c.prepareStatement(sqlLog);
            p3.setInt(1, userId);
            p3.setInt(2, bookId);
            p3.executeUpdate();

            c.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Return a book
    public boolean returnBook(int transId) {

        String sqlGet = "SELECT book_id FROM transactions WHERE id = ? AND return_date IS NULL";
        String sqlReturnLog = "UPDATE transactions SET return_date = CURDATE() WHERE id = ?";
        String sqlReturnBook = "UPDATE books SET quantity = quantity + 1 WHERE book_id = ?";

        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);

            // Get book id
            PreparedStatement p1 = c.prepareStatement(sqlGet);
            p1.setInt(1, transId);
            ResultSet rs = p1.executeQuery();

            if (!rs.next()) {
                c.rollback();
                return false;
            }

            int bookId = rs.getInt("book_id");

            // Update transaction
            PreparedStatement p2 = c.prepareStatement(sqlReturnLog);
            p2.setInt(1, transId);
            p2.executeUpdate();

            // Increase book quantity
            PreparedStatement p3 = c.prepareStatement(sqlReturnBook);
            p3.setInt(1, bookId);
            p3.executeUpdate();

            c.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
