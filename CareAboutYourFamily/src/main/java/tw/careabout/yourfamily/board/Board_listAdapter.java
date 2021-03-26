package tw.careabout.yourfamily.board;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tw.careabout.yourfamily.R;

import java.util.ArrayList;



public class Board_listAdapter extends RecyclerView.Adapter <Board_listAdapter.Board_viewhoder>implements View.OnClickListener{

    private ArrayList<SimpleBoardData> recycleData = new ArrayList<>();
    private Context mContext;

    public Board_listAdapter(Context context, ArrayList<SimpleBoardData> data){
        recycleData = data;
        mContext = context;
    }

    @NonNull
    @Override
    public Board_viewhoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.board_list, parent, false);
        view.setOnClickListener(this);
        return new Board_viewhoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Board_viewhoder holder, int position) {
        holder.list_title.setText(recycleData.get(position).getTitle());
        holder.list_creater.setText(recycleData.get(position).getCreater());
        holder.list_createTime.setText(recycleData.get(position).getCreateTime());

        //將position保存在itemView的Tag中，以便點擊時進行獲取
        holder.itemView.setTag(position);
    }


    @Override
    public int getItemCount() {
        return recycleData.size();
    }

    //-----------------------------------監聽實作----------------------------------------------------

    private OnItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意這裡使用getTag方法獲取position
            mOnItemClickListener.onItemClick(v, (int)v.getTag());
        }
    }

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    //----------------------------------------innerClass--------------------------------------------
    public class Board_viewhoder extends RecyclerView.ViewHolder {

        private TextView list_title,list_creater,list_createTime;
        private int nowPosition;

        public Board_viewhoder(@NonNull View itemView) {
            super(itemView);
            list_title = (TextView)itemView.findViewById(R.id.board_list_title);
            list_creater = (TextView)itemView.findViewById(R.id.board_list_creater);
            list_createTime = (TextView)itemView.findViewById(R.id.board_list_createTime);
        }

        public int getNowPosition(){
            nowPosition = getAdapterPosition();
            return nowPosition;
        }
    }

}
