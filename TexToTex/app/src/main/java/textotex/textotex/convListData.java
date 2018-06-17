package textotex.textotex;

public class convListData {
    private int mConversationID;
    private String mConversationName;
    private String mContent;
    private String mDate;
    private boolean mUnread;

    public convListData(int conversationID, String username, String content, String date, Boolean unread) {
        this.mConversationID = conversationID;
        this.mConversationName = username;
        this.mContent = content;
        this.mDate = date;
        this.mUnread = unread;
    }

    public int getConversationID() {
        return this.mConversationID;
    }

    public String getConversationName() { return this.mConversationName; }

    public String getContent() {
        return this.mContent;
    }

    public String getDate() {
        return this.mDate;
    }

    public boolean getUnread() { return this.mUnread; }

    public void toggleUnread() { this.mUnread = !(this.mUnread); }



}
