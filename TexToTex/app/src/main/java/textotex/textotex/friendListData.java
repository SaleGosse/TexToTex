package textotex.textotex;

public class friendListData {
    private int userID;
    private String userName;


    public friendListData(int userID, String userName) {
        this.userID = userID;
        this.userName = userName;
    }

    public int getConversationID() {
        return this.userID;
    }

    public String  getConversationUserName() {
        return this.userName;
    }

}
