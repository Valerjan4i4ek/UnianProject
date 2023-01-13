import javafx.animation.ParallelTransition;
import javafx.scene.shape.PathElement;

import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsingClass {
    public static final String UNIAN_LINK = "https://www.unian.ua/tag/viyna-v-ukrajini";
    private final static String USER_AGENT = "Chrome/104.0.0.0";
    static Scanner scanner = new Scanner(System.in);

    static NewsDataCache newsDataCache = new NewsDataCache();
    static TextArticleCache textArticleCache = new TextArticleCache();

    public static void main(String [] args) throws IOException {
        Map<Integer, NewsData> newsDataMap = parsingNews(UNIAN_LINK);
        List<PagesData> pagesDataList = parsingPages(UNIAN_LINK);
        boolean b = false;
        while (!b){
            b = start(newsDataMap, pagesDataList);
        }
//        start(newsDataMap, pagesDataList);
    }

    public static boolean start(Map<Integer, NewsData> newsDataMap, List<PagesData> pagesDataList) throws IOException {

        System.out.println("News or Pages : 1 or 2");
        int newsOrPages = scanner.nextInt();
        int newsNumber;
        int pagesNumber;
        boolean b;
        if(newsOrPages == 1){
            System.out.println("Choose number of news");
            newsNumber = scanner.nextInt();
            if(newsDataMap.containsKey(newsNumber)){
                NewsData newsData = newsDataMap.get(newsNumber);
                if(getNewsDataCache(newsData.getLink()) != null){
                    if(getTextArticleCache(newsData.getLink()) != null){
                        System.out.println(parsingText(Objects.requireNonNull(getTextArticleCache(newsData.getLink())).getLink()));
                        parsingText(Objects.requireNonNull(getTextArticleCache(newsData.getLink())).getLink());
                        b = reStart();
                        return b;
//                        start(parsingNews(UNIAN_LINK), parsingPages(UNIAN_LINK));
                    }
                    else{
                        String s = parsingText(newsData.getLink());
                        System.out.println(s);
                        addTextArticleCache(newsData.getArticle(), newsData.getLink(), s);
                        parsingText(Objects.requireNonNull(getNewsDataCache(newsData.getLink())).getLink());
                        b = reStart();
                        return b;
//                        start(parsingNews(UNIAN_LINK), parsingPages(UNIAN_LINK));
                    }
                }
                else{
                    addNewsDataCache(newsData.getArticle(), newsData.getLink());
                    if(getTextArticleCache(newsData.getLink()) != null){
                        System.out.println(parsingText(Objects.requireNonNull(getTextArticleCache(newsData.getLink())).getLink()));
                        parsingText(Objects.requireNonNull(getTextArticleCache(newsData.getLink())).getLink());
                        b = reStart();
                        return b;
//                        start(parsingNews(UNIAN_LINK), parsingPages(UNIAN_LINK));
                    }
                    else{
                        String s = parsingText(newsData.getLink());
                        System.out.println(s);
                        addTextArticleCache(newsData.getArticle(), newsData.getLink(), s);
                        parsingText(newsData.getLink());
                        b = reStart();
                        return b;
//                        start(parsingNews(UNIAN_LINK), parsingPages(UNIAN_LINK));
                    }
                }
            }
        }
        else if(newsOrPages == 2){
            System.out.println("Choose number of page");
            pagesNumber = scanner.nextInt();
            for(PagesData pagesData : pagesDataList){
                if(pagesData.getPageNumber() == pagesNumber){
                    start(parsingNews(pagesData.getLink()), parsingPages(pagesData.getLink()));

                }
            }

        }
        else{
            System.out.println("incorrect");
        }
        return false;
    }

    public static boolean reStart(){
        System.out.println("Restart? Y/N");
        String s = scanner.next();
        if(s.equalsIgnoreCase("Y")){
            return false;
        }
        else if(s.equalsIgnoreCase("N")){
            return true;
        }
        else{
            System.out.println("incorrect");
            return false;
        }
    }

    public static void addTextArticleCache(String article, String link, String articleText){
        textArticleCache.addTextArticleCache(article, link, articleText);
    }

    public static TextArticle getTextArticleCache(String link){
        if(textArticleCache.getTextArticleCache(link) != null){
            return textArticleCache.getTextArticleCache(link);
        }
        return null;
    }

    public static void addNewsDataCache(String article, String link){
        newsDataCache.addNewsDataCache(article, link);
    }

    public static NewsData getNewsDataCache(String link){
        if(newsDataCache.getNewsDataCache(link) != null){
            return newsDataCache.getNewsDataCache(link);
        }
        return null;
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

    public static String parsingText(String link) throws IOException{
        String url = getURLData(link);
        String regex = "<p>(.+?)</p>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        String result = "";
        StringBuffer sb = new StringBuffer();

        while (matcher.find()){
            result = matcher.group(1);
            if(!result.contains("Правила користування сайтом")){
                String[] strArray = result.split("<.+?>");
                for(String s : strArray){
                    sb.append(s);
                }
                sb.append("\n");
            }
        }

//        Pattern pattern1 = Pattern.compile();
//        Matcher matcher1 = pattern1.matcher(sb);

        return sb.toString();
    }

    public static Map<Integer, NewsData> parsingNews(String lInk) throws IOException{
        Map<Integer, NewsData> map = new ConcurrentHashMap<>();
        String url = getURLData(lInk);
//        String regex = "<div class=\"list-thumbs__time time\">(.+?)</div></div></div><div class=\"list-thumbs__item\"><a href=\"(.+?)\".+?alt=\"(.+?)\" src";
        String regex = "<h3><a href=\"(.+?)\" class=\"list-thumbs__title\">(.+?)</a>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        String link = "";
        String article = "";
        int i = 1;
        while (matcher.find()){
            link = matcher.group(1);
            article = matcher.group(2);
            System.out.println(i + " " + article);
            System.out.println(link);
            NewsData newsData = new NewsData(i, article, link);
            map.put(i, newsData);
            i++;
        }
        return map;
    }

    public static List<PagesData> parsingPages(String link) throws IOException{
        List<PagesData> list = new ArrayList<>();
        String url = getURLData(link);
        String regex = "(/tag/viyna-v-ukrajini\\?page=(.+?)\")";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        String pageNumber;
        String urlName = "";
        int i = 0;
        while (matcher.find() && i < 6){
            pageNumber = matcher.group(2);
            urlName = "https://www.unian.ua" + matcher.group(1);
            StringBuilder sb = new StringBuilder(urlName);
            sb.deleteCharAt(sb.length()-1);
            System.out.println(pageNumber + " " + sb);
            PagesData pagesData = new PagesData(Integer.parseInt(pageNumber), sb.toString());
            list.add(pagesData);
            i++;
        }
        return list;
    }
}
