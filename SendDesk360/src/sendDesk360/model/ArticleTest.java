package sendDesk360.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class ArticleTest {

    @Test
    void testDefaultConstructor() {
        Article article = new Article();
        
        assertEquals(0, article.getArticleID());
        assertEquals(0, article.getUniqueID());
        assertNull(article.getTitle());
        assertNull(article.getShortDescription());
        assertNull(article.getDifficulty());
        assertNull(article.getBody());
        assertNull(article.getKeywords());
        assertNull(article.getReferenceLinks());
        assertNull(article.getRelatedArticleIDs());
    }

    @Test
    void testParameterizedConstructor() {
        Article article = new Article(12345L, "JUnit Testing", "Testing with JUnit",
                "Intermediate", "This is a body of the article.");
        
        assertEquals(12345L, article.getUniqueID());
        assertEquals("JUnit Testing", article.getTitle());
        assertEquals("Testing with JUnit", article.getShortDescription());
        assertEquals("Intermediate", article.getDifficulty());
        assertEquals("This is a body of the article.", article.getBody());
    }

    @Test
    void testSettersAndGetters() {
        Article article = new Article();

        article.setArticleID(1L);
        article.setUniqueID(12345L);
        article.setTitle("JUnit Testing");
        article.setShortDescription("Testing with JUnit");
        article.setDifficulty("Intermediate");
        article.setBody("This is a body of the article.");

        List<String> keywords = Arrays.asList("Java", "Testing", "JUnit");
        List<String> referenceLinks = Arrays.asList("https://example.com", "https://junit.org");
        List<Long> relatedArticleIDs = Arrays.asList(2L, 3L, 4L);

        article.setKeywords(keywords);
        article.setReferenceLinks(referenceLinks);
        article.setRelatedArticleIDs(relatedArticleIDs);

        assertEquals(1L, article.getArticleID());
        assertEquals(12345L, article.getUniqueID());
        assertEquals("JUnit Testing", article.getTitle());
        assertEquals("Testing with JUnit", article.getShortDescription());
        assertEquals("Intermediate", article.getDifficulty());
        assertEquals("This is a body of the article.", article.getBody());
        assertEquals(keywords, article.getKeywords());
        assertEquals(referenceLinks, article.getReferenceLinks());
        assertEquals(relatedArticleIDs, article.getRelatedArticleIDs());
    }

    @Test
    void testEmptyKeywordsAndReferenceLinks() {
        Article article = new Article();
        
        article.setKeywords(Arrays.asList());
        article.setReferenceLinks(Arrays.asList());
        
        assertTrue(article.getKeywords().isEmpty());
        assertTrue(article.getReferenceLinks().isEmpty());
    }
}
