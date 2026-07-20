# UninaMultiCloud

Progetto Java Swing (Eclipse) organizzato secondo il pattern BCE, con persistenza su PostgreSQL via JDBC.

## Struttura del progetto

```
src/
 ├─ boundaries/   interfacce grafiche Swing (JFrame, JDialog, JPanel)
 ├─ control/      logica applicativa
 ├─ db/           DAO e connessione JDBC
 └─ entity/       classi di dominio ed enumerazioni
database/
 ├─ table.sql         struttura delle tabelle
 ├─ trigger.sql        funzioni, trigger e viste
 └─ popolamento.sql    dati di prova
```

## Importare in Eclipse

`File > Import... > General > Existing Projects into Workspace`, seleziona questa cartella.

## Driver PostgreSQL

Scarica `postgresql-<versione>.jar` da [jdbc.postgresql.org/download](https://jdbc.postgresql.org/download/)
e aggiungilo con `Tasto destro sul progetto > Build Path > Configure Build Path > Libraries > Add External JARs`.

## Database

Crea un database `uninamulticloud` su PostgreSQL, poi esegui in **questo ordine**:

1. `database/table.sql`
2. `database/trigger.sql`
3. `database/popolamento.sql`

Utenti di prova dopo il popolamento (username / password): `marracash` / `persona`,
`sferaebbasta` / `xdvr`, `tonyboy` / `goinghard`, `vinz` / `vinzamic`, `wappo` / `wappoamic`.

Le credenziali di connessione sono in `src/db/ConnessioneDatabase.java` (`URL`, `UTENTE_DB`, `PASSWORD_DB`):
vanno adattate all'installazione locale di PostgreSQL.

## Avvio

Classe main: `boundaries.SchermataBenvenuto` → `Run As > Java Application`.
