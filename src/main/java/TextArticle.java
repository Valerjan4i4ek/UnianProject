public class TextArticle {
    private String article;
    private String link;
    private String articleText;

    public TextArticle(String article, String link, String articleText) {
        this.article = article;
        this.link = link;
        this.articleText = articleText;
    }

    public String getArticle() {
        return article;
    }

    public String getLink() {
        return link;
    }

    public String getArticleText() {
        return articleText;
    }
}
