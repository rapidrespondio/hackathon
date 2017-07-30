package com.example.winningteam.rapidrespose;

import android.os.Bundle;
import android.view.View;

import android.databinding.ObservableArrayList;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.alfonz.adapter.SimpleDataBoundRecyclerAdapter;

import java.util.List;

import io.rapid.ListUpdate;
import io.rapid.Rapid;
import io.rapid.RapidCallback;
import io.rapid.RapidCollectionSubscription;
import io.rapid.RapidDocument;
import io.rapid.RapidDocumentReference;
import io.rapid.Sorting;
import com.example.winningteam.rapidrespose.basses.base;



public class stream extends base {

    private RecyclerView.Adapter mAdapter;
    @Nullable private RapidCollectionSubscription mSubscription;
    private ObservableArrayList<test> mItems = new ObservableArrayList<>();

    private Sorting mClickedOrdering = Sorting.DESC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


            setupInput();
            setupOrdering();
            setupAdapter();
        }


        @Override
        protected void onStart () {
            super.onStart();
            subscribeDataFromRapid();
        }


        @Override
        protected void onStop () {
            // TODO don't leak and unsubscribe
            unsubscribeFromRapid();
            super.onStop();
        }


    public void onItemClick(test item) {
        updateItemInRapid(item);
    }


    public boolean onItemLongClick(test item) {
        deleteItemFromRapid(item);
        return true;
    }


    public void addItemToRapid(final String name) {
        // TODO add item
        RapidDocumentReference<test> newDocument = Rapid.getInstance().collection("tests", test.class).newDocument();
        newDocument.mutate(new test(newDocument.getId(), name, 1));
    }


    public void subscribeDataFromRapid() {
        // TODO observing data
        // 1) subscribe to collection
        // 2) filter the collection
        // 3) order it

        mSubscription = Rapid.getInstance().collection("tests", test.class)
                .lessThan("clicked", 10)
                .orderBy("clicked", mClickedOrdering)
                .subscribeWithListUpdates(new RapidCallback.CollectionUpdates<test>() {
                    @Override
                    public void onValueChanged(List<RapidDocument<test>> rapidDocuments, ListUpdate listUpdate) {
                        mItems.clear();
                        for (RapidDocument<test> rapidDocument : rapidDocuments) {
                            mItems.add(rapidDocument.getBody());
                        }

                        listUpdate.dispatchUpdateTo(mAdapter);
                    }
                });
    }



    public void updateItemInRapid(final test item) {
        // TODO merge or mutate item
        item.setClicked(item.getClicked() + 1);

        Rapid.getInstance().collection("tests", test.class)
                .document(item.id)
                .mutate(item);
    }


    public void deleteItemFromRapid(final test item) {
        Rapid.getInstance().collection("tests", test.class)
                .document(item.id)
                .delete();
    }


    private void unsubscribeFromRapid() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }


    private void setupAdapter() {
        mAdapter = new SimpleDataBoundRecyclerAdapter<ActivityMainItemBinding>(R.layout.activity_test, this, mItems) {
        };

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
    }


    private void setupOrdering() {
        final ImageView orderArrow = findViewById(R.id.order_arrow);
        orderArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClickedOrdering == Sorting.ASC) {
                    mClickedOrdering = Sorting.DESC;
                    orderArrow.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
                } else {
                    mClickedOrdering = Sorting.ASC;
                    orderArrow.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
                }

                unsubscribeFromRapid();
                subscribeDataFromRapid();
            }
        });
    }


    private void setupInput() {
        final EditText input = findViewById(R.id.input_text);
        Button button = findViewById(R.id.button_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemToRapid(input.getText().toString());
                input.setText("");
            }
        });
    }

}
