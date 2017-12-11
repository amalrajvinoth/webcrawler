package com.wiprodigital.webcrawler.web;

import com.wiprodigital.webcrawler.exception.BadRequest;
import com.wiprodigital.webcrawler.model.CrawlResults;
import com.wiprodigital.webcrawler.service.WebCrawlerService;
import io.swagger.annotations.*;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value="api/v1.0/webcrawler", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value="Operations pertaining to URL Reader")
public class WebCrawlerController {
    
	private Logger logger = Logger.getLogger(this.getClass());
    
    @Autowired
    WebCrawlerService service;

    @ApiOperation(value = "Extract crawl result for given URL")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "No Result found"),
            @ApiResponse(code = 400, message = "Invalid URL"), })
    @RequestMapping(value = "/extract", method = RequestMethod.GET)
    public @ResponseBody
    CrawlResults extract(@ApiParam(value = "URL", required = true) @RequestParam("url") String url) {
        url = service.decode(url);

        UrlValidator urlValidator = new UrlValidator();
        if (!urlValidator.isValid(url)) {
            throw new BadRequest("Invalid URL");
        }
        return service.extract(url);
    }
}
