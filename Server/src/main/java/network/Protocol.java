package network;

public class Protocol {
    public static final int UNDEFINED = 0;


    // type
    public static final int TYPE_REQUEST = 1;
    public static final int TYPE_RESPONSE = 2;


    // code
    public static final int APART_RECOMMENDATION = 1;
    public static final int INDICES_GRAPH = 2;
    public static final int SIGN_UP = 3;
    public static final int USERINFO_UPDATE = 4;
    public static final int USERINFO_READ = 5;
    public static final int WITHDRAWAL = 6;
    public static final int LOGIN = 7;
    public static final int LOGOUT = 8;
    public static final int FAVORITES_CREATE = 9;
    public static final int FAVORITES_DELETE = 10;
    public static final int FAVORITES_READ = 11;


    public static final int SUCCESS = 1;
    public static final int FAIL = 2;


    // LENGTH
    public static final int LEN_TYPE = 1;
    public static final int LEN_CODE = 1;
    public static final int LEN_BODY_LENGTH = 4;
    public static final int LEN_HEADER = 6;
}
