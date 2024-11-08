package fun.destywen.houry.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import fun.destywen.houry.R;
import fun.destywen.houry.database.entity.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private static final String TAG = "c_o_l_u_h";

    private final List<Post> mData;
    private File filesDir;

    public PostAdapter(List<Post> mData) {
        this.mData = mData;
    }

    public void setFilesDir(File filesDir) {
        this.filesDir = filesDir;
    }

    public void notifyInsert(Post post) {
        mData.add(0, post);
        notifyItemInserted(0);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new ViewHolder(view);
    }

    private final Map<String, Bitmap> images = new HashMap<>();

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post item = mData.get(position);
        holder.tag.setText(item.getTag());
        holder.content.setText(item.getContent());
        holder.image.setVisibility(View.VISIBLE);
        holder.image.setImageBitmap(null);
//        Log.d(TAG, "onBindViewHolder: set " + item.getContent() + " image: " + item.getUuid());
        if (Objects.equals(item.getUuid(), "")) {
            holder.image.setVisibility(View.GONE);
        } else {
            if (images.get(item.getUuid()) == null) {
                images.put(item.getUuid(), loadImage(item.getUuid()));
            }
            holder.image.setImageBitmap(images.get(item.getUuid()));
        }
        holder.time.setText(item.getTimeFormated());
    }

    private Bitmap loadImage(String uuid) {
        File imageFile = new File(filesDir, "images/" + uuid + ".jpg");
        if (imageFile.exists()) {
            return BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        } else {
            throw new RuntimeException("Image Gone!!");
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tag;
        public TextView content;
        public ImageView image;
        public TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tag = itemView.findViewById(R.id.post_tag);
            content = itemView.findViewById(R.id.post_content);
            image = itemView.findViewById(R.id.post_image);
            time = itemView.findViewById(R.id.post_time);
        }
    }
}
