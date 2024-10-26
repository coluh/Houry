package fun.destywen.houry.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import fun.destywen.houry.R;

public class PostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button cancelButton = findViewById(R.id.btn_post_cancel);
        cancelButton.setOnClickListener(view -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });

        EditText contentText = findViewById(R.id.post_new_content);
        EditText tagText = findViewById(R.id.post_new_tag);

        Button commitButton = findViewById(R.id.btn_post_new);
        commitButton.setOnClickListener(view -> {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("content", contentText.getText().toString());
            bundle.putString("tag", tagText.getText().toString());
            intent.putExtras(bundle);
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
    }
}