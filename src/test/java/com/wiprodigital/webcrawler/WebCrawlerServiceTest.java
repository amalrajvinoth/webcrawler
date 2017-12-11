package com.wiprodigital.webcrawler;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import com.wiprodigital.webcrawler.model.CrawlResults;
import com.wiprodigital.webcrawler.service.WebCrawlerService;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WebCrawlerServiceTest {

    private static final String WEB_SITE_URL = "http://wiprodigital.com/";
    private static final String META_DECRIPTION = "The full breadth of Wipro Digital and Designit capabilities in strategy, design and technology positions us at intersections where we derive insight, shape interaction, develop integration and unlock innovation - with the customer's journey at the center of everything we do.";

    WebCrawlerService service;

    @Before
    public void setUp() throws Exception {
        service = new WebCrawlerService();
    }

    @After
    public void tearDown() throws Exception {
        service = null;
    }

    @Test
    public void testGetTitle() {
        CrawlResults r = service.extract(WEB_SITE_URL);
        assertThat(r.getTitle(), is("Digital Transformation - Wipro Digital"));
        assertThat(r.getDescription(), is(META_DECRIPTION));
        assertThat(r.getSitemap(), hasSize(5));
        assertThat(r.getResources(), hasSize(98));
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidURL() {
        CrawlResults r = service.extract("invalid-url");
    }

    /* Test decoded URL */
    @Test
    public void testDecodedURL() {
        String decodedURL = service.decode("http://localhost:8080/api/v1.0/webcrawler/extract?url=http%3A%2F%2Fwiprodigital.com%2F");
        assertThat(decodedURL, is("http://localhost:8080/api/v1.0/webcrawler/extract?url=http://wiprodigital.com/"));
    }
}