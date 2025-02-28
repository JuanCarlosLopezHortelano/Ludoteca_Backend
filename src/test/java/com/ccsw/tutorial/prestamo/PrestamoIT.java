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

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PrestamoIT {

    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/prestamo";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    ParameterizedTypeReference<ResponsePage<PrestamoDto>> responseTypePage = new ParameterizedTypeReference<ResponsePage<PrestamoDto>>() {
    };

    ParameterizedTypeReference<List<PrestamoDto>> responseTypeList = new ParameterizedTypeReference<List<PrestamoDto>>() {
    };

    private static final int TOTAL_PRESTAMOS = 5;
    public static final Long DELETE_PRESTAMO_ID = 5L;
    public static final Long MODIFY_AUTHOR_ID = 3L;
    public static final String NEW_AUTHOR_NAME = "Nuevo Autor";
    public static final String NEW_NATIONALITY = "Nueva Nacionalidad";

    private static final int PAGE_SIZE = 5;

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

}
