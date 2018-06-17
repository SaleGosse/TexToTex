package textotex.textotex;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Random;

public class SecureClass extends SQLiteOpenHelper {

    private SQLiteDatabase database;
    private static SharedPreferences mSharedPref;
    private final int mUserID;
    public static final String  DATABASE_NAME = "hashDB";
    public static final int     DATABASE_VERSION = 1;


    public SecureClass(Context context, SharedPreferences sharedPref) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        this.mSharedPref = sharedPref;
        this.mUserID = this.mSharedPref.getInt(context.getString(R.string.user_id_key), -1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String rq1 = "CREATE TABLE IF NOT EXISTS PublicKeys(idPubKey INTEGER PRIMARY KEY AUTOINCREMENT, idUser INTEGER NOT NULL, pubExp TEXT, modulus TEXT);";
        final String rq2 = "CREATE TABLE IF NOT EXISTS AESKeys (idAESKey INTEGER PRIMARY KEY AUTOINCREMENT, idConversation INTEGER NOT NULL, privExp TEXT, aesKey TEXT NOT NULL);";

        db.execSQL(rq1);
        db.execSQL(rq2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String rq1 = "DROP TABLE IF EXISTS PublicKeys;";
        String rq2 = "DROP TABLE IF EXISTS AESKeys;";

        database.execSQL(rq1);
        database.execSQL(rq2);

        onCreate(db);
    }

    public KeyPair newRSAKey(int conversationID) {
        KeyPair kp = new KeyPair(null, null);

        try {
            final KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            kp = generator.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }

        int rand = new Random().nextInt();

        this.insertKeys(conversationID, kp, Integer.toString(rand));

        return kp;
    }

    public void insertKeys(int conversationID, KeyPair kp, String aeskey)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        //The public idUser/exponent/modulus
        ContentValues val = new ContentValues();

        val.put("idUser", this.mUserID);
        val.put("pubExp", ((RSAPublicKey)kp.getPublic()).getPublicExponent().toString());
        val.put("modulus", ((RSAPublicKey)kp.getPublic()).getModulus().toString());

        db.insert("PublicKeys", null, val);

        //The private idConversation/exponent/AESKey

        val = new ContentValues();

        val.put("idConversation", conversationID);
        val.put("privExp", ((RSAPrivateKey)kp.getPrivate()).getModulus().toString());
        val.put("aesKey", getHash(Integer.toString(new Random().nextInt())));

        db.insert("AESKeys", null, val);
    }

    public String getAESKey(int conversationID)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT aesKey FROM AESKeys WHERE idConversation = " + Integer.toString(conversationID), null);

        cursor.moveToFirst();

        return cursor.getString(0);
    }

    public String getHash(String str)
    {
        MessageDigest digest = null;

        try
        {
            digest = MessageDigest.getInstance("SHA-256");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "error";
        }

        digest.reset();
        return digest.digest(str.getBytes()).toString();
    }

}


//[P#1:AndroidOpenSSL][S#2:Cipher][A#1:AES/CBC/NoPadding]
//[P#1:AndroidOpenSSL][S#2:Cipher][A#2:AES/CBC/PKCS5Padding]
//[P#1:AndroidOpenSSL][S#2:Cipher][A#3:AES/CBC/PKCS7Padding]

//[P#1:AndroidOpenSSL][S#5:KeyPairGenerator][A#6:RSA]

//[P#3:BC][S#11:KeyPairGenerator][A#1:1.2.840.10040.4.1]
//[P#3:BC][S#11:KeyPairGenerator][A#2:1.2.840.10045.2.1]
//[P#3:BC][S#11:KeyPairGenerator][A#3:1.2.840.113549.1.1.1]
//[P#3:BC][S#11:KeyPairGenerator][A#4:1.2.840.113549.1.1.7]
//[P#3:BC][S#11:KeyPairGenerator][A#5:1.2.840.113549.1.3.1]
//[P#3:BC][S#11:KeyPairGenerator][A#6:1.3.133.16.840.63.0.2]
//[P#3:BC][S#11:KeyPairGenerator][A#7:1.3.14.3.2.27]
//[P#3:BC][S#11:KeyPairGenerator][A#8:2.5.8.1.1]
//[P#3:BC][S#11:KeyPairGenerator][A#9:DH]
//[P#3:BC][S#11:KeyPairGenerator][A#10:DIFFIEHELLMAN]
//[P#3:BC][S#11:KeyPairGenerator][A#11:DSA]
//[P#3:BC][S#11:KeyPairGenerator][A#12:EC]
//[P#3:BC][S#11:KeyPairGenerator][A#13:RSA]

