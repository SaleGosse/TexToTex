package textotex.textotex;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class friendListAdapter extends ArrayAdapter<friendListData>{
    private Context mContext;
    private List<friendListData> mListData;

    public friendListAdapter(Context context, List<friendListData> listData) {
        super(context, R.layout.friend_list_row, listData);

        this.mContext = context;
        this.mListData = listData;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(this.mContext);

        View rowView = inflater.inflate(R.layout.friend_row, null, true);

        TextView mItemName = (TextView)rowView.findViewById(R.id.row_name);

        friendListData data = mListData.get(position);

        mItemName.setText(data.getConversationUserName());

        //TODO: do something with the first letter of the username to display the right conv icon

        return rowView;
    }


}
