package fun.destywen.houry.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import fun.destywen.houry.MyApplication;
import fun.destywen.houry.R;
import fun.destywen.houry.activities.PostActivity;
import fun.destywen.houry.adapters.PostAdapter;
import fun.destywen.houry.database.dao.PostDao;
import fun.destywen.houry.database.entity.Post;

public class PostListFragment extends Fragment {

    private static final String TAG = "c_o_l_u_h";
    PostDao postDao;
    private PostAdapter adapter;
    private RecyclerView recyclerView;

    public PostListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postDao = MyApplication.getInstance().getDB().postDao();
        File directory = new File(requireContext().getFilesDir(), "images");
        if (!directory.exists()) {
            boolean success = directory.mkdirs();
            assert success;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.postList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PostAdapter(getPostData());
        adapter.setFilesDir(requireContext().getFilesDir());
        recyclerView.setAdapter(adapter);

        ActivityResultLauncher<Intent> register = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result != null) {
                Intent intent = result.getData();
                if (intent != null && result.getResultCode() == Activity.RESULT_OK) {
                    Bundle bundle = intent.getExtras();
                    assert bundle != null;
                    String tag = bundle.getString("tag");
                    String content = bundle.getString("content");
                    Uri imgUri = bundle.getParcelable("image");
                    insertPost(tag, content, imgUri);
                }
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.fab_new_post);
        fab.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), PostActivity.class);
            register.launch(intent);
        });
    }

    private List<Post> getPostData() {
        List<Post> posts = postDao.queryAll();
        for (Post post : posts) {
            Log.d(TAG, "getPostData: " + post);
        }
        return postDao.queryAll();
    }

    public void insertPost(String tag, String content, Uri imgUri) {
        Post post = new Post();
        post.setTag(tag);
        post.setContent(content);
        post.setTime(System.currentTimeMillis());
        post.setUuid(saveImage(imgUri));
        postDao.insert(post);

        adapter.notifyInsert(post);
        recyclerView.scrollToPosition(0);
    }

    // return the uuid assigned
    private String saveImage(Uri imgUri) {
        if (imgUri == null)
            return "";
        Bitmap image;
        try {
            image = BitmapFactory.decodeStream(requireContext().getContentResolver().openInputStream(imgUri));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (image != null) {
            String uuid = UUID.randomUUID().toString();
            Log.d(TAG, "insertPost: UUID: " + uuid);
            File imageFile = new File(requireContext().getFilesDir(), "images/" + uuid + ".jpg");
            try (FileOutputStream out = new FileOutputStream(imageFile)) {
                image.compress(Bitmap.CompressFormat.JPEG, 100, out);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return uuid;
        }
        return "";
    }
}