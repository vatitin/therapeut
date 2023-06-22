
package com.acme.therapeut.dev;

import org.springframework.context.annotation.Profile;

/**
 * Konfigurationsklasse für die Anwendung bzw. den Microservice, falls das Profile dev aktiviert ist.
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@Profile(DevConfig.DEV)
@SuppressWarnings({"ClassNamePrefixedWithPackageName", "HideUtilityClassConstructor"})
public class DevConfig implements Flyway {
    /**
     * Konstante für das Spring-Profile "dev".
     */
    public static final String DEV = "dev";

    DevConfig() {
    }
}
