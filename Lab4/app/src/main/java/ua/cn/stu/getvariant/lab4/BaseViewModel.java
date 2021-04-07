package ua.cn.stu.getvariant.lab4;

import androidx.lifecycle.ViewModel;

import ua.cn.stu.getvariant.lab4.model.BookService;

public class BaseViewModel extends ViewModel {
    private BookService bookService;

    public BaseViewModel(BookService bookService) {
        this.bookService = bookService;
    }

    protected final BookService getBookService(){
        return bookService;
    }
}
