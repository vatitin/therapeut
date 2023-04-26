/*
 * Copyright (C) 2022 - present Juergen Zimmermann, Hochschule Karlsruhe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.acme.therapeut.rest;

import com.acme.therapeut.service.EmailExistsException;
import com.acme.therapeut.service.TherapeutWriteService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static com.acme.therapeut.rest.TherapeutGetController.REST_PATH;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;

/**
 * Eine `@RestController`-Klasse bildet die REST-Schnittstelle, wobei die HTTP-Methoden, Pfade und MIME-Typen auf die
 * Methoden der Klasse abgebildet werden.
 *
 * @author Valentin Sackmann
 */
@Controller
@RequestMapping(REST_PATH)
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("ClassFanOutComplexity")
class TherapeutWriteController {
    @SuppressWarnings("TrailingComment")
    private static final String PROBLEM_PATH = "/problem/";
    private final TherapeutWriteService service;
    private final UriHelper uriHelper;


    /**
     * Einen neuen Therapeut-Datensatz anlegen.
     *
     * @param therapeutDTO Das Therapeutenobjekt aus dem eingegangenen Request-Body.
     * @param request Das Request-Objekt, um `Location` im Response-Header zu erstellen.
     * @return Response mit Statuscode 201 einschließlich Location-Header oder Statuscode 422 falls
     *      die Emailadresse bereits existiert oder Statuscode 400 falls syntaktische Fehler im Request-Body
     *      vorliegen.
     */
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "201", description = "Therapeut neu angelegt")
    @ApiResponse(responseCode = "422", description = "Email vorhanden")
    @SuppressWarnings("TrailingComment")
    ResponseEntity<Void> create(@RequestBody final TherapeutDTO therapeutDTO, final HttpServletRequest request) {
        log.debug("create: {}", therapeutDTO);

        final var therapeut = service.create(therapeutDTO.toTherapeut());
        final var baseUri = uriHelper.getBaseUri(request).toString();
        final var location = URI.create(baseUri + '/' + therapeut.getId());
        return created(location).build();
    }

    @ExceptionHandler
    ProblemDetail onEmailExists(final EmailExistsException ex, final HttpServletRequest request) {
        log.debug("onEmailExists: {}", ex.getMessage());
        final var problemDetail = ProblemDetail.forStatusAndDetail(UNPROCESSABLE_ENTITY, ex.getMessage());
        problemDetail.setType(URI.create(PROBLEM_PATH + ProblemType.CONSTRAINTS.getValue()));
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));
        return problemDetail;
    }
}
