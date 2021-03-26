package tw.careabout.yourfamily.med;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tw.careabout.yourfamily.R;

public class MedShow_CardAdapter extends RecyclerView.Adapter<MedShow_CardAdapter.CardVH> {

    private final List<MedShow_Card> cardList;
    OnCardListen onCardListen;

    public MedShow_CardAdapter(List<MedShow_Card> cardList, OnCardListen onCardListen) {
        this.cardList = cardList;
        this.onCardListen=onCardListen;
    }

    @NonNull
    @Override
    public CardVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medshow_card, parent, false);
        return new CardVH(view,onCardListen);
    }

    @Override
    public void onBindViewHolder(@NonNull CardVH holder, final int position) {

        final MedShow_Card card = cardList.get(position);
        holder.title.setText(card.getMed002());
        holder.time.setText(card.getMed003());
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public class CardVH extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title,time;
        OnCardListen onCardListen;
        public CardVH(@NonNull View itemView, OnCardListen onCardListen) {
            super(itemView);
            this.onCardListen=onCardListen;

            title = itemView.findViewById(R.id.medshow_content);
            time = itemView.findViewById(R.id.medshow_time);
            itemView.findViewById(R.id.medshow_edit).setOnClickListener(this);
            itemView.findViewById(R.id.medshow_del).setOnClickListener(this);


//            linearLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    MedShow_Card cards = cardList.get(getAdapterPosition());
//                    notifyItemChanged(getAdapterPosition());
//                }
//            });
        }

        @Override
        public void onClick(View v) {
            onCardListen.OnCardClick(getAdapterPosition(),v);
        }
    }


    //新增-------------------------------------------------------------
    public interface OnCardListen{
        void OnCardClick(int position,View v);
    }
}
