package fun.destywen.houry.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.atomic.AtomicReference;

import fun.destywen.houry.R;

public class PostActivity extends AppCompatActivity {

    private static final String TAG = "c_o_l_u_h";

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
        ImageView imageView = findViewById(R.id.post_new_image);

        AtomicReference<Uri> imgUri = new AtomicReference<>();
        Log.d(TAG, "onCreate: imgUri initial: " + imgUri);
        ActivityResultLauncher<Intent> getImageRegister = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result != null) {
                Intent intent = result.getData();
                if (intent != null && result.getResultCode() == Activity.RESULT_OK) {
                    imgUri.set(intent.getData());
                    Log.d(TAG, "onCreate: New Image URI: " + imgUri);
                    imageView.setImageURI(imgUri.get());
                }
            }
        });
        imageView.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            getImageRegister.launch(intent);
        });

        Button commitButton = findViewById(R.id.btn_post_new);
        commitButton.setOnClickListener(view -> {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("content", contentText.getText().toString());
            bundle.putString("tag", tagText.getText().toString());
            bundle.putParcelable("image", imgUri.get());
            intent.putExtras(bundle);
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
    }
}