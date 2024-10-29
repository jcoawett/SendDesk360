package sendDesk360.model.database;

import sendDesk360.model.Article;
import sendDesk360.model.encryption.EncryptionHelper;
import sendDesk360.model.encryption.EncryptionUtils;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ArticleManager {
    private Connection connection;
    private EncryptionHelper encryptionHelper;

    public ArticleManager(DatabaseManager dbManager) throws Exception {
        this.connection = dbManager.getConnection();
        this.encryptionHelper = new EncryptionHelper();
    }

    // Add a new article with encryption
    public void addArticle(Article article) throws Exception {
        String sql = "INSERT INTO Articles (uniqueID, title, title_iv, shortDescription, shortDescription_iv, difficulty, body, body_iv) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, article.getUniqueID());

            // Encrypt fields and generate random IVs
            byte[] titleIV = EncryptionUtils.generateRandomIV();
            String encryptedTitle = encryptField(article.getTitle(), titleIV);
            String titleIVBase64 = Base64.getEncoder().encodeToString(titleIV);

            byte[] shortDescIV = EncryptionUtils.generateRandomIV();
            String encryptedShortDesc = encryptField(article.getShortDescription(), shortDescIV);
            String shortDescIVBase64 = Base64.getEncoder().encodeToString(shortDescIV);

            byte[] bodyIV = EncryptionUtils.generateRandomIV();
            String encryptedBody = encryptField(article.getBody(), bodyIV);
            String bodyIVBase64 = Base64.getEncoder().encodeToString(bodyIV);

            pstmt.setString(2, encryptedTitle);
            pstmt.setString(3, titleIVBase64);
            pstmt.setString(4, encryptedShortDesc);
            pstmt.setString(5, shortDescIVBase64);
            pstmt.setString(6, article.getDifficulty());
            pstmt.setString(7, encryptedBody);
            pstmt.setString(8, bodyIVBase64);

            pstmt.executeUpdate();

            ResultSet keys = pstmt.getGeneratedKeys();
            if (keys.next()) {
                long articleID = keys.getLong(1);
                article.setArticleID(articleID);
                addKeywords(article);
                addReferences(article);
                addRelatedArticles(article);
            }
        }
    }

    // Update an existing article with encryption
    public void updateArticle(Article article) throws Exception {
        String sql = "UPDATE Articles SET "
                   + "title = ?, title_iv = ?, "
                   + "shortDescription = ?, shortDescription_iv = ?, "
                   + "difficulty = ?, "
                   + "body = ?, body_iv = ? "
                   + "WHERE articleID = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Generate new IVs for each field
            byte[] titleIV = EncryptionUtils.generateRandomIV();
            String encryptedTitle = encryptField(article.getTitle(), titleIV);
            String titleIVBase64 = Base64.getEncoder().encodeToString(titleIV);

            byte[] shortDescIV = EncryptionUtils.generateRandomIV();
            String encryptedShortDesc = encryptField(article.getShortDescription(), shortDescIV);
            String shortDescIVBase64 = Base64.getEncoder().encodeToString(shortDescIV);

            byte[] bodyIV = EncryptionUtils.generateRandomIV();
            String encryptedBody = encryptField(article.getBody(), bodyIV);
            String bodyIVBase64 = Base64.getEncoder().encodeToString(bodyIV);

            // Set parameters in the PreparedStatement
            pstmt.setString(1, encryptedTitle);
            pstmt.setString(2, titleIVBase64);
            pstmt.setString(3, encryptedShortDesc);
            pstmt.setString(4, shortDescIVBase64);
            pstmt.setString(5, article.getDifficulty()); // Assuming difficulty is not encrypted
            pstmt.setString(6, encryptedBody);
            pstmt.setString(7, bodyIVBase64);
            pstmt.setLong(8, article.getArticleID());
            pstmt.executeUpdate();

            // Update keywords, references, related articles as needed
            // For example:
            updateKeywords(article);
            updateReferences(article);
            updateRelatedArticles(article);

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // Map article from ResultSet
    private Article mapArticle(ResultSet rs) throws SQLException {
        Article article = new Article();
        try {
            article.setArticleID(rs.getLong("articleID"));
            article.setUniqueID(rs.getLong("uniqueID"));

            // Get encrypted fields and IVs
            String encryptedTitle = rs.getString("title");
            String titleIVBase64 = rs.getString("title_iv");

            String encryptedShortDesc = rs.getString("shortDescription");
            String shortDescIVBase64 = rs.getString("shortDescription_iv");

            String encryptedBody = rs.getString("body");
            String bodyIVBase64 = rs.getString("body_iv");

            // Decrypt fields
            String decryptedTitle = decryptField(encryptedTitle, titleIVBase64);
            String decryptedShortDesc = decryptField(encryptedShortDesc, shortDescIVBase64);
            String decryptedBody = decryptField(encryptedBody, bodyIVBase64);

            article.setTitle(decryptedTitle);
            article.setShortDescription(decryptedShortDesc);
            article.setDifficulty(rs.getString("difficulty")); // Not encrypted
            article.setBody(decryptedBody);

            // Retrieve keywords, references, related articles
            article.setKeywords(getKeywordsForArticle(article.getArticleID()));
            article.setReferenceLinks(getReferencesForArticle(article.getArticleID()));
            article.setRelatedArticleIDs(getRelatedArticlesForArticle(article.getArticleID()));

        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Error mapping article from ResultSet", e);
        }
        return article;
    }

    // Encryption helper methods
    private String encryptField(String plainText, byte[] iv) throws Exception {
        if (plainText == null) return null;
        byte[] encryptedBytes = encryptionHelper.encrypt(plainText.getBytes("UTF-8"), iv);
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    private String decryptField(String encryptedText, String ivBase64) throws Exception {
        if (encryptedText == null || ivBase64 == null) return null;
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] iv = Base64.getDecoder().decode(ivBase64);
        byte[] decryptedBytes = encryptionHelper.decrypt(encryptedBytes, iv);
        return new String(decryptedBytes, "UTF-8");
    }

    // Delete an article
    public void deleteArticle(long articleID) throws SQLException {
        String sql = "DELETE FROM Articles WHERE articleID = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, articleID);
            pstmt.executeUpdate();
        }
    }

    // Add keywords for an article
    private void addKeywords(Article article) throws SQLException {
        String sql = "INSERT INTO Keywords (articleID, keyword) VALUES (?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (String keyword : article.getKeywords()) {
                pstmt.setLong(1, article.getArticleID());
                pstmt.setString(2, keyword);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    // Add references for an article
    private void addReferences(Article article) throws SQLException {
        String sql = "INSERT INTO References (articleID, referenceLink) VALUES (?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (String link : article.getReferenceLinks()) {
                pstmt.setLong(1, article.getArticleID());
                pstmt.setString(2, link);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    // Add related articles
    private void addRelatedArticles(Article article) throws SQLException {
        String sql = "INSERT INTO RelatedArticles (articleID, relatedArticleID) VALUES (?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (Long relatedID : article.getRelatedArticleIDs()) {
                pstmt.setLong(1, article.getArticleID());
                pstmt.setLong(2, relatedID);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    // Search articles by keyword or title
    public List<Article> searchArticles(String searchTerm) throws Exception {
        String sql = "SELECT * FROM Articles;";
        List<Article> articles = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = pstmt.executeQuery()) {
                while (resultSet.next()) {
                    Article article = mapArticle(resultSet);
                    if (article.getTitle().contains(searchTerm) ||
                        article.getKeywords().contains(searchTerm)) {
                        articles.add(article);
                    }
                }
            }
        }
        return articles;
    }
    
    

    // Get articles by difficulty
    public List<Article> getArticlesByDifficulty(String difficulty) throws SQLException {
        String sql = "SELECT * FROM Articles WHERE difficulty = ?;";
        List<Article> articles = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, difficulty);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                while (resultSet.next()) {
                    articles.add(mapArticle(resultSet));
                }
            }
        }
        return articles;
    }

    // Remove articles by difficulty
    public void removeArticlesByDifficulty(String difficulty) throws SQLException {
        String sql = "DELETE FROM Articles WHERE difficulty = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, difficulty);
            pstmt.executeUpdate();
        }
    }

    // Backup articles to file
    public void backupArticles(String fileName) {
        // Implement backup logic (e.g., serialize articles to JSON file)
    }

    // Restore articles from backup
    public void restoreArticles(String fileName, boolean removeExisting) {
        // Implement restore logic, ensuring no duplicates based on uniqueID
    }

    // Fetch article by ID
    public Article getArticleByID(long articleID) throws Exception {
        String sql = "SELECT * FROM Articles WHERE articleID = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, articleID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapArticle(rs);
                } else {
                    return null; // Article not found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error fetching article by ID", e);
        }
    }

    // Fetch keywords for an article
    private List<String> getKeywordsForArticle(long articleID) throws SQLException {
        String sql = "SELECT keyword FROM Keywords WHERE articleID = ?;";
        List<String> keywords = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, articleID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    keywords.add(rs.getString("keyword"));
                }
            }
        }
        return keywords;
    }

    // Fetch references for an article
    private List<String> getReferencesForArticle(long articleID) throws SQLException {
        String sql = "SELECT referenceLink FROM References WHERE articleID = ?;";
        List<String> references = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, articleID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    references.add(rs.getString("referenceLink"));
                }
            }
        }
        return references;
    }

    // Fetch related articles for an article
    private List<Long> getRelatedArticlesForArticle(long articleID) throws SQLException {
        String sql = "SELECT relatedArticleID FROM RelatedArticles WHERE articleID = ?;";
        List<Long> relatedArticles = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, articleID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    relatedArticles.add(rs.getLong("relatedArticleID"));
                }
            }
        }
        return relatedArticles;
    }
    
    
    // Fetch all articles regardless
    public List<Article> getAllArticles() throws Exception {
        String sql = "SELECT * FROM Articles;";
        List<Article> articles = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Article article = mapArticle(rs);
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error fetching all articles", e);
        }
        return articles;
    }
    
    
    private void updateKeywords(Article article) throws SQLException {
        // Delete existing keywords
        String deleteSql = "DELETE FROM Keywords WHERE articleID = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSql)) {
            pstmt.setLong(1, article.getArticleID());
            pstmt.executeUpdate();
        }

        // Add new keywords
        addKeywords(article);
    }

    private void updateReferences(Article article) throws SQLException {
        // Delete existing references
        String deleteSql = "DELETE FROM References WHERE articleID = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSql)) {
            pstmt.setLong(1, article.getArticleID());
            pstmt.executeUpdate();
        }

        // Add new references
        addReferences(article);
    }

    private void updateRelatedArticles(Article article) throws SQLException {
        // Delete existing related articles
        String deleteSql = "DELETE FROM RelatedArticles WHERE articleID = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSql)) {
            pstmt.setLong(1, article.getArticleID());
            pstmt.executeUpdate();
        }

        // Add new related articles
        addRelatedArticles(article);
    }
}