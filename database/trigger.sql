CREATE OR REPLACE FUNCTION fn_crea_statistiche()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO statisticheriproduzione
        (id_elemento, numeroascolti, numerocondivisioni, mipiace)
    VALUES (NEW.id_elemento, 0, 0, 0);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_crea_statistiche
    AFTER INSERT ON elementomultimediale
    FOR EACH ROW EXECUTE FUNCTION fn_crea_statistiche();



CREATE OR REPLACE FUNCTION fn_aggiorna_numero_brani()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        UPDATE playlist
           SET numerobrani = numerobrani + 1
         WHERE id_playlist = NEW.id_playlist;
        RETURN NEW;
    ELSE
        UPDATE playlist
           SET numerobrani = numerobrani - 1
         WHERE id_playlist = OLD.id_playlist;
        RETURN OLD;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_aggiorna_numero_brani
    AFTER INSERT OR DELETE ON includeelemento
    FOR EACH ROW EXECUTE FUNCTION fn_aggiorna_numero_brani();



CREATE OR REPLACE FUNCTION fn_condivisione_solo_condivise()
RETURNS TRIGGER AS $$
DECLARE
    v_iscondivisa BOOLEAN;
    v_nome        VARCHAR(100);
BEGIN
    SELECT iscondivisa, nome
      INTO v_iscondivisa, v_nome
      FROM playlist
     WHERE id_playlist = NEW.id_playlist;

    IF NOT FOUND THEN
        RAISE EXCEPTION
            'CkCondivisioneSoloCondivise: la playlist % non esiste',
            NEW.id_playlist;
    END IF;

    IF NOT v_iscondivisa THEN
        RAISE EXCEPTION
            'CkCondivisioneSoloCondivise: la playlist "%" (id %) non ha visibilita Condivisa',
            v_nome, NEW.id_playlist;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_condivisione_solo_condivise
    BEFORE INSERT OR UPDATE ON condivisione
    FOR EACH ROW EXECUTE FUNCTION fn_condivisione_solo_condivise();



CREATE OR REPLACE FUNCTION fn_playlist_visibilita_coerente()
RETURNS TRIGGER AS $$
DECLARE
    v_condivisioni INTEGER;
BEGIN
    IF OLD.iscondivisa AND NOT NEW.iscondivisa THEN
        SELECT COUNT(*) INTO v_condivisioni
          FROM condivisione
         WHERE id_playlist = OLD.id_playlist;

        IF v_condivisioni > 0 THEN
            RAISE EXCEPTION
                'CkCondivisioneSoloCondivise: la playlist % ha % condivisioni attive, revocarle prima di cambiare visibilita',
                OLD.id_playlist, v_condivisioni;
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_playlist_visibilita_coerente
    BEFORE UPDATE ON playlist
    FOR EACH ROW EXECUTE FUNCTION fn_playlist_visibilita_coerente();



CREATE OR REPLACE FUNCTION fn_normalizza_amicizia()
RETURNS TRIGGER AS $$
DECLARE
    v_scambio INTEGER;
BEGIN
    IF NEW.codiceutente1 = NEW.codiceutente2 THEN
        RAISE EXCEPTION
            'CkAmiciziaNonRiflessiva: un utente non puo essere amico di se stesso (codice %)',
            NEW.codiceutente1;
    END IF;

    IF NEW.codiceutente1 > NEW.codiceutente2 THEN
        v_scambio          := NEW.codiceutente1;
        NEW.codiceutente1  := NEW.codiceutente2;
        NEW.codiceutente2  := v_scambio;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_normalizza_amicizia
    BEFORE INSERT OR UPDATE ON amicizia
    FOR EACH ROW EXECUTE FUNCTION fn_normalizza_amicizia();



CREATE OR REPLACE FUNCTION fn_no_condivisione_proprietario()
RETURNS TRIGGER AS $$
DECLARE
    v_proprietario INTEGER;
BEGIN
    SELECT codiceutente INTO v_proprietario
      FROM playlist
     WHERE id_playlist = NEW.id_playlist;

    IF v_proprietario = NEW.codiceutente THEN
        RAISE EXCEPTION
            'Condivisione non valida: la playlist % appartiene gia allutente %',
            NEW.id_playlist, NEW.codiceutente;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_no_condivisione_proprietario
    BEFORE INSERT OR UPDATE ON condivisione
    FOR EACH ROW EXECUTE FUNCTION fn_no_condivisione_proprietario();


CREATE OR REPLACE FUNCTION fn_condivisione_solo_tra_amici()
RETURNS TRIGGER AS $$
DECLARE
    v_proprietario INTEGER;
    v_min          INTEGER;
    v_max          INTEGER;
BEGIN
    SELECT codiceutente INTO v_proprietario
      FROM playlist
     WHERE id_playlist = NEW.id_playlist;

    v_min := LEAST(v_proprietario, NEW.codiceutente);
    v_max := GREATEST(v_proprietario, NEW.codiceutente);

    IF NOT EXISTS (SELECT 1
                     FROM amicizia
                    WHERE codiceutente1 = v_min
                      AND codiceutente2 = v_max) THEN
        RAISE EXCEPTION
            'Condivisione non valida: lutente % non e amico del proprietario % della playlist %',
            NEW.codiceutente, v_proprietario, NEW.id_playlist;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_condivisione_solo_tra_amici
    BEFORE INSERT OR UPDATE ON condivisione
    FOR EACH ROW EXECUTE FUNCTION fn_condivisione_solo_tra_amici();



CREATE OR REPLACE FUNCTION fn_condivisione_incrementa_stat()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE statisticheriproduzione
       SET numerocondivisioni = numerocondivisioni + 1
     WHERE id_elemento IN (SELECT id_elemento
                             FROM includeelemento
                            WHERE id_playlist = NEW.id_playlist);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_condivisione_incrementa_stat
    AFTER INSERT ON condivisione
    FOR EACH ROW EXECUTE FUNCTION fn_condivisione_incrementa_stat();


CREATE OR REPLACE VIEW v_catalogo AS
SELECT e.id_elemento,
       e.titolo,
       e.tipomedia,
       e.durata,
       u.username AS autore,
       s.numeroascolti,
       s.mipiace,
       s.numerocondivisioni
  FROM elementomultimediale e
  JOIN utente u                  ON u.codiceutente = e.codiceutente
  JOIN statisticheriproduzione s ON s.id_elemento  = e.id_elemento;

CREATE OR REPLACE VIEW v_playlist_dettaglio AS
SELECT p.id_playlist,
       p.nome,
       CASE WHEN p.ispubblica  THEN 'Pubblica'
            WHEN p.iscondivisa THEN 'Condivisa'
            ELSE 'Privata' END AS visibilita,
       p.numerobrani,
       u.username AS proprietario,
       COUNT(c.codiceutente) AS collaboratori
  FROM playlist p
  JOIN utente u       ON u.codiceutente = p.codiceutente
  LEFT JOIN condivisione c ON c.id_playlist = p.id_playlist
 GROUP BY p.id_playlist, p.nome, p.ispubblica, p.iscondivisa,
          p.numerobrani, u.username;
