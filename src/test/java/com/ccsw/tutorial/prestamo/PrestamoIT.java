package com.ccsw.tutorial.prestamo;

import com.ccsw.tutorial.author.model.AuthorDto;
import com.ccsw.tutorial.category.model.CategoryDto;
import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.common.pagination.PageableRequest;
import com.ccsw.tutorial.config.ResponsePage;
import com.ccsw.tutorial.game.model.GameDto;
import com.ccsw.tutorial.prestamo.model.PrestamoDto;
import com.ccsw.tutorial.prestamo.model.PrestamoSearchDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PrestamoIT {

    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/prestamo";
    private static final String GAME_ID_PARAM = "id_game";
    private static final String CLIENT_ID_PARAM = "id_client";
    private static final String FILTER_DATE_PARAM = "filterDate";
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    ParameterizedTypeReference<ResponsePage<PrestamoDto>> responseTypePage = new ParameterizedTypeReference<ResponsePage<PrestamoDto>>() {
    };

    ParameterizedTypeReference<List<PrestamoDto>> responseTypeList = new ParameterizedTypeReference<List<PrestamoDto>>() {
    };

    private String getUrlWithParams(Map<String, Object> params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(LOCALHOST + port + SERVICE_PATH);

        if (params.get(GAME_ID_PARAM) != null) {
            builder.queryParam(GAME_ID_PARAM, params.get(GAME_ID_PARAM));
        }
        if (params.get(CLIENT_ID_PARAM) != null) {
            builder.queryParam(CLIENT_ID_PARAM, params.get(CLIENT_ID_PARAM));
        }
        if (params.get(FILTER_DATE_PARAM) != null) {
            builder.queryParam(FILTER_DATE_PARAM, params.get(FILTER_DATE_PARAM));
        }

        return builder.encode().toUriString();
    }

    private static final int TOTAL_PRESTAMOS = 5;
    public static final Long DELETE_PRESTAMO_ID = 5L;

    private static final int PAGE_SIZE = 5;
    private static final String EXISTS_CLIENT = "1";
    private static final String EXISTS_GAME = "1";
    private static final String NO_EXISTS_CLIENT = "5";
    private static final String NO_EXISTS_GAME = "5";
    private static final String EXISTS_FILTER_DATE = "2025-02-28";
    private static final String NO_EXISTS_FILTER_DATE = "1900-01-01";

    @Test
    public void findFirstPageWithFiveSizeShouldReturnFirstFiveResults() {

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(TOTAL_PRESTAMOS, response.getBody().getTotalElements());
        assertEquals(PAGE_SIZE, response.getBody().getContent().size());
    }

    @Test
    public void findSecondPageWithFiveSizeShouldReturnLastResult() {

        int elementsCount = TOTAL_PRESTAMOS - PAGE_SIZE;

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(1, PAGE_SIZE));

        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(TOTAL_PRESTAMOS, response.getBody().getTotalElements());
        assertEquals(elementsCount, response.getBody().getContent().size());
    }

    @Test
    public void saveWithoutIdShouldCreateNewPrestamo() {

        long newPrestamoId = TOTAL_PRESTAMOS + 1;
        long newPrestamoSize = TOTAL_PRESTAMOS + 1;

        ClientDto dto = new ClientDto();
        dto.setName("PEPE");
        dto.setId(1L);

        GameDto dto2 = new GameDto();
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        dto2.setTitle("NUEVO_TITULO");
        dto2.setAge("18");
        dto2.setAuthor(authorDto);
        dto2.setCategory(categoryDto);
        dto2.setId(1L);

        PrestamoDto dto3 = new PrestamoDto();
        dto3.setClient(dto);
        dto3.setGame(dto2);
        dto3.setLoanDate(LocalDate.of(2025, 8, 26));
        dto3.setReturnDate(LocalDate.of(2025, 8, 28));

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto3), Void.class);

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, (int) newPrestamoSize));

        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(newPrestamoSize, response.getBody().getTotalElements());

        PrestamoDto prestamo = response.getBody().getContent().stream().filter(item -> item.getId().equals(newPrestamoId)).findFirst().orElse(null);
        assertNotNull(prestamo);
        assertEquals(dto3.getLoanDate(), prestamo.getLoanDate());
    }

    @Test
    public void deleteWithExistsIdShouldDeleteCategory() {

        long newPrestamoSize = TOTAL_PRESTAMOS - 1;

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + DELETE_PRESTAMO_ID, HttpMethod.DELETE, null, Void.class);

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, TOTAL_PRESTAMOS));

        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(newPrestamoSize, response.getBody().getTotalElements());
    }

    @Test
    public void deleteWithNotExistsIdShouldThrowException() {

        long newPrestamoId = TOTAL_PRESTAMOS + 1;

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + newPrestamoId, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void findExistsClientShouldReturnPrestamos() {
        int PRESTAMOS_WITH_FILTERS = 4;

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_ID_PARAM, null);
        params.put(FILTER_DATE_PARAM, null);
        params.put(CLIENT_ID_PARAM, EXISTS_CLIENT);

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, TOTAL_PRESTAMOS));
        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(params), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(PRESTAMOS_WITH_FILTERS, response.getBody().getContent().size());
    }

    @Test
    public void findNotExistsClientShouldNoReturnPrestamos() {
        int PRESTAMOS_WITH_FILTERS = 0;

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_ID_PARAM, null);
        params.put(FILTER_DATE_PARAM, null);
        params.put(CLIENT_ID_PARAM, NO_EXISTS_CLIENT);

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, TOTAL_PRESTAMOS));
        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(params), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(PRESTAMOS_WITH_FILTERS, response.getBody().getContent().size());
    }

    @Test
    public void findExistsGameShouldReturnPrestamos() {
        int PRESTAMOS_WITH_FILTERS = 4;

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_ID_PARAM, EXISTS_GAME);
        params.put(FILTER_DATE_PARAM, null);
        params.put(CLIENT_ID_PARAM, null);

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, TOTAL_PRESTAMOS));
        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(params), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(PRESTAMOS_WITH_FILTERS, response.getBody().getContent().size());
    }

    @Test
    public void findNotExistsGameShouldNoReturnPrestamos() {
        int PRESTAMOS_WITH_FILTERS = 0;

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_ID_PARAM, NO_EXISTS_GAME);
        params.put(FILTER_DATE_PARAM, null);
        params.put(CLIENT_ID_PARAM, null);

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, TOTAL_PRESTAMOS));
        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(params), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(PRESTAMOS_WITH_FILTERS, response.getBody().getContent().size());
    }

    @Test
    public void findExistsFilterDateShouldReturnPrestamos() {
        int PRESTAMOS_WITH_FILTERS = 1;

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_ID_PARAM, null);
        params.put(FILTER_DATE_PARAM, EXISTS_FILTER_DATE);
        params.put(CLIENT_ID_PARAM, null);

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, TOTAL_PRESTAMOS));
        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(params), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(PRESTAMOS_WITH_FILTERS, response.getBody().getContent().size());
    }

    @Test
    public void findNotExistsFilterDateShouldNoReturnPrestamos() {
        int PRESTAMOS_WITH_FILTERS = 0;

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_ID_PARAM, null);
        params.put(FILTER_DATE_PARAM, NO_EXISTS_FILTER_DATE);
        params.put(CLIENT_ID_PARAM, null);

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, TOTAL_PRESTAMOS));
        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(params), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(PRESTAMOS_WITH_FILTERS, response.getBody().getContent().size());
    }

    @Test
    public void findExistsAllFilterDateShouldReturnPrestamos() {
        int PRESTAMOS_WITH_FILTERS = 1;

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_ID_PARAM, EXISTS_GAME);
        params.put(FILTER_DATE_PARAM, EXISTS_FILTER_DATE);
        params.put(CLIENT_ID_PARAM, EXISTS_CLIENT);

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, TOTAL_PRESTAMOS));
        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(params), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(PRESTAMOS_WITH_FILTERS, response.getBody().getContent().size());
    }
}
