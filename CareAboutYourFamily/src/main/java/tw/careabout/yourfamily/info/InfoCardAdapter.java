package tw.careabout.yourfamily.info;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tw.careabout.yourfamily.R;

public class InfoCardAdapter extends RecyclerView.Adapter <InfoCardAdapter.CardVH>{
    private final List<InfoCard> cardList;
    OnCardListen onCardListen;

    public InfoCardAdapter(List<InfoCard> cardList, OnCardListen onCardListen) {
        this.cardList = cardList;
        this.onCardListen=onCardListen;
    }

    @NonNull
    @Override
    public CardVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info, parent, false);
        return new CardVH(view,onCardListen);
    }

    @Override
    public void onBindViewHolder(@NonNull CardVH holder, int position) {
        InfoCard card = cardList.get(position);
        holder.titleTxt.setText(card.getTitle());
        holder.phoneTxt.setText(card.getT001());
        holder.addressTxt.setText(card.getT002());
        holder.urlTxt.setText(card.getT003());
        holder.contentTxt.setText(card.getT000());

        holder.otherTxt.setText(card.getT010());
        holder.areaTxt.setText(card.getT100());

        boolean isExpanable = cardList.get(position).isExpandable();
        holder.expendableLayout.setVisibility(isExpanable ? View.VISIBLE : View.GONE);


        emptyandgone(card.getT003(),holder.urlTxt);
        emptyandgone(card.getT000(),holder.contentTxt);

        if(card.getTitle().equals("null")){
            holder.titleTxt.setText(cardList.get((position - 1)).getTitle());
        }



    }

    void emptyandgone(String a, TextView b){
        if(a.trim()==""||a==null || a.equals("null")){
            b.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public class CardVH extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView titleTxt,phoneTxt,addressTxt,urlTxt,contentTxt;
        private final TextView otherTxt,areaTxt;
        private final View linearLayout,expendableLayout;
        OnCardListen onCardListen;

        public CardVH(@NonNull View itemView,OnCardListen onCardListen) {
            super(itemView);
            this.onCardListen=onCardListen;

            titleTxt = itemView.findViewById(R.id.title);
            phoneTxt = itemView.findViewById(R.id.t001);
            addressTxt = itemView.findViewById(R.id.t002);
            urlTxt = itemView.findViewById(R.id.t003);

            contentTxt=itemView.findViewById(R.id.t000);
            otherTxt=itemView.findViewById(R.id.t010);
            areaTxt=itemView.findViewById(R.id.t100);



            otherTxt.setVisibility(View.GONE);
            areaTxt.setVisibility(View.GONE);


            linearLayout = itemView.findViewById(R.id.linearlayout);
            expendableLayout = itemView.findViewById(R.id.expandablelayout);
            titleTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InfoCard cards = cardList.get(getAdapterPosition());
                    cards.setExpandable(!cards.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });
            phoneTxt.setOnClickListener(this);
            addressTxt.setOnClickListener(this);
            urlTxt.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onCardListen.OnCardClick(getAdapterPosition(),v);
        }
    }


    public interface OnCardListen{
        void OnCardClick(int position, View v);
    }
}
