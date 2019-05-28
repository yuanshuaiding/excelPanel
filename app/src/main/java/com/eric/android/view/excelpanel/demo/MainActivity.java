package com.eric.android.view.excelpanel.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.eric.android.view.excelpanel.ExcelPanel;
import com.eric.android.view.excelpanel.bean.ExcelPanelRow;

import java.util.ArrayList;
import java.util.List;

import cn.zhouchaoyuan.excelpaneldemo.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ExcelPanel excelPanel = findViewById(R.id.excel_panel);
        List<ExcelPanelRow> rows = new ArrayList<>();
        int i = 20;
        for (int i1 = 0; i1 < i; i1++) {
            ExcelPanelRow row = new ExcelPanelRow();
            row.header = "标题" + i1;
            row.rowItems = new ArrayList<>();
            int j = 10;
            for (int i2 = 0; i2 < j; i2++) {
                ExcelPanelRow.RowItem rowItem = new ExcelPanelRow.RowItem();
                rowItem.content = "单元格单元格单元格单元格单元格单元格单元格单元格单元格单元格单元格单元格单元格单元格单元格单元格单元格单元格单元格单元格单元格单元格单元格单元格单元格单元格单元格单元格单元格" + i2;
                row.rowItems.add(rowItem);
            }
            rows.add(row);
        }
        excelPanel.setDatas(rows);
    }

}
