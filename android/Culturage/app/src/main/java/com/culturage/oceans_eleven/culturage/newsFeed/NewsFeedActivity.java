package com.culturage.oceans_eleven.culturage.newsFeed;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.culturage.oceans_eleven.culturage.R;

import java.util.ArrayList;

public class NewsFeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewsFeedActivity.this, UploadActivity.class));
            }
        });

        ImageView profileIcon = (ImageView) findViewById(R.id.profileIcon);
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewsFeedActivity.this, ProfilePageActivity.class));
            }
        });


        ArrayList<HeritageItem> items = new ArrayList<HeritageItem>();

        String description = "\"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\"";
        String title = "Lorem Ipsum";
        items.add(new HeritageItem(1, title, description, R.drawable.sample_0, 1, 3));
        items.add(new HeritageItem(2, title, description, R.drawable.sample_1, 2, 6));
        items.add(new HeritageItem(3, title, description, R.drawable.sample_2, 1, 7));
        items.add(new HeritageItem(4, title, description, R.drawable.sample_3, 3, 3));
        items.add(new HeritageItem(5, title, description, R.drawable.sample_4, 1, 89));
        items.add(new HeritageItem(6, title, description, R.drawable.sample_5, 5, 5));
        items.add(new HeritageItem(7, title, description, R.drawable.sample_6, 3, 7));
        items.add(new HeritageItem(8, title, description, R.drawable.sample_7, 1, 4));

        HeritageItemAdapter adapter = new HeritageItemAdapter(NewsFeedActivity.this, items);
        final ListView listView = (ListView) findViewById(R.id.news_feed_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HeritageItem item = ((HeritageItem) listView.getItemAtPosition(position));
                Intent intent = new Intent(NewsFeedActivity.this, HeritageItemViewActivity.class);
                intent.putExtra("postId", item.getmPostId());
                intent.putExtra("title", item.getmTitle());
                intent.putExtra("description", item.getmDescription());
                intent.putExtra("resourceID", item.getmResourceId());
                startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
//        Intent intent = new Intent(mContext, LoginActivity.class);
//        intent.putExtra("FLAG", 0);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        startActivity(intent);
    }

}
