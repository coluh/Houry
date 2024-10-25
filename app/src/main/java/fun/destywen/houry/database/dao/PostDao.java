package fun.destywen.houry.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import fun.destywen.houry.database.entity.Post;

@Dao
public interface PostDao {

    @Insert
    void insert(Post post);

    @Delete
    void delete(Post post);

    // You better not call this
    @Query("DELETE FROM Post")
    void deleteAll();

    @Update
    int update(Post post);

    @Query("SELECT * FROM Post WHERE time = :time limit 1")
    Post queryByTime(long time);

    @Query("SELECT * FROM Post ORDER BY time DESC")
    List<Post> queryAll();
}
