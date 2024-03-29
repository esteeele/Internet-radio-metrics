package com.service.api.presenters.programmes;

import com.service.api.BaseResource;
import com.service.api.presenters.CommonTestDaos;
import com.service.api.presenters.PresenterResource;
import com.service.api.stations.Stations;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetPresenterProgrammesIT {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private static final String PATH = ProgrammesResource.PRESENTER_PROGRAMMES_PATH;
    private CommonTestDaos commonTestDaos;

    @Before
    public void setUp() throws Exception {
        commonTestDaos = new CommonTestDaos(jdbcTemplate);
    }

    @Test
    public void invalidStationIdReturnsNotFound() {
        String stationId = "{" + BaseResource.STATION_ID + "}";
        String presenterIdToken = "{" + PresenterResource.PRESENTER_ID + "}";

        UUID presenterId = UUID.randomUUID();

        String constructedPath = PATH.replace(stationId, "76").replace(presenterIdToken, presenterId.toString());
        ResponseEntity responseEntity = this.restTemplate.getForEntity("http://localhost:" + port + constructedPath, Object.class);

        Assert.assertEquals(responseEntity.getStatusCode().value(), SC_NOT_FOUND);
    }

    @Test
    public void shouldListPresenterProgrammes() {
        UUID presenterId = commonTestDaos.insertPresenter("foo_bar", Stations.BBC_6_MUSIC.getStationId());

        for (int i = 1; i <= 10; i++) {
            commonTestDaos.insertProgramme(presenterId, "/foo/bar/", "a programme description");
        }

        String stationId = "{" + BaseResource.STATION_ID + "}";
        String presenterIdToken = "{" + PresenterResource.PRESENTER_ID + "}";
        String constructedPath = PATH.replace(stationId, String.valueOf(Stations.BBC_6_MUSIC.getStationId()))
                .replace(presenterIdToken, presenterId.toString());

        ResponseEntity<List> responseEntity = this.restTemplate.getForEntity("http://localhost:" + port + constructedPath, List.class);

        Assert.assertEquals(responseEntity.getStatusCode().value(), SC_OK);
        List<Object> presenter = responseEntity.getBody();

        Assert.assertNotNull(presenter);
        Assert.assertEquals(presenter.size(), 10);
    }
}
