import org.json.JSONObject;

public class JsonWithObjectParse {
    public static void main(String[] args) {
        String jsonString =
                "{"
                        +   "\"post1\": {"
                        +       "\"title\": \"how to get stroage size\","
                        +       "\"url\": \"https://codechacha.com/ko/get-free-and-total-size-of-volumes-in-android/\","
                        +       "\"draft\": false"
                        +"  },"
                        +   "\"post2\": {"
                        +       "\"title\": \"Android Q, Scoped Storage\","
                        +       "\"url\": \"https://codechacha.com/ko/android-q-scoped-storage/\","
                        +       "\"draft\": false"
                        +   "}"
                        +"}";
        System.out.println(jsonString);
        JSONObject jsonObject = new JSONObject(jsonString);

//    JSONObject p
    }

}
