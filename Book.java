package Library;

public class Book {

    private int id;
    private String isbn;
    private String title;
    private String author;
    private String category;
    private int total;
    private int available;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }

    public int getAvailable() { return available; }
    public void setAvailable(int available) { this.available = available; }
}
