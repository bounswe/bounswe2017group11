package com.culturage.oceans_eleven.culturage.newsFeed;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.culturage.oceans_eleven.culturage.R;
import com.culturage.oceans_eleven.culturage.adapters.HeritageItemAdapter;
import com.culturage.oceans_eleven.culturage.baseClasses.HeritageItem;
import com.culturage.oceans_eleven.culturage.network.SearchLoader;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by me on 17.11.2017.
 * The search activity
 */

public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<HeritageItem>> {


    private HeritageItemAdapter itemAdapter;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);


        /*don't want this to appear*/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        ArrayList<HeritageItem> items = new ArrayList<HeritageItem>();


        itemAdapter = new HeritageItemAdapter(this, items);
        final ListView listView = (ListView) findViewById(R.id.news_feed_list);
        listView.setAdapter(itemAdapter);
        updateUi(items);
        getLoaderManager().initLoader(1, null, this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HeritageItem item = ((HeritageItem) listView.getItemAtPosition(position));
                Intent intent = new Intent(SearchActivity.this, HeritageItemViewActivity.class);
                intent.putExtra("postId", item.getmPostId());
                intent.putExtra("title", item.getmTitle());
                intent.putExtra("description", item.getmDescription());
                intent.putExtra("imageUrl", item.getmImageUrl());
                intent.putExtra("israted", item.isRated());
                Log.v("search", "postid " + item.getmPostId());

                startActivity(intent);
            }
        });
    }

    /**
     * Updates UI (the list)
     * clears old one and adds current items
     *
     * @param heritageItems up-to-date list of heritage items came from search result
     */
    private void updateUi(ArrayList<HeritageItem> heritageItems) {
        itemAdapter.clear();
        if (heritageItems != null || heritageItems.size() > 0) {
            itemAdapter.addAll(heritageItems);
        }
    }


    @Override
    public Loader<ArrayList<HeritageItem>> onCreateLoader(int id, Bundle args) {
        String stringToSearch = getIntent().getStringExtra("stringToSearch");

        try {
            stringToSearch = URLEncoder.encode(stringToSearch, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String endPoint = getIntent().getStringExtra("endPoint");
        return new SearchLoader(SearchActivity.this, endPoint + stringToSearch);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<HeritageItem>> loader, ArrayList<HeritageItem> heritageItems) {
        if (heritageItems == null || heritageItems.isEmpty()) {
            String stringToSearch = getIntent().getStringExtra("stringToSearch");
            Toast.makeText(SearchActivity.this, "Sorry we have got nothing to show about " + stringToSearch, Toast.LENGTH_LONG).show();

            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3600); // As I am using LENGTH_LONG in Toast + 500ms
                        SearchActivity.this.finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            thread.start();
            return;
        }
        updateUi(heritageItems);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<HeritageItem>> loader) {

        itemAdapter.clear();
    }
}
