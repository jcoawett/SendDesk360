package sendDesk360.model.database;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.*;

public class DeleteTest {
    private Connection connection;
    private ArticleManager articleManager;

    @Before
    public void setUp() throws Exception {
        // Create an in-memory H2 database for testing
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        
        // Create necessary tables
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE Articles (" +
                "articleID BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "uniqueID BIGINT UNIQUE NOT NULL, " +
                "title VARCHAR(1024) NOT NULL, " +
                "shortDescription VARCHAR(2048), " +
                "difficulty VARCHAR(20) NOT NULL, " +
                "body TEXT NOT NULL, " +
                "ISENCRYPTED BOOLEAN NOT NULL DEFAULT FALSE)");
            
            stmt.execute("CREATE TABLE Keywords (" +
                "articleID BIGINT NOT NULL, " +
                "keyword VARCHAR(255), " +
                "FOREIGN KEY (articleID) REFERENCES Articles(articleID) ON DELETE CASCADE)");
            
            stmt.execute("CREATE TABLE References (" +
                "articleID BIGINT NOT NULL, " +
                "referenceLink VARCHAR(500), " +
                "FOREIGN KEY (articleID) REFERENCES Articles(articleID) ON DELETE CASCADE)");
            
            stmt.execute("CREATE TABLE RelatedArticles (" +
                "articleID BIGINT NOT NULL," +
                "relatedArticleID BIGINT NOT NULL," +
                "FOREIGN KEY (articleID) REFERENCES Articles(articleID) ON DELETE CASCADE)");
        }

        // Mock a simple DatabaseManager for the test
        DatabaseManager mockDbManager = new DatabaseManager() {
            public Connection getConnection() {
                return connection;
            }
        };

        articleManager = new ArticleManager(mockDbManager);
    }

    @Test
    public void testDeleteArticle() throws Exception {
        // Insert a test article
        long uniqueId = System.currentTimeMillis();
        long articleId = insertTestArticle(uniqueId);

        // Insert test data for related tables
        insertTestKeywords(articleId);
        insertTestReferences(articleId);
        insertTestRelatedArticles(articleId);

        // Verify data exists before deletion
        assertTrue("Article should exist before deletion", checkArticleExists(articleId));
        assertTrue("Keywords should exist before deletion", checkKeywordsExist(articleId));
        assertTrue("References should exist before deletion", checkReferencesExist(articleId));
        assertTrue("Related articles should exist before deletion", checkRelatedArticlesExist(articleId));

        // Delete the article
        articleManager.deleteArticle(articleId);

        // Verify data is deleted
        assertFalse("Article should not exist after deletion", checkArticleExists(articleId));
        assertFalse("Keywords should be deleted", checkKeywordsExist(articleId));
        assertFalse("References should be deleted", checkReferencesExist(articleId));
        assertFalse("Related articles should be deleted", checkRelatedArticlesExist(articleId));
    }

    // Helper methods to insert and check test data
    private long insertTestArticle(long uniqueId) throws SQLException {
        String sql = "INSERT INTO Articles (uniqueID, title, shortDescription, difficulty, body, ISENCRYPTED) " +
                     "VALUES (?, 'Test Title', 'Test Description', 'beginner', 'Test Body', false)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, uniqueId);
            pstmt.executeUpdate();
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        throw new SQLException("Could not insert test article");
    }

    private void insertTestKeywords(long articleId) throws SQLException {
        String sql = "INSERT INTO Keywords (articleID, keyword) VALUES (?, 'test')";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, articleId);
            pstmt.executeUpdate();
        }
    }

    private void insertTestReferences(long articleId) throws SQLException {
        String sql = "INSERT INTO References (articleID, referenceLink) VALUES (?, 'http://test.com')";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, articleId);
            pstmt.executeUpdate();
        }
    }

    private void insertTestRelatedArticles(long articleId) throws SQLException {
        String sql = "INSERT INTO RelatedArticles (articleID, relatedArticleID) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, articleId);
            pstmt.setLong(2, articleId + 1);
            pstmt.executeUpdate();
        }
    }

    private boolean checkArticleExists(long articleId) throws SQLException {
        return checkCountExists("Articles", articleId);
    }

    private boolean checkKeywordsExist(long articleId) throws SQLException {
        return checkCountExists("Keywords", articleId);
    }

    private boolean checkReferencesExist(long articleId) throws SQLException {
        return checkCountExists("References", articleId);
    }

    private boolean checkRelatedArticlesExist(long articleId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM RelatedArticles WHERE articleID = ? OR relatedArticleID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, articleId);
            pstmt.setLong(2, articleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private boolean checkCountExists(String tableName, long articleId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE articleID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, articleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
}