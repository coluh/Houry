package fun.destywen.houry.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import fun.destywen.houry.R;
import fun.destywen.houry.fragments.HomeFragment;
import fun.destywen.houry.fragments.PostListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.fg, new HomeFragment()).commit();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add a toggle to make it more convenient
        DrawerLayout drawerLayout = findViewById(R.id.main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        // set navigation listener
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Fragment fragment = null;
            if (id == R.id.nav_home) {
                fragment = new HomeFragment();
            } else if (id == R.id.nav_post) {
                fragment = new PostListFragment();
            } else {
                Toast.makeText(this, "Not Implement", Toast.LENGTH_SHORT).show();
            }
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fg, fragment).commit();
                drawerLayout.close();
            }
            return false; // show item chosen state in navigation view
        });
    }
}