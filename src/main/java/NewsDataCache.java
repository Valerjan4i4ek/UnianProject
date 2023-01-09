import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NewsDataCache {
    Map<String, NewsData> newsDataCacheMap = new ConcurrentHashMap<>();

    public Map<String, NewsData> addNewsDataCache(String article, String link){
        newsDataCacheMap.put(link, new NewsData(article, link));
        return newsDataCacheMap;
    }

    public NewsData getNewsDataCache(String link){
        return newsDataCacheMap.get(link);
    }
}
