package fun.destywen.houry.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;
import java.util.Objects;

import fun.destywen.houry.R;
import fun.destywen.houry.database.entity.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post item = mData.get(position);
        holder.tag.setText(item.getTag());
        holder.content.setText(item.getContent());
        if (Objects.equals(item.getUuid(), "")) {
            holder.image.setVisibility(View.GONE);
        } else {
            holder.image.setImageBitmap(loadImage(item.getUuid()));
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
