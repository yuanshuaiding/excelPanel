package com.eric.android.view.excelpanel.bean;

import android.text.TextUtils;

import java.util.List;

public class ExcelPanelRow {
    public String header;
    public int headerLevel;
    public List<RowItem> rowItems;

    public ExcelPanelRow() {
    }

    public ExcelPanelRow(String header) {
        this(header, 0);
    }

    public ExcelPanelRow(String header, int headerLevel) {
        this.header = header;
        this.headerLevel = headerLevel;
    }

    public List<RowItem> getRowItems() {
        return rowItems;
    }

    public String getHeader() {
        return TextUtils.isEmpty(header) ? "" : header;
    }

    public static class RowItem {

        public String content;

        public RowItem() {
        }

        public RowItem(String content) {
            this.content = content;
        }

        public String getContent() {
            return TextUtils.isEmpty(content) ? "" : content;
        }
    }
}
