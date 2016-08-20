package com.axnshy.cloudmusic.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.axnshy.cloudmusic.DBHelper.ListsInfoDao;
import com.axnshy.cloudmusic.FilesRead.ListsInfo;
import com.axnshy.cloudmusic.FilesRead.MusicInfo;
import com.axnshy.cloudmusic.R;

import java.util.List;

/**
 * Created by axnshy on 16/8/11.
 */
public class CollectDialog extends Dialog {

    private List<ListsInfo> mList;
    private Context context;
    private RecyclerView recycler;
    private MusicInfo musicInfo;
    private Adapter mAdapter;

    public CollectDialog(Context context, List<ListsInfo> mList,MusicInfo music) {
        super(context);
        this.mList = mList;
        this.context = context;
        this.musicInfo=music;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_collect);
        recycler= (RecyclerView) findViewById(R.id.recycler_dialog_list);
        mAdapter=new Adapter(context,mList);
        mAdapter.setOnClickListener(new Adapter.OnClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ListsInfoDao.getDAO().addMusicToList(context,musicInfo,mList.get(position).getListId());
                dismiss();
            }
            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
        recycler.setAdapter(mAdapter);
        LinearLayoutManager manager=new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(manager);
    }


}
class Adapter extends RecyclerView.Adapter<MViewHolder>{

    private LayoutInflater mInflater;
    private Context context;
    private List<ListsInfo> mList;
    public Adapter(Context context,List<ListsInfo> List){
        this.context=context;
        this.mList=List;
        mInflater = LayoutInflater.from(context);
    }

    OnClickListener mListener;

    public interface OnClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(OnClickListener listener){
        this.mListener=listener;
    }


    @Override
    public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.dialog_list_item, parent, false);
        MViewHolder viewHolder = new MViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(final MViewHolder holder, final int position) {
        Log.w("TAG","image path ----------   "+mList.get(position).getBackgroundPath());
        holder.listAvatarImg.setImageBitmap(BitmapFactory.decodeFile(mList.get(position).getBackgroundPath()));
        holder.listNameTx.setText(mList.get(position).getListName());
        if(mListener!=null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(holder.itemView,position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mListener.onItemLongClick(holder.itemView,position);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
class MViewHolder extends RecyclerView.ViewHolder{

//    @ViewInject(R.id.iv_dialog_list_avatar)
    ImageView listAvatarImg;
//    @ViewInject(R.id.tv_dialog_list_name)
    TextView listNameTx;

    public MViewHolder(View itemView) {
        super(itemView);
        listAvatarImg= (ImageView) itemView.findViewById(R.id.iv_dialog_list_avatar);
        listNameTx= (TextView) itemView.findViewById(R.id.tv_dialog_list_name);
    }
}
