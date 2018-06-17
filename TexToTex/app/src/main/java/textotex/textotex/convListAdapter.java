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

public class convListAdapter extends ArrayAdapter<convListData> {
    private Context mContext;
    private List<convListData> mListData;

    public convListAdapter(Context context, List<convListData> listData) {
        super(context, R.layout.conv_list_row, listData);

        this.mContext = context;
        this.mListData = listData;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(this.mContext);

        View rowView = inflater.inflate(R.layout.conv_list_row, null, true);


        TextView mItemName = (TextView)rowView.findViewById(R.id.row_name);
        TextView mItemContent = (TextView)rowView.findViewById(R.id.row_message);
        TextView mItemDate = (TextView)rowView.findViewById(R.id.row_date);
        ImageView mItemUnread = (ImageView)rowView.findViewById(R.id.row_unread);

        convListData data = mListData.get(position);

        mItemName.setText(data.getConversationName());
        mItemContent.setText(data.getContent());
        mItemDate.setText(data.getDate());

        if(data.getUnread())
            mItemUnread.setVisibility(ImageView.VISIBLE);
        else
            mItemUnread.setVisibility(ImageView.GONE);

        //TODO: do something with the first letter of the username to display the right conv icon

        return rowView;
    }


}
