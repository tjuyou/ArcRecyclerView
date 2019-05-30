package com.you.arcrecyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ArcRecyclerView mArcRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mArcRecyclerView = findViewById(R.id.arc_view);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mArcRecyclerView.setLayoutManager(mLayoutManager);
        ArcRecyclerAdapter mCircleAdapter = new ArcRecyclerAdapter(this);
        mArcRecyclerView.setAdapter(mCircleAdapter);
        mCircleAdapter.setOnCircleItemClickListener(new ArcRecyclerAdapter.OnCircleItemClickListener() {
            @Override
            public void onCircleItemClick(View view, int position) {
                switch (position % 5) {
                    case 0:
                        Toast.makeText(MainActivity.this, "1", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, "2", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "3", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(MainActivity.this, "4", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(MainActivity.this, "5", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}
