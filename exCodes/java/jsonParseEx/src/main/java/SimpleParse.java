import org.json.JSONObject;

public class SimpleParse {
    public static void main(String[] args) {
        String jsonString = "{\"title\": \"how to get stroage size\","
                + "\"url\": \"https://codechacha.com/ko/get-free-and-total-size-of-volumes-in-android/\","
                + "\"draft\": false,"
                + "\"star\": 10"
                + "}";
        JSONObject jsonObject = new JSONObject(jsonString);
        String title = jsonObject.getString("title");
        String url = jsonObject.getString("url");
        Boolean draft = jsonObject.getBoolean("draft");
        int star = jsonObject.getInt("star");

        System.out.println("title: " + title);
        System.out.println("url: " + url);
        System.out.println("draft: " + draft);
        System.out.println("star: " + star);
    }
}
