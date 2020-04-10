# PKI

## PKI pokretanje

1) Obrisati keystore.jks koji dolazi uz projekat
2) Unutar `src/main/resources/application.properties` fajla promeniti `server.ssl.enabled` na `False`, `spring.jpa.hibernate.ddl-auto` na `create-drop` i `keycloak.credentials.secret` staviti vrednost iz secret ID Keycloak clienta na serverskoj putanji realms/BSEP/clients/credentials
3) Pokrenuti aplikaciju i nakon startovanje servera, ugasiti ga
4) Pri ponovnom paljenju vratiti  `server.ssl.enabled` na `True`, `spring.jpa.hibernate.ddl-auto` na `update`
