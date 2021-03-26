package tw.careabout.yourfamily.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tw.careabout.yourfamily.R;

import java.util.ArrayList;


public class HOME_elder_RecyclerAdapter extends RecyclerView.Adapter<HOME_elder_RecyclerAdapter.ViewHolder> implements View.OnClickListener {
    private final Context mContext;
    private final ArrayList<Post24> mData;
    //    -------------------------------------------------------------------
    private OnItemClickListener mOnItemClickListener = null;

    //--------------------------------------------
    public HOME_elder_RecyclerAdapter(Context context, ArrayList<Post24> data) {
        this.mContext = context;
        this.mData = data;
    }

    //    -------------------------------------------------------------------
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    //-------------------------------------------------------------------
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.elder_cell_post, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.t001 = (TextView) view.findViewById(R.id.elder_cell_post_t001);
        //holder.t001.setOnClickListener(this);
        holder.t002 = (TextView) view.findViewById(R.id.elder_cell_post_t002);
        holder.t003 = (TextView) view.findViewById(R.id.elder_cell_post_t003);
        holder.t004 = (TextView) view.findViewById(R.id.elder_cell_post_t004);
        holder.t005 = (TextView) view.findViewById(R.id.elder_cell_post_t005);
        holder.t006 = (TextView) view.findViewById(R.id.elder_cell_post_t006);
        holder.t007 = (TextView) view.findViewById(R.id.elder_cell_post_t007);
        holder.t008 = (TextView) view.findViewById(R.id.elder_cell_post_t008);
        holder.t009 = (TextView) view.findViewById(R.id.elder_cell_post_t009);
        holder.t009.setOnClickListener(this);
        holder.t010 = (TextView) view.findViewById(R.id.elder_cell_post_t010);
        holder.t010.setOnClickListener(this);
        //----------------------------------------------------
        //將創建的View註冊點擊事件
        view.setOnClickListener(this);
//        holder
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Post24 post = mData.get(position);
        //-----------------------------------
        holder.t002.setText(post.mem0002);
        holder.t004.setText(post.mem0003);
        holder.t006.setText(post.mem0004);
        holder.t008.setText(post.mem0005);
        holder.t009.setTag(position);
        holder.t010.setTag(position);
        holder.itemView.setTag(position);
    }



    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意這裡使用getTag方法獲取position
            mOnItemClickListener.onItemClick(v, (int)v.getTag());
        }

    }

    //define interface
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    //======= sub class   ==================
    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView t001,t002,t003,t004,t005,t006,t007,t008,t009,t010;
        public ViewHolder(View itemView) {
            super(itemView);
        }


    }
//-----------------------------------------------
}

//public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements View.OnClickListener {
//    private Context mContext;
//    private ArrayList<Post> mData;
//    //    -------------------------------------------------------------------
//    private OnItemClickListener mOnItemClickListener = null;
//    //--------------------------------------------
//    public RecyclerAdapter(Context context, ArrayList<Post> data) {
//        this.mContext = context;
//        this.mData = data;
//    }
//    //    -------------------------------------------------------------------
//    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
//        this.mOnItemClickListener = mOnItemClickListener;
//    }
//    //-------------------------------------------------------------------
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater
//                .from(mContext)
//                .inflate(R.layout.cell_post, parent, false);
//        ViewHolder holder = new ViewHolder(view);
//        holder.img = (ImageView) view.findViewById(R.id.img);
//        holder.Name = (TextView) view.findViewById(R.id.Name);
//        holder.Content = (TextView) view.findViewById(R.id.Content);
//        holder.Add = (TextView) view.findViewById(R.id.Addr);
//        holder.Zipcode = (TextView) view.findViewById(R.id.Zipcode);
//        //----------------------------------------------------
//        //將創建的View註冊點擊事件
//        view.setOnClickListener(this);
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        final Post post = mData.get(position);
//        holder.Name.setText(post.Name);
//        holder.Add.setText(post.Add);
//        holder.Content.setText(post.Content);
//        if (post.Zipcode.length()>0){
//            holder.Zipcode.setText("["+post.Zipcode+"]");
//        }else{
//            holder.Zipcode.setText("[000]");
//        }
//
//
//
//        //========線上抓取網路的地方=======================================
//
//        Glide.with(mContext)
//                .load(post.posterThumbnailUrl)
////                .skipMemoryCache(true)
////                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                .override(100, 75)
//                .transition(withCrossFade())
//                .error(
//                        Glide.with(mContext)
//                                .load("https://truth.bahamut.com.tw/s01/202102/6e729754af77b4da0b2145730323c713.JPG"))
//                .into(holder.img);
//
//        //將position保存在itemView的Tag中，以便點擊時進行獲取
//        holder.itemView.setTag(position);
//    }
//
//    @Override
//    public int getItemCount() {
//        return mData.size();
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (mOnItemClickListener != null) {
//            //注意這裡使用getTag方法獲取position
//            mOnItemClickListener.onItemClick(v, (int) v.getTag());
//        }
//    }
//
//    //define interface
//    public static interface OnItemClickListener {
//        void onItemClick(View view, int position);
//    }
//
//    //======= sub class   ==================
//    class ViewHolder extends RecyclerView.ViewHolder {
//        public ImageView img;
//        public TextView Name;
//        public TextView Add;
//        public TextView Content;
//        public TextView Zipcode;
//        public ViewHolder(View itemView) {
//            super(itemView);
//        }
//    }
//-----------------------------------------------
//}
