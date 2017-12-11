package com.wiprodigital.webcrawler;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import com.wiprodigital.webcrawler.Main;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
        )
@AutoConfigureMockMvc
public class WebCrawlerServiceIntegrationTest {

	private static final String BASE_URL = "/api/v1.0/webcrawler/extract";
    private static final String WEB_SITE_URL = "http://wiprodigital.com/";

    private static final String META_DECRIPTION = "The full breadth of Wipro Digital and Designit capabilities in strategy, design and technology positions us at intersections where we derive insight, shape interaction, develop integration and unlock innovation - with the customer's journey at the center of everything we do.";

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void extractSuccess() throws Exception {

        mvc.perform(get(BASE_URL+"?url="+WEB_SITE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }


    /* Test Invalid url */
    @Test
    public void testInvalidURL() throws Exception {

        mvc.perform(get(BASE_URL+"?url=invalid_url")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /* Test Invalid url */
    @Test
    public void testMetaTags() throws Exception {

        mvc.perform(get(BASE_URL+"?url="+WEB_SITE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Digital Transformation - Wipro Digital")))
                .andExpect(jsonPath("$.description", is(META_DECRIPTION)));
    }

    /* Test sitemap */
    @Test
    public void testSitemapResource() throws Exception {

        mvc.perform(get(BASE_URL+"?url="+WEB_SITE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sitemap", hasSize(5)))
                .andExpect(jsonPath("$.resources", hasSize(98)));
    }
}