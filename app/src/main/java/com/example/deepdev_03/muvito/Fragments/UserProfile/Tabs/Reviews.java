package com.example.deepdev_03.muvito.Fragments.UserProfile.Tabs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.deepdev_03.muvito.Activities.UserProfileActivity;
import com.example.deepdev_03.muvito.Adapters.RecyclerView.UserData.RatingUserListAdapter;
import com.example.deepdev_03.muvito.Model.UserData;
import com.example.deepdev_03.muvito.R;
import com.example.deepdev_03.muvito.Utils.ItemClickSupportWithHeader;
import com.example.deepdev_03.muvito.Utils.ItemClickSupportWithNoHeader;

import java.util.ArrayList;

public class Reviews extends Fragment
{
    private final String OFFERS_KEY = "offerItem";
    private RecyclerView recyclerView;
    private ArrayList<UserData> items;
    private RecyclerView.LayoutManager layoutManager;
    private LinearLayout tablayoutHolder;
    private RatingUserListAdapter itemsAdapter;
    private NestedScrollView nestedScrollView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.user_profile_rewies_tab, container, false);
        this.findViews(rootView);
        this.items = this.getItems();
        this.initRecyclerView();
        this.initNestedScroll();
        this.initSwipeRefresh();

        return rootView;
    }

    private void findViews(View root)
    {
        this.recyclerView = (RecyclerView) root.findViewById(R.id.user_profile_reviewers_recycler_view);
        this.tablayoutHolder = (LinearLayout) getActivity().findViewById(R.id.main_tablayout_holder);
        this.nestedScrollView = (NestedScrollView) root.findViewById(R.id.user_profile_reviewers_nested_scroll);
        this.swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.user_profile_review_tab_swipe_refresh_layout);
    }

    private ArrayList<UserData> getItems()
    {
        Bitmap image;
        ArrayList<UserData> items = new ArrayList<>();

        for(int i = 0; i <= 10; i++)
        {
            image = BitmapFactory.decodeResource(getResources(), R.drawable.empty_img);
            items.add(new UserData( i + " name ", i + " location ", i + " date ", image, 3));
        }

        return items;
    }

    private void initRecyclerView()
    {
        this.layoutManager = new LinearLayoutManager(getContext());
        this.itemsAdapter = new RatingUserListAdapter(this.items);
        this.recyclerView.setNestedScrollingEnabled(false);
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.recyclerView.setAdapter(this.itemsAdapter);

        ItemClickSupportWithNoHeader.addTo(this.recyclerView).setOnItemClickListener(new ItemClickSupportWithHeader.OnItemClickListener()
        {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v)
            {
                System.out.println("trying to start offers activity with position " + position);
                Intent intent = new Intent(getActivity().getApplicationContext(), UserProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initNestedScroll()
    {
        this.nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener()
        {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY)
            {
                if(oldScrollY < scrollY)
                {
                    hideViews();
                }
                if(oldScrollY > scrollY)
                {
                    showViews();
                }
            }
        });
    }

    private void initSwipeRefresh()
    {
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                Toast.makeText(getActivity().getApplicationContext(), "Start refreshing list", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(true);
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity().getApplicationContext(), "End of refreshing", Toast.LENGTH_SHORT).show();
                    }
                }, 1000);
            }
        });
    }

    public void hideViews()
    {
        if(this.tablayoutHolder != null)
        {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) this.tablayoutHolder.getLayoutParams();
            int tabsBottomMargin = lp.bottomMargin;
            this.tablayoutHolder.animate().translationY(this.tablayoutHolder.getHeight() + tabsBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
        }
    }

    private void showViews()
    {
        if(this.tablayoutHolder != null)
        {
            this.tablayoutHolder.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
        }
    }
}
