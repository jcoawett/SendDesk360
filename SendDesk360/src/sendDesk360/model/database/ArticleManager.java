package sendDesk360.model.database;

import sendDesk360.model.Article;
import sendDesk360.model.encryption.EncryptionHelper;
import sendDesk360.model.encryption.EncryptionUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Manages the articles stored in the database and handles related operations,
 * such as adding, removing, updating, and retrieving articles. 
 * This class also supports encryption and decryption of sensitive article data.
 */
public class ArticleManager {
	/** The database connection used for executing SQL operations. */
    private Connection connection;

    /** Helper class used for encrypting and decrypting article data. */
    private EncryptionHelper encryptionHelper;

    /**
     * Constructs an ArticleManager with a database connection obtained from the provided DatabaseManager.
     *
     * @param dbManager the DatabaseManager instance used to obtain a database connection
     * @throws Exception if an error occurs while establishing the connection
     */
    public ArticleManager(DatabaseManager dbManager) throws Exception {
        this.connection = dbManager.getConnection();
        this.encryptionHelper = new EncryptionHelper();
    }

    /**
     * Adds a new article to the data base, encrypting the information 
     *
     * @param article the article object to be added to the database
     * @throws Exception if adding the article fails 
     */
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

    /**
     * Update an existing article while keeping it encrypted  
     *
     * @param article the article object to be updated
     * @throws Exception if updating the article fails 
     */
    public void updateArticle(Article article) throws Exception {
        String sql = "UPDATE Articles SET "
                   + "title = ?, title_iv = ?, "
                   + "shortDescription = ?, shortDescription_iv = ?, "
                   + "difficulty = ?, "
                   + "body = ?, body_iv = ? "
                   + "WHERE articleID = ?;";
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
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error adding article to database", e);
        }
    }

    /**
     * Map article from result set   
     *
     * @param rs the result set from an SQL query on the article table 
     * @throws SQLException if there is an error mapping the article from the ResultSet 
     */
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

    /**
     * Encryption helper method, encrypts plainText into a Base64 encrypted string   
     *
     * @param plainText the field to be encrypted 
     * @param  initialization vector (IV) used for encryption. 
     * @throws Exception if there is an error encrypting the data
     */
    private String encryptField(String plainText, byte[] iv) throws Exception {
        if (plainText == null) return null;
        byte[] encryptedBytes = encryptionHelper.encrypt(plainText.getBytes("UTF-8"), iv);
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Encryption helper method, encrypts plainText into a Base64 encrypted string   
     *
     * @param  encryptedText the field to be decrypted, currently in non-readable format
     * @param  The initialization vector (IV) in Base64 format, used during the encryption process. 
     * @throws Exception if there is an error dencrypting the data
     */
    private String decryptField(String encryptedText, String ivBase64) throws Exception {
        if (encryptedText == null || ivBase64 == null) return null;
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] iv = Base64.getDecoder().decode(ivBase64);
        byte[] decryptedBytes = encryptionHelper.decrypt(encryptedBytes, iv);
        return new String(decryptedBytes, "UTF-8");
    }

    /**
     * Deletes an article  
     *
     * @param  articleID  Article ID for the article to delete 
     * @throws SQLException if there is an error deleting the article from the SQLdatabase 
     */
    
    
    public void deleteArticle(long articleID) throws SQLException {
        // Delete keywords associated with the article
        String deleteKeywordsSql = "DELETE FROM Keywords WHERE articleID = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteKeywordsSql)) {
            pstmt.setLong(1, articleID);
            pstmt.executeUpdate();
        }

        // Delete references associated with the article
        String deleteReferencesSql = "DELETE FROM References WHERE articleID = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteReferencesSql)) {
            pstmt.setLong(1, articleID);
            pstmt.executeUpdate();
        }

        // Delete any related articles referencing this article
        String deleteRelatedSql = "DELETE FROM RelatedArticles WHERE articleID = ? OR relatedArticleID = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteRelatedSql)) {
            pstmt.setLong(1, articleID);
            pstmt.setLong(2, articleID);
            pstmt.executeUpdate();
        }

        // Finally, delete the article itself
        String deleteArticleSql = "DELETE FROM Articles WHERE articleID = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteArticleSql)) {
            pstmt.setLong(1, articleID);
            pstmt.executeUpdate();
        }
    }

    /**
     * Add keywords for an article   
     *
     * @param  article article in which to add keywords 
     * @throws SQLException if there is an error adding keywords into the keywords table  
     */
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

    /**
     * Add references for an article    
     *
     * @param  article article in which to add references
     * @throws SQLException if there is an error adding references into the references table  
     */
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

    /**
     * Add Related Articles section for an article   
     *
     * @param  article article in which to add related articles
     * @throws SQLException if there is an error adding articles into the RelatedArticles table
     */
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

    /**
     * Search articles from the Article table using keywords  
     *
     * @param  searchTerm the keyword to be used in finding the article from the article table 
     * @throws Exception if there is an error searching the db 
     * @throws SQLException if there is an error writing and executing the search on the SQL database 
     */
    public List<Article> searchArticles(String searchTerm) throws Exception {
        String sql = "SELECT * FROM Articles;";
        List<Article> articles = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
                ResultSet resultSet = pstmt.executeQuery()) {
               while (resultSet.next()) {
                   Article article = mapArticle(resultSet);
                   if (article.getTitle().contains(searchTerm) || article.getKeywords().contains(searchTerm)) {
                       articles.add(article);
                   }
               }
           } catch (SQLException e) {
               e.printStackTrace();
               throw new Exception("Error searching articles", e);
           }
        return articles;
    }
    
    

    /**
     * Get the articles by difficulty  
     *
     * @param  difficulty the difficulty in which we require articles from 
     * @throws SQLException if there is an error writing and executing the search on the SQL database using the difficulty 
     */
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

    /**
     * Removes articles with the specified difficulty.
     *
     * @param difficulty the difficulty level of articles to remove
     * @throws SQLException if an SQL error occurs during the removal process
     */
    public void removeArticlesByDifficulty(String difficulty) throws SQLException {
        String sql = "DELETE FROM Articles WHERE difficulty = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, difficulty);
            pstmt.executeUpdate();
        }
    }

    /**
     * Backs up articles to a specified file.
     *
     * @param fileName the name of the file to which articles should be backed up
     */
    public void backupArticles(String fileName) {
        // Implement backup logic (e.g., serialize articles to JSON file)
    }

    /**
     * Restores articles from a backup file.
     *
     * @param fileName       the name of the backup file
     * @param removeExisting whether to remove existing articles before restoring
     */
    public void restoreArticles(String fileName, boolean removeExisting) {
        // Implement restore logic, ensuring no duplicates based on uniqueID
    }

    /**
     * Fetches an article by its unique ID.
     *
     * @param articleID the ID of the article to fetch
     * @return the fetched Article object, or null if not found
     * @throws Exception if an error occurs during fetching
     */
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

    /**
     * Fetches keywords associated with an article by its ID.
     *
     * @param articleID the ID of the article
     * @return a list of keywords associated with the article
     * @throws SQLException if an SQL error occurs during fetching
     */
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

    /**
     * Fetches references associated with an article by its ID.
     *
     * @param articleID the ID of the article
     * @return a list of references associated with the article
     * @throws SQLException if an SQL error occurs during fetching
     */
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

    /**
     * Fetches related articles for an article by its ID.
     *
     * @param articleID the ID of the article
     * @return a list of IDs of related articles
     * @throws SQLException if an SQL error occurs during fetching
     */
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
    
    
    /**
     * Fetches all articles from the database.
     *
     * @return a list of all articles
     * @throws Exception if an error occurs during fetching
     */
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
    
    /**
     * Updates the keywords for a specified article.
     *
     * @param article the article for which to update keywords
     * @throws SQLException if an SQL error occurs during the update
     */
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
    
    /**
     * Updates the references for a specified article.
     *
     * @param article the article for which to update references
     * @throws SQLException if an SQL error occurs during the update
     */
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

    /**
     * Updates the related articles for a specified article.
     *
     * @param article the article for which to update related articles
     * @throws SQLException if an SQL error occurs during the update
     */
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
    
    public void restoreArticles(File backupFile) {
    	String insertSQL = "INSERT INTO Articles (uniqueID, title, title_iv, shortDescription, shortDescription_iv, difficulty, body, body_iv) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        try (BufferedReader reader = new BufferedReader(new FileReader(backupFile))) {
            String line;
            try (PreparedStatement pstmt = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
                while ((line = reader.readLine()) != null) {
                	
                    String[] articleData = line.split(","); 
                    if (articleData.length < 6) { // Ensure there are enough fields for the article
                        System.out.println("Skipping invalid article data: " + line);
                        continue;
                    }

                    // Create Article object and set values
                    Article article = new Article();
                    article.setUniqueID(Long.parseLong(articleData[0].trim()));
                    article.setTitle(articleData[1].trim());
                    article.setShortDescription(articleData[2].trim());
                    article.setDifficulty(articleData[3].trim());
                    article.setBody(articleData[4].trim());

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

                    // Set parameters for the SQL statement
                    pstmt.setLong(1, article.getUniqueID());
                    pstmt.setString(2, encryptedTitle);
                    pstmt.setString(3, titleIVBase64);
                    pstmt.setString(4, encryptedShortDesc);
                    pstmt.setString(5, shortDescIVBase64);
                    pstmt.setString(6, article.getDifficulty());
                    pstmt.setString(7, encryptedBody);
                    pstmt.setString(8, bodyIVBase64);

                    // Execute the insert
                    pstmt.executeUpdate();

                    // Get the generated keys if necessary
                    ResultSet keys = pstmt.getGeneratedKeys();
                    if (keys.next()) {
                        long articleID = keys.getLong(1);
                        article.setArticleID(articleID);
                        
                        addKeywords(article);
                        addReferences(article);
                        addRelatedArticles(article);
                    }
                }
                System.out.println("Articles restored successfully from " + backupFile.getAbsolutePath());
            }
        } catch (Exception e ) {
            System.out.println("Error reading the backup file: " + e.getMessage());
        } 
    }
    
    public void backupArticles(File restoreFile) {
    	try {
            List<Article> articles = getAllArticles(); // Fetch all articles

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(restoreFile))) {
                for (Article article : articles) {
                    StringBuilder articleData = new StringBuilder();
                    articleData.append(article.getUniqueID()).append(",")
                               .append(article.getTitle()).append(",")
                               .append(article.getShortDescription()).append(",")
                               .append(article.getDifficulty()).append(",")
                               .append(article.getBody());
                    
                    writer.write(articleData.toString());
                    writer.newLine(); // Write a new line after each article
                }
                System.out.println("Articles backed up successfully to " + restoreFile.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("Error writing to the backup file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error fetching articles for backup: " + e.getMessage());
        }
    }
}