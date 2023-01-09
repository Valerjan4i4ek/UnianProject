import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TextArticleCache {
    Map<String, TextArticle> textArticleCacheMap = new ConcurrentHashMap<>();

    public Map<String, TextArticle> addTextArticleCache(String article, String link, String articleText){
        textArticleCacheMap.put(link, new TextArticle(article, link, articleText));
        return textArticleCacheMap;
    }

    public TextArticle getTextArticleCache(String link){
        return textArticleCacheMap.get(link);
    }
}
