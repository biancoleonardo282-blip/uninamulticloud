# UninaMultiCloud

Piattaforma per pubblicare, creare e condividere playlist audio/video, sviluppata in Java (Swing) con backend PostgreSQL.

## Requisiti

- **JDK 26** (o superiore, con supporto ai moduli)
- **Eclipse IDE** (per l'esecuzione tramite `.project`/`.classpath` già inclusi)
- **PostgreSQL** (versione 14+ consigliata)

Il driver JDBC per PostgreSQL è già incluso nel repository in `lib/postgresql-42.7.12.jar` ed è referenziato dal `.classpath` con path relativo: non serve scaricarlo a parte.

## Setup del database

1. Installa PostgreSQL e assicurati che sia in ascolto su `localhost:5432`.
2. Crea l'utente/verifica che esista l'utente `postgres` con password `postgres` (credenziali usate da `src/db/ConnessioneDatabase.java`). Se preferisci credenziali diverse, modificale in quel file.
3. Crea il database:
   ```sql
   CREATE DATABASE uninamulticloud;
   ```
4. Esegui gli script SQL in `database/`, **nell'ordine**:
   ```
   database/table.sql
   database/trigger.sql
   database/popolamento.sql
   ```
   Ad esempio da riga di comando:
   ```
   psql -U postgres -h localhost -d uninamulticloud -f database/table.sql
   psql -U postgres -h localhost -d uninamulticloud -f database/trigger.sql
   psql -U postgres -h localhost -d uninamulticloud -f database/popolamento.sql
   ```

## Avvio dell'applicazione

1. In Eclipse: **File → Open Projects from File System...** e seleziona la cartella del repository (contiene già `.project` e `.classpath`).
2. Verifica che il progetto usi un JDK 26 come JRE del progetto (Eclipse dovrebbe rilevarlo automaticamente dal `.classpath`; in caso contrario aggiungilo da **Window → Preferences → Java → Installed JREs**).
3. Avvia `src/boundaries/SchermataBenvenuto.java` come **Java Application** (unica classe con `main`).

## Struttura del progetto

- `src/boundaries` — interfacce grafiche (Swing)
- `src/control` — controller applicativi
- `src/db` — accesso al database (DAO + connessione JDBC)
- `src/entity` — entità di dominio
- `database` — script SQL per schema, trigger e popolamento dati
- `lib` — dipendenze esterne (driver JDBC PostgreSQL)
