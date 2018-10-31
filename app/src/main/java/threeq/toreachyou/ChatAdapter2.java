//리사이클러뷰 버전 - 클릭이벤트 처리가 어려워서 우선 보류
package threeq.toreachyou;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatAdapter2 extends RecyclerView.Adapter<ChatAdapter2.ViewHolder> {

    ArrayList<ChatData> mItems;
    Context context;
    public ChatAdapter2(ArrayList<ChatData> items, Context context){
        mItems = items;
        this.context = context;
    }



    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_chat, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatData chatData = mItems.get(position);
        holder.mTxtUserName.setText(chatData.userName);
        holder.mTxtTitle.setText(chatData.title);
        holder.mTxtMessage.setText(chatData.message);
        holder.mTxtTime.setText(chatData.time);

    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTxtUserName, mTxtTitle, mTxtMessage, mTxtTime;

        public ViewHolder(View itemView) {
            super(itemView);
            mTxtUserName = (TextView) itemView.findViewById(R.id.txt_userName);
            mTxtTitle = (TextView) itemView.findViewById(R.id.txt_title);
            mTxtMessage = (TextView) itemView.findViewById(R.id.txt_message);
            mTxtTime = (TextView) itemView.findViewById(R.id.txt_time);
        }

    }
}
