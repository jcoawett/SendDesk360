package sendDesk360.viewModel;

import sendDesk360.model.Article;
import sendDesk360.model.User;
import sendDesk360.model.database.ArticleManager;

public class ArticleViewModel {

    private final Article article;
    private final User currentUser;
    private final ArticleManager articleManager;  // Injected dependency

    // Constructor with dependencies
    public ArticleViewModel(Article article, User currentUser, ArticleManager articleManager) {
        this.article = article;
        this.currentUser = currentUser;
        this.articleManager = articleManager;
    }

    // Get the title of the article
    public String getTitle() {
        return article.getTitle();
    }

    // Get the body of the article
    public String getBody() {
        return article.getBody();
    }

    // Get the article object itself
    public Article getArticle() {
        return article;
    }

    // Check if the current user has admin privileges
    public boolean isAdmin() {
        return currentUser.getRoles().stream()
            .anyMatch(role -> role.getName().equalsIgnoreCase("admin"));
    }

    // Delete the article, only if the user is an admin
    public void deleteArticle() throws Exception {
        if (isAdmin()) {
            articleManager.deleteArticle(article.getArticleID());  // Delete article via ArticleManager
        } else {
            throw new IllegalAccessException("Only admins can delete articles.");
        }
    }
}