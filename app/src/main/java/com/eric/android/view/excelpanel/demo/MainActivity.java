package com.eric.android.view.excelpanel.demo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.eric.android.view.excelpanel.ExcelPanel;
import com.eric.android.view.excelpanel.OnExcelPanelScrollListener;
import com.eric.android.view.excelpanel.OnExcelPanelTextCopyListener;
import com.eric.android.view.excelpanel.bean.ExcelPanelRow;
import com.eric.android.view.excelpanel.util.RecycleViewUtil;
import com.eric.android.view.excelpanel.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import cn.zhouchaoyuan.excelpaneldemo.R;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.eric.android.view.excelpanel.util.DPUtil.dp2px;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recycler_year;
    private ArrayList<String> titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recycler_year = findViewById(R.id.recycler_year);
        recycler_year.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        final ExcelPanel excelPanel = findViewById(R.id.excel_panel);
        titles = new ArrayList<>();
        List<ExcelPanelRow> rows = new ArrayList<>();
        int i = 20;
        int j = 15;
        for (int i1 = 0; i1 < i; i1++) {
            ExcelPanelRow row = new ExcelPanelRow();
            row.header = "标题" + i1;
            row.headerLevel = i1 % 3;
            row.rowItems = new ArrayList<>();
            for (int i2 = 0; i2 < j; i2++) {
                ExcelPanelRow.RowItem rowItem = new ExcelPanelRow.RowItem();
                if (row.headerLevel == 0)
                    rowItem.content = null;
                else {
                    if (i2 == 5) {
                        rowItem.content = "格单元格单元格格单元格单元格格单元格单格单元格单元格格单元格单元格单元格元格格单元格单元格格单元格单元格格单元格单元格格单元格单元格格单元格单元格格单元格单元格格单元格单元格" + i2;
                    } else {
                        rowItem.content = "格单元格单元格" + i2;
                    }
                }
                row.rowItems.add(rowItem);
            }
            rows.add(row);
        }
        //标题,用于联动
        for (int i1 = 0; i1 < j; i1++) {
            titles.add("标题" + i1);
        }
        excelPanel.setDatas(rows);
        recycler_year.setAdapter(new MyAdapter());
        new RecycleViewUtil(MainActivity.this, recycler_year).setOnItemLongClickListener(new RecycleViewUtil.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position, View view) {
                TextView tvHeader = view.findViewById(R.id.tv_cell);
                if (tvHeader != null) {
                    ViewUtil.copyText(MainActivity.this, tvHeader);
                    Toast.makeText(MainActivity.this, tvHeader.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        excelPanel.setUpScroll(recycler_year);
        excelPanel.canCopy(true, new OnExcelPanelTextCopyListener() {
            @Override
            public void onCopy(String text) {
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
        excelPanel.setOnScrollListener(new OnExcelPanelScrollListener() {
            @Override
            public void onScrolled(ExcelPanel excelPanel, int dx, int dy) {
                Log.d("MainActivityExcelScroll", "dx=" + dx + ",dy=" + dy);
            }

            @Override
            public void onScrollStateChanged(ExcelPanel excelPanel, int newState) {
                Log.d("MainActivityExcelScroll", "newState=" + newState);
            }
        });
        Button btnL = findViewById(R.id.btn_left);
        Button btnR = findViewById(R.id.btn_right);
        btnL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                excelPanel.goLeft();
            }
        });
        btnR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                excelPanel.goRight();
            }
        });
    }

    private class MyAdapter extends RecyclerView.Adapter<ExcelPanel.ExcelRowVH> {
        @NonNull
        @Override
        public ExcelPanel.ExcelRowVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ExcelPanel.ExcelRowVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_excel_panel_row_cell, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ExcelPanel.ExcelRowVH holder, int position) {
            holder.cellGapView.getLayoutParams().width = dp2px(MainActivity.this, 15);
            if (position == 0) {
                holder.cellGapView.setVisibility(GONE);
            } else {
                holder.cellGapView.setVisibility(VISIBLE);
            }
            holder.tvCell.setText(titles.get(position));
        }

        @Override
        public int getItemCount() {
            return titles.size();
        }
    }

}
