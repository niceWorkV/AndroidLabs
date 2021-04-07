package ua.cn.stu.getvariant.lab4.Main.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface BookDAO {
    @Query("SELECT * FROM books WHERE author = :author")
    List<BookDbEntity> getBooks(String author);

    @Query("SELECT * FROM books WHERE id = :id")
    BookDbEntity getById(int id);

    @Insert
    void insertBooks(List<BookDbEntity> books);

    @Query("DELETE FROM books WHERE author = :author")
    void deleteBook(String author);

    @Transaction
    default void updateBooksForAuthor(String author, List<BookDbEntity> entities){
        deleteBook(author);
        insertBooks(entities);
    }

}
