package textotex.textotex;


public class Message {
    private int persoId;
    private String message;
    private String name;
    private boolean isEncrypt;

    public Message(int userId, String message,String name, boolean isEncrypt )
    {
        this.persoId = userId;
        this.message = message;
        this.isEncrypt = isEncrypt;
        this.name = name;
    }
    public int getPersoId() {
        return persoId;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public boolean getisEncrypt() {
        return isEncrypt;
    }

}
