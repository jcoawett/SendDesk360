package sendDesk360.model;

import java.util.List;

public class Article {
    private long articleID;
    private long uniqueID;
    private String title;
    private String shortDescription;
    private String difficulty;
    private boolean encrypted; 
    private String body;
    private List<String> keywords;
    private List<String> referenceLinks;
    private List<Long> relatedArticleIDs;

    // Constructors
    public Article() {}

    public Article(long uniqueID, String title, String shortDescription, String difficulty, String body, boolean encrypted) {
        this.uniqueID = uniqueID;
        this.title = title;
        this.shortDescription = shortDescription;
        this.difficulty = difficulty;
        this.body = body;
        this.encrypted = encrypted;
    }

    // Getters and setters
    public long getArticleID() {
        return articleID;
    }

    public void setArticleID(long articleID) {
        this.articleID = articleID;
    }

    public long getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(long uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    
    public boolean getEncrypted() {
    	return this.encrypted;
    }
    
    public void setEncrypted(boolean encrypted) {
    	this.encrypted = encrypted;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    
    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<String> getReferenceLinks() {
        return referenceLinks;
    }

    public void setReferenceLinks(List<String> referenceLinks) {
        this.referenceLinks = referenceLinks;
    }

    public List<Long> getRelatedArticleIDs() {
        return relatedArticleIDs;
    }

    public void setRelatedArticleIDs(List<Long> relatedArticleIDs) {
        this.relatedArticleIDs = relatedArticleIDs;
    }
}