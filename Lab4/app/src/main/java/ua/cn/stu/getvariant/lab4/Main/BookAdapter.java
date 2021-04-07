package ua.cn.stu.getvariant.lab4.Main;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.Collections;
import java.util.List;

import ua.cn.stu.getvariant.lab4.R;
import ua.cn.stu.getvariant.lab4.model.Book;

public class BookAdapter
        extends Adapter
        implements View.OnClickListener{

    private List<Book> books = Collections.emptyList();
    private BookListener listener;

    public BookAdapter(BookListener bookListener){
        listener = bookListener;
    }

    public void setBooks(List<Book> books){
        this.books = books;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(R.layout.item_books,parent,false);
        return new BookViewHolder(root,this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder1, int position) {
        BookViewHolder holder = (BookViewHolder) holder1;
        Book book = books.get(position);
        holder.nameTextView.setText(book.getName());
        holder.startTextView.setText( new Integer(position+1).toString());
        holder.itemView.setTag(book);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    @Override
    public void onClick(View v) {
        Book book = (Book) v.getTag();
        listener.onBookchoose(book);
    }

    static class BookViewHolder extends ViewHolder{
        private TextView startTextView;
        private TextView nameTextView;
        public BookViewHolder(@NonNull View itemView, View.OnClickListener listener) {
            super(itemView);
             startTextView = itemView.findViewById(R.id.starsTextViews);
             nameTextView = itemView.findViewById(R.id.nameTextView);
            itemView.setOnClickListener(listener);
        }
    }

    public interface BookListener{
        public void onBookchoose(Book book);
    }
}
