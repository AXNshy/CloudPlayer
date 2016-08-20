package com.axnshy.cloudmusic.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.axnshy.cloudmusic.FilesRead.ListsInfo;
import com.axnshy.cloudmusic.R;

import java.util.List;

/**
 * Created by axnshy on 16/8/9.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<ListsInfo> mListsList;
    private LayoutInflater mInflater;
    private int screenWidth;
    private int screenHeight;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public RecyclerViewAdapter(Context context, List<ListsInfo> list, int width, int length) {
        super();
        this.context = context;
        this.mListsList = list;
        this.screenHeight = length;
        this.screenWidth = width;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.music_list_view, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.listAvatarLayout.getLayoutParams();
        Log.w("TAG", "screenWidth--------" + screenWidth + "     screenHeight----------" + screenHeight +"    --avatar path"+mListsList.get(position).getBackgroundPath());
        params.width = (screenWidth - 4 * 8) / 3;
        params.height = (int) (params.width * 1.414);
        holder.listAvatarLayout.setLayoutParams(params);
//        holder.listCountTx = (TextView) holder.findViewById(R.id.tv_musiclist_name);
//        holder.listNameTx = (TextView) itemView.findViewById(R.id.tv_musiclist_count);
        holder.listNameTx.setText(mListsList.get(position).getListName());
        holder.listCountTx.setText(mListsList.get(position).getListCount() + "");
        if(mListsList.get(position).getBackgroundPath()!=null) {
            holder.listAvatarLayout.setBackground(Drawable.createFromPath(mListsList.get(position).getBackgroundPath()));
        }
        else
            holder.listAvatarLayout.setBackgroundResource(R.drawable.h1);
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemClickListener.onItemLongClick(holder.itemView,position);
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mListsList.size();
    }
}

//@ContentView(R.layout.music_list_view)
class MyViewHolder extends RecyclerView.ViewHolder {

    RelativeLayout listAvatarLayout;
    TextView listNameTx;
    TextView listCountTx;

    public MyViewHolder(View itemView) {
        super(itemView);
        listAvatarLayout = (RelativeLayout) itemView.findViewById(R.id.layout_list_avatar);
        listNameTx = (TextView) itemView.findViewById(R.id.tv_musiclist_name);
        listCountTx = (TextView) itemView.findViewById(R.id.tv_musiclist_count);
    }
}