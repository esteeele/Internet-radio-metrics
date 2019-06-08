package com.service.radiodownloader.pagescraping.bbc;

import com.service.radiodownloader.dataclasses.ProgrammeData;
import com.service.radiodownloader.dataclasses.Track;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.time.LocalDate;
import java.util.List;


public class TestProgrammeParser {

    @Autowired
    private ParseBBCSchedulePage parseBBCSchedulePage;

    @Test
    public void testParseDJPage() throws Exception {
        String url = System.getProperty("user.dir") + "/src/test/resources/sampleProgrammePage.html";
        File htmlFile = new File(url);
        Document doc = Jsoup.parse(htmlFile, "UTF-8");
        List<Track> res = ParseBBCDJPage.getAllTracks(doc);
        Assert.assertFalse(res.isEmpty());
        Assert.assertEquals("The Kinks", res.get(0).getArtist());
        Assert.assertEquals("Singularity", res.get(3).getSong_name());
    }

    @Test
    public void canGetPresenterName() throws Exception {
        String url = System.getProperty("user.dir") + "/src/test/resources/sampleProgrammePage.html";
        File htmlFile = new File(url);
        Document doc = Jsoup.parse(htmlFile, "UTF-8");
        String presenterName = ParseBBCDJPage.getPresenter(doc);
        Assert.assertEquals("Steve Lamacq", presenterName);
    }

    @Test
    public void canGetDescription() throws Exception {
        String url = System.getProperty("user.dir") + "/src/test/resources/sampleProgrammePage.html";
        File htmlFile = new File(url);
        Document doc = Jsoup.parse(htmlFile, "UTF-8");
        String description = ParseBBCDJPage.getDescription(doc);
        Assert.assertEquals("Steve replays his Classic Album of the Day interview with Steel Pulse, about their debut 1978 album Handsworth Revolution.", description);
    }

    @Test
    public void canParseScehulePage() throws Exception {
        String url = System.getProperty("user.dir") + "/src/test/resources/sampleProgrammeListPage.html";
        File htmlFile = new File(url);
        Document doc = Jsoup.parse(htmlFile, "UTF-8");
        List<ProgrammeData> programmesOnDay = parseBBCSchedulePage.getAllShows(doc, LocalDate.now());
        Assert.assertEquals(0, programmesOnDay.size());

    }
}