package ua.cn.stu.getvariant.lab4.Details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import ua.cn.stu.getvariant.lab4.App;
import ua.cn.stu.getvariant.lab4.R;

public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_BOOK_ID = "BOOK_ID";
    public static final String EXTRA_AUTHOR = "AUTHOR";

    private ProgressBar progressBar;
    private TextView nameTextView;
    private TextView desctiptionTextView;

    private DetailsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        nameTextView = findViewById(R.id.nameTextView);
        desctiptionTextView = findViewById(R.id.descriptionTextView);
        progressBar = findViewById(R.id.progress);

        int bookID = getIntent().getIntExtra(EXTRA_BOOK_ID,-1);
        if(bookID == -1){
            throw new RuntimeException("No book with such id");
        }
        String author = getIntent().getStringExtra(EXTRA_AUTHOR);

        App app = (App) getApplication();
        ViewModelProvider viewModelProvider = new ViewModelProvider(this,app.getViewModelFactory());
        viewModel = viewModelProvider.get(DetailsViewModel.class);

        viewModel.loadDetails(bookID);

        viewModel.getResult().observe(this, result ->{
            switch (result.getStatus()){
                case SUCCESS:
                    nameTextView.setText(result.getData().getName());
                    String[] subjects = result.getData().getSubjects();
                    String text = "Subjects:\n";
                    for(String x: subjects){
                        text = text+x+"\n";
                    }
                    desctiptionTextView.setText(text);
                    progressBar.setVisibility(View.GONE);
                    break;
                case EMPTY:
                    break;
                case LOADING:
                    nameTextView.setText("");
                    desctiptionTextView.setText("");
                    progressBar.setVisibility(View.GONE);
                    break;
            }
        });
    }
}