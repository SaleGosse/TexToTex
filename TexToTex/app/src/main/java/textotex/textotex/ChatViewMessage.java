package textotex.textotex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class ChatViewMessage extends ArrayAdapter<ManagerChat> {

    private TextView chatText;
    private List<ManagerChat> chatMessageList = new ArrayList<>();
    private Context context;

    @Override
    public void add(ManagerChat object) {
        chatMessageList.add(object);
        super.add(object);
    }

    public  ChatViewMessage(Context context, int textViewResourceId)
    {
        super(context, textViewResourceId);
        this.context = context;
    }

    public int getCount() {
        return this.chatMessageList.size();
    }


    public ManagerChat getItem(int index) {
        return this.chatMessageList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ManagerChat chatMessageObj = getItem(position);
        View row = convertView;
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //row = inflater.inflate(R.layout.right , parent, false);
        if (chatMessageObj.left) {
            row = inflater.inflate(R.layout.right, parent, false);

        } else {
            row = inflater.inflate(R.layout.left, parent, false);
        }
        //rowBis = inflater.inflate(R.layout.right, parent, false);
        chatText = row.findViewById(R.id.textViewTime);
        chatText.setText(chatMessageObj.date);
        chatText = row.findViewById(R.id.msgr);
        chatText.setText(chatMessageObj.message);
        chatText = row.findViewById(R.id.name);
        chatText.setText(chatMessageObj.name);
        return row;
    }
}