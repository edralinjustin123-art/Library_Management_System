-- Create the database
CREATE DATABASE IF NOT EXISTS library_db;
USE library_db;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role ENUM('admin', 'student') DEFAULT 'student'
);

-- Books table
CREATE TABLE IF NOT EXISTS books (
    book_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255),
    genre VARCHAR(100),
    quantity INT DEFAULT 1
);

-- Borrowing / Transactions table
CREATE TABLE IF NOT EXISTS transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    book_id INT,
    borrow_date DATE,
    due_date DATE,
    return_date DATE,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (book_id) REFERENCES books(book_id)
);

-- Borrowing table
CREATE TABLE IF NOT EXISTS borrowed_books (
    borrow_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    book_id INT,
    borrow_date DATE,
    return_date DATE,
    returned BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (book_id) REFERENCES books(book_id)
);  

-- Insert sample users
INSERT INTO users (username, password, role) VALUES
('admin', 'admin123', 'admin'),
('student1', 'password1', 'student'),
('student2', 'password2', 'student');

-- Insert sample books
INSERT INTO books (title, author, genre, quantity) VALUES
('Harry Potter', 'J.K. Rowling', 'Fantasy', 5),
('The Hobbit', 'J.R.R. Tolkien', 'Adventure', 3),
('Introduction to Java', 'James Gosling', 'Programming', 10),
('Database Systems', 'R. Elmasri', 'Education', 2);
