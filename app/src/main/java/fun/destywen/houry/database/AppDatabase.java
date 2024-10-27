package fun.destywen.houry.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import fun.destywen.houry.database.dao.PostDao;
import fun.destywen.houry.database.entity.Post;

@Database(entities = {Post.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PostDao postDao();

}
