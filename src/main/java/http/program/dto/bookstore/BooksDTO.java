package http.program.dto.bookstore;

import java.util.ArrayList;
import java.util.List;

public class BooksDTO {
    private List<Book> books = new ArrayList<>();

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
