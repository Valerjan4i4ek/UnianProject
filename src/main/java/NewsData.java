public class NewsData {
    private String urlName;
    private String articleName;

    public NewsData(String urlName, String articleName) {
        this.urlName = urlName;
        this.articleName = articleName;
    }

    public String getUrlName() {
        return urlName;
    }

    public String getArticleName() {
        return articleName;
    }
}
