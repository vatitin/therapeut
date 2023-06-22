package com.acme.therapeut.repository;

import com.acme.therapeut.entity.GeschlechtType;
import com.acme.therapeut.entity.QTherapeut;
import com.acme.therapeut.entity.TaetigkeitsbereichType;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static java.util.Locale.GERMAN;

/**
 * Singleton-Klasse, um Prädikate durch QueryDSL für eine WHERE-Klausel zu bauen.
 *
 * @author Valentin Sackmann
 */
@Component
@Slf4j
public class PredicateBuilder {
    /**
     * Prädikate durch QueryDSL für eine WHERE-Klausel zu bauen.
     *
     * @param queryParams als MultiValueMap
     * @return Predicate in QueryDSL für eine WHERE-Klausel
     */
    @SuppressWarnings("ReturnCount")
    public Optional<Predicate> build(final Map<String, ? extends List<String>> queryParams) {
        log.debug("build: queryParams={}", queryParams);

        final var qTherapeut = QTherapeut.therapeut;
        final var booleanExprList = queryParams
            .entrySet()
            .stream()
            .map(entry -> toBooleanExpression(entry.getKey(), entry.getValue(), qTherapeut))
            .toList();
        if (booleanExprList.isEmpty() || booleanExprList.contains(null)) {
            return Optional.empty();
        }

        final var result = booleanExprList
            .stream()
            .reduce(booleanExprList.get(0), BooleanExpression::and);
        return Optional.of(result);
    }

    @SuppressWarnings({"CyclomaticComplexity", "UnnecessaryParentheses"})
    private BooleanExpression toBooleanExpression(
        final String paramName,
        final List<String> paramValues,
        final QTherapeut qTherapeut
    ) {
        log.trace("toSpec: paramName={}, paramValues={}", paramName, paramValues);

        if (paramValues == null || (!Objects.equals(paramName, "taetigkeitsbereich") && paramValues.size() != 1)) {
            return null;
        }

        final var value = paramValues.get(0);
        return switch (paramName) {
            case "nachname" -> nachname(value, qTherapeut);
            case "vorname" -> vorname(value, qTherapeut);
            case "email" ->  email(value, qTherapeut);
            case "geschlecht" -> geschlecht(value, qTherapeut);
            case "taetigkeitsbereiche" -> taetigkeitsbereiche(paramValues, qTherapeut);
            case "plz" -> plz(value, qTherapeut);
            case "ort" -> ort(value, qTherapeut);
            default -> null;
        };
    }

    private BooleanExpression nachname(final String teil, final QTherapeut qTherapeut) {
        return qTherapeut.nachname.toLowerCase().matches("%" + teil.toLowerCase(GERMAN) + '%');
    }

    private BooleanExpression vorname(final String teil, final QTherapeut qTherapeut) {
        return qTherapeut.vorname.toLowerCase().matches("%" + teil.toLowerCase(GERMAN) + '%');
    }

    private BooleanExpression email(final String teil, final QTherapeut qTherapeut) {
        return qTherapeut.email.toLowerCase().matches("%" + teil.toLowerCase(GERMAN) + '%');
    }

    private BooleanExpression geschlecht(final String geschlecht, final QTherapeut qTherapeut) {
        return qTherapeut.geschlecht.eq(GeschlechtType.of(geschlecht).orElse(null));
    }

    private BooleanExpression taetigkeitsbereiche(final Collection<String> taetigkeitsbereiche, final QTherapeut qTherapeut) {
        if (taetigkeitsbereiche == null || taetigkeitsbereiche.isEmpty()) {
            return null;
        }

        final var expressionList = taetigkeitsbereiche
            .stream()
            .map(interesseStr -> TaetigkeitsbereichType.of(interesseStr).orElse(null))
            .filter(Objects::nonNull)
            .map(interesse -> qTherapeut.taetigkeitsbereicheStr.matches("%" + interesse.name() + '%'))
            .toList();
        if (expressionList.isEmpty()) {
            return null;
        }

        return expressionList
            .stream()
            .reduce(expressionList.get(0), BooleanExpression::and);
    }

    private BooleanExpression plz(final String prefix, final QTherapeut qTherapeut) {
        return qTherapeut.adresse.plz.matches(prefix + '%');
    }

    private BooleanExpression ort(final String prefix, final QTherapeut qTherapeut) {
        return qTherapeut.adresse.ort.toLowerCase().matches("%" + prefix.toLowerCase(GERMAN) + '%');
    }
}
