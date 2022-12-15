import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsingClass {
    public static final String UNIAN_LINK = "https://www.unian.ua/tag/viyna-v-ukrajini";
    private final static String USER_AGENT = "Chrome/104.0.0.0";

    public static void main(String [] args) throws IOException {
//        System.out.println(getURLData(UNIAN_LINK));
        List<NewsData> list = letsFind(getURLData(UNIAN_LINK));
        for(NewsData newsData : list){
            System.out.println(newsData.getUrlName());
            System.out.println(newsData.getArticleName());
        }
    }

    public static String getURLData(String link) throws IOException{
        URL urlObject = new URL(link);
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = connection.getResponseCode();
        if (responseCode == 404) {
            throw new IllegalArgumentException();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    public static List<NewsData> letsFind(String url){

//        Pattern pattern = Pattern.compile("news_.+?<");
//        Pattern pattern = Pattern.compile("(?:news)(\\w+)");
        List<NewsData> list = new ArrayList<>();
        String regex = "<div class=\"list-thumbs__item\"><a href=\"(.*?)\".+alt=\"(.*?)src";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
//        String s = "";
        String result = "";
        while(matcher.find()){
//            s = matcher.group();
            String urlName = matcher.group(1);
            String articleName = matcher.group(2);
            NewsData newsData = new NewsData(urlName, articleName);
            list.add(newsData);

//            result = urlName + " " + articleName;
//            System.out.println(urlName + " " + articleName);

        }
        return list;
    }
}
