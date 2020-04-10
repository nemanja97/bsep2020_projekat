# IAM

## Keycloak konfiguracija

1) Skinuti i instalirati verziju
2) Podesiti startni port na :8180 (./standalone.bat -Djboss.http.port=8180 -Dkeycloak.profile.feature.upload_scripts=enabled)
3) Dodati novi realm ispod Master-a i kliknuti na opciju import
4) Importovati realm-export.json
5) Kreirati test korisnike
