package fun.destywen.houry.fragments;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import fun.destywen.houry.MyApplication;
import fun.destywen.houry.R;
import fun.destywen.houry.adapters.PostAdapter;
import fun.destywen.houry.database.dao.PostDao;
import fun.destywen.houry.database.entity.Post;

public class PostListFragment extends Fragment {

    private static final String TAG = "coluh";
    PostDao postDao;

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

        RecyclerView recyclerView = view.findViewById(R.id.postList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        PostAdapter adapter = new PostAdapter(getPostData());
        recyclerView.setAdapter(adapter);

        Button button = view.findViewById(R.id.test_insert);
        button.setOnClickListener(view1 -> {
            Post post = new Post();
            post.setTime(System.currentTimeMillis());
            post.setTag("Test");
            post.setContent("This is a test message.");
            postDao.insert(post);
            adapter.notifyInsert(post);
            recyclerView.scrollToPosition(0);
        });
    }

    private List<Post> getPostData() {
        List<Post> posts = postDao.queryAll();
        for (Post post : posts) {
            Log.d(TAG, "getPostData: " + post);
        }
        return postDao.queryAll();
    }
}