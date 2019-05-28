package com.eric.android.view.excelpanel;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.eric.android.view.excelpanel.bean.ExcelPanelRow;

import java.util.ArrayList;
import java.util.List;

public class ExcelPanel extends FrameLayout {
    private final RecyclerView recycleMain;
    private int headerColor1;
    private int headerColor2;
    private int headerWidth;
    private int cellGap;//列间距
    private int cellWidth;
    private int cellTxtColor;
    private int headerTxtSize;
    private int cellTxtSize;
    private int rowDividerColor;
    private int rowDividerHeight;

    public ExcelPanel(@NonNull Context context) {
        this(context, null);
    }

    public ExcelPanel(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExcelPanel(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //添加主布局
        addView(LayoutInflater.from(context).inflate(R.layout.layout_excel_panel_main, this, false));
        //获取控件
        recycleMain = findViewById(R.id.recyclerView_main);
        recycleMain.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recycleMain.setNestedScrollingEnabled(false);
        //获取相关样式属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExcelPanel);
        if (typedArray != null) {
            headerWidth = typedArray.getDimensionPixelSize(R.styleable.ExcelPanel_header_width, dp2px(context, 60));
            cellGap = typedArray.getDimensionPixelSize(R.styleable.ExcelPanel_cell_gap, dp2px(context, 10));
            headerTxtSize = typedArray.getInt(R.styleable.ExcelPanel_header_text_size, 14);
            headerColor1 = typedArray.getColor(R.styleable.ExcelPanel_header_text_color_1, getResources().getColor(R.color.black));
            headerColor2 = typedArray.getColor(R.styleable.ExcelPanel_header_text_color_2, getResources().getColor(R.color.gray));
            cellWidth = typedArray.getDimensionPixelSize(R.styleable.ExcelPanel_cell_width, dp2px(context, 50));
            cellTxtColor = typedArray.getColor(R.styleable.ExcelPanel_cell_text_color, getResources().getColor(R.color.gray));
            cellTxtSize = typedArray.getInt(R.styleable.ExcelPanel_cell_text_size, 14);
            rowDividerColor = typedArray.getInt(R.styleable.ExcelPanel_row_divider_color, 0xffffffff);
            rowDividerHeight = typedArray.getDimensionPixelSize(R.styleable.ExcelPanel_row_divider_height, 0);
        }
    }


    private int dp2px(Context context, float dp) {
        final float density = context.getResources().getDisplayMetrics().density;
        return (int) (density * dp + 0.5f);
    }

    /**
     * 设置数据源(header列表需要指定标题级别,用于设置样式)
     */
    public void setDatas(List<ExcelPanelRow> rows) {
        recycleMain.setAdapter(new MainAdapter(rows));
    }

    private class MainAdapter extends RecyclerView.Adapter<VH> {
        private final List<ExcelPanelRow> datas;
        private final List<RecyclerView> recyleViewRows = new ArrayList<>();
        private View touchView;

        MainAdapter(List<ExcelPanelRow> datas) {
            this.datas = datas;
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_excel_panel_row, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final VH holder, int position) {
            ExcelPanelRow item = datas.get(position);
            holder.tvHeader.setText(item.getHeader());
            holder.tvHeader.getLayoutParams().width = headerWidth;
            holder.spacer.getLayoutParams().height = rowDividerHeight;
            holder.spacer.setBackgroundColor(rowDividerColor);
            holder.recyclerViewRow.setPadding(0, 0, 0, rowDividerHeight);
            if (item.headerLevel == 0)
                holder.tvHeader.setTextColor(headerColor1);
            else
                holder.tvHeader.setTextColor(headerColor2);
            if (!recyleViewRows.contains(holder.recyclerViewRow)) {
                recyleViewRows.add(holder.recyclerViewRow);
            }
            holder.recyclerViewRow.setLayoutManager(new LinearLayoutManager(holder.recyclerViewRow.getContext(), LinearLayoutManager.HORIZONTAL, false));
            holder.recyclerViewRow.setNestedScrollingEnabled(false);
            holder.recyclerViewRow.setAdapter(new RowAdapter(item.getRowItems()));
            holder.recyclerViewRow.clearOnScrollListeners();
            holder.recyclerViewRow.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    //其他recycleView禁止滑动
                    for (RecyclerView rv : recyleViewRows) {
                        rv.stopScroll();
                    }
                    touchView = view;
                    return false;
                }
            });
            holder.recyclerViewRow.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (recyclerView != touchView) return;//防止重复滚动
                    //同步其他recycleview滚动
                    for (RecyclerView rv : recyleViewRows) {
                        if (rv != recyclerView) {
                            rv.scrollBy(dx, dy);
                        }
                    }
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    //同步一下滚动
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (recyclerView != touchView) return;//防止重复滚动
                        //同步其他recycleview滚动
                        for (RecyclerView rv : recyleViewRows) {
                            if (rv != recyclerView) {
                                rv.scrollBy(recyclerView.getScrollX(), recyclerView.getScrollY());
                            }
                        }
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return datas == null ? 0 : datas.size();
        }
    }

    private static class VH extends RecyclerView.ViewHolder {
        View spacer;
        TextView tvHeader;
        RecyclerView recyclerViewRow;

        public VH(View itemView) {
            super(itemView);
            spacer = itemView.findViewById(R.id.spacer);
            tvHeader = itemView.findViewById(R.id.tv_header);
            recyclerViewRow = itemView.findViewById(R.id.recyclerView_row);

        }
    }

    private class RowAdapter extends RecyclerView.Adapter<RHV> {

        private final List<ExcelPanelRow.RowItem> cells;

        RowAdapter(List<ExcelPanelRow.RowItem> cells) {
            this.cells = cells;
        }

        @NonNull
        @Override
        public RHV onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RHV(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_excel_panel_row_cell, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RHV holder, int position) {
            if (position == 0) {
                holder.cellGapView.setVisibility(GONE);
            } else {
                holder.cellGapView.setVisibility(VISIBLE);
            }
            holder.cellGapView.getLayoutParams().width = cellGap;
            holder.tvCell.setTextColor(cellTxtColor);
            holder.tvCell.setTextSize(cellTxtSize);
            holder.tvCell.setText(cells.get(position).getContent());
        }

        @Override
        public int getItemCount() {
            return cells == null ? 0 : cells.size();
        }
    }

    private static class RHV extends RecyclerView.ViewHolder {
        View cellGapView;
        TextView tvCell;

        public RHV(View itemView) {
            super(itemView);
            cellGapView = itemView.findViewById(R.id.cell_gap);
            tvCell = itemView.findViewById(R.id.tv_cell);
        }
    }
}
