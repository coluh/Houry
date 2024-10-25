package fun.destywen.houry.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fun.destywen.houry.R;
import fun.destywen.houry.database.entity.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private List<Post> mData;

    public PostAdapter(List<Post> mData) {
        this.mData = mData;
    }

    public void notifyInsert(Post post) {
        mData.add(0, post);
        notifyItemInserted(0);
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        Post item = mData.get(position);
        holder.tag.setText(item.getTag());
        holder.content.setText(item.getContent());
        holder.time.setText(item.getTimeFormated());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tag;
        public TextView content;
        public TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tag = itemView.findViewById(R.id.post_tag);
            content = itemView.findViewById(R.id.post_content);
            time = itemView.findViewById(R.id.post_time);
        }
    }
}
