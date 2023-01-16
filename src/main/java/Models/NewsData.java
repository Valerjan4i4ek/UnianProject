package Models;

public class NewsData {

    private int numberOfNews;
    private String article;
    private String link;

    public NewsData(int numberOfNews,  String article, String link) {
        this.numberOfNews = numberOfNews;
        this.article = article;
        this.link = link;
    }

    public NewsData(String article, String link) {
        this.article = article;
        this.link = link;
    }

    public int getNumberOfNews() {
        return numberOfNews;
    }

    public String getArticle() {
        return article;
    }

    public String getLink() {
        return link;
    }
}
