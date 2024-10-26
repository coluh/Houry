package fun.destywen.houry.fragments;

import android.app.Activity;
import android.content.Intent;
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

import java.util.List;

import fun.destywen.houry.MyApplication;
import fun.destywen.houry.R;
import fun.destywen.houry.activities.PostActivity;
import fun.destywen.houry.adapters.PostAdapter;
import fun.destywen.houry.database.dao.PostDao;
import fun.destywen.houry.database.entity.Post;

public class PostListFragment extends Fragment {

    private static final String TAG = "coluh";
    PostDao postDao;
    private PostAdapter adapter;
    private RecyclerView recyclerView;

    public PostListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postDao = MyApplication.getInstance().getDB().postDao();
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
        recyclerView.setAdapter(adapter);

        ActivityResultLauncher<Intent> register = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result != null) {
                Intent intent = result.getData();
                if (intent != null && result.getResultCode() == Activity.RESULT_OK) {
                    Bundle bundle = intent.getExtras();
                    assert bundle != null;
                    String tag = bundle.getString("tag");
                    String content = bundle.getString("content");
                    insertPost(tag, content);
                }
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.fab_new_post);
        fab.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), PostActivity.class);
            register.launch(intent);
        });

//        Button button = view.findViewById(R.id.test_insert);
//        button.setOnClickListener(view1 -> {
//            Post post = new Post();
//            post.setTime(System.currentTimeMillis());
//            post.setTag("Test");
//            post.setContent("This is a test message.");
//            postDao.insert(post);
//            adapter.notifyInsert(post);
//            recyclerView.scrollToPosition(0);
//        });
    }

    private List<Post> getPostData() {
        List<Post> posts = postDao.queryAll();
        for (Post post : posts) {
            Log.d(TAG, "getPostData: " + post);
        }
        return postDao.queryAll();
    }

    public void insertPost(String tag, String content) {
        Post post = new Post();
        post.setTag(tag);
        post.setContent(content);
        post.setTime(System.currentTimeMillis());
        postDao.insert(post);

        adapter.notifyInsert(post);
        recyclerView.scrollToPosition(0);
    }
}