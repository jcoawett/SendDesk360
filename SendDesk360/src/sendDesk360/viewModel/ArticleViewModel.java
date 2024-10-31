package sendDesk360.viewModel;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import sendDesk360.model.Article;
import sendDesk360.model.User;
import sendDesk360.model.database.ArticleManager;

import java.util.ArrayList;
import java.util.List;

public class ArticleViewModel {

    private final ArticleManager articleManager;
    private final User currentUser;

    // Properties for data binding with the UI
    private final LongProperty articleID = new SimpleLongProperty();
    private final LongProperty uniqueID = new SimpleLongProperty();
    private final StringProperty title = new SimpleStringProperty("");
    private final StringProperty shortDescription = new SimpleStringProperty("");
    private final StringProperty difficulty = new SimpleStringProperty("");
    private final StringProperty body = new SimpleStringProperty("");
    private final ListProperty<String> keywords = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ListProperty<String> referenceLinks = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ListProperty<Long> relatedArticleIDs = new SimpleListProperty<>(FXCollections.observableArrayList());

    // Constructor with dependencies
    public ArticleViewModel(ArticleManager articleManager, User currentUser) {
        this.articleManager = articleManager;
        this.currentUser = currentUser;
    }

    // Load article details into the view model properties
    public void loadArticle(Article article) {
        articleID.set(article.getArticleID());
        uniqueID.set(article.getUniqueID());
        title.set(article.getTitle());
        shortDescription.set(article.getShortDescription());
        difficulty.set(article.getDifficulty());
        body.set(article.getBody());

        // Ensure collections are initialized with non-null lists
        keywords.setAll(article.getKeywords() != null ? article.getKeywords() : new ArrayList<>());
        referenceLinks.setAll(article.getReferenceLinks() != null ? article.getReferenceLinks() : new ArrayList<>());
        relatedArticleIDs.setAll(article.getRelatedArticleIDs() != null ? article.getRelatedArticleIDs() : new ArrayList<>());

        // Debugging output to confirm article data
        System.out.println("Loaded Article Title: " + title.get());
        System.out.println("Loaded Article Body: " + body.get());
    }

    // Create a new article
    public void saveNewArticle() throws Exception {
        validateAdminAccess();
        Article newArticle = buildArticleFromProperties();
        articleManager.addArticle(newArticle);
        articleID.set(newArticle.getArticleID());  // Update ID after saving
    }

    // Update an existing article
    public void updateArticle() throws Exception {
        validateAdminAccess();
        Article updatedArticle = buildArticleFromProperties();
        updatedArticle.setArticleID(articleID.get());
        articleManager.updateArticle(updatedArticle);
    }

    // Delete the article, only if the user is an admin
    public void deleteArticle() throws Exception {
        validateAdminAccess();
        articleManager.deleteArticle(articleID.get());
    }

    // Check if the current user has admin privileges
    public boolean isAdmin() {
        return currentUser.getRoles().stream()
            .anyMatch(role -> role.getName().equalsIgnoreCase("admin"));
    }

    // Helper method to validate admin access before performing an action
    private void validateAdminAccess() throws IllegalAccessException {
        if (!isAdmin()) {
            throw new IllegalAccessException("Only admins can perform this action.");
        }
    }
    
    
    
    // RESET PROPERTIES
    public void resetProperties() {
        articleID.set(0L);
        uniqueID.set(0L);
        title.set("");
        shortDescription.set("");
        difficulty.set("");
        body.set("");
        
        // Clear each list property safely
        keywords.clear();
        referenceLinks.clear();
        relatedArticleIDs.clear();
    }

    // Helper method to build an Article object from the view model properties
    public Article buildArticleFromProperties() {
        Article article = new Article();
        article.setUniqueID(uniqueID.get());
        article.setTitle(title.get());
        article.setShortDescription(shortDescription.get());
        article.setDifficulty(difficulty.get());
        article.setBody(body.get());
        article.setKeywords(new ArrayList<>(keywords));
        article.setReferenceLinks(new ArrayList<>(referenceLinks));
        article.setRelatedArticleIDs(new ArrayList<>(relatedArticleIDs));
        return article;
    }

    
    // SETTERS
    public LongProperty articleIDProperty() { 
        articleID.addListener((obs, oldVal, newVal) -> System.out.println("Article ID updated: " + newVal));
        return articleID; 
    }

    public LongProperty uniqueIDProperty() { 
        uniqueID.addListener((obs, oldVal, newVal) -> System.out.println("Unique ID updated: " + newVal));
        return uniqueID; 
    }

    public StringProperty titleProperty() { 
        title.addListener((obs, oldVal, newVal) -> System.out.println("Title updated: " + newVal));
        return title; 
    }

    public StringProperty shortDescriptionProperty() { 
        shortDescription.addListener((obs, oldVal, newVal) -> System.out.println("Short Description updated: " + newVal));
        return shortDescription; 
    }

    public StringProperty difficultyProperty() { 
        difficulty.addListener((obs, oldVal, newVal) -> System.out.println("Difficulty updated: " + newVal));
        return difficulty; 
    }

    public StringProperty bodyProperty() { 
        body.addListener((obs, oldVal, newVal) -> System.out.println("Body updated: " + newVal));
        return body; 
    }

    public ListProperty<String> keywordsProperty() { 
        keywords.addListener((obs, oldVal, newVal) -> System.out.println("Keywords updated: " + newVal));
        return keywords; 
    }

    public ListProperty<String> referenceLinksProperty() { 
        referenceLinks.addListener((obs, oldVal, newVal) -> System.out.println("Reference Links updated: " + newVal));
        return referenceLinks; 
    }

    public ListProperty<Long> relatedArticleIDsProperty() { 
        relatedArticleIDs.addListener((obs, oldVal, newVal) -> System.out.println("Related Article IDs updated: " + newVal));
        return relatedArticleIDs; 
    }
    
    
}