INSERT INTO utente (codiceutente, username, email, password, nome, cognome) VALUES
(1, 'marracash',    'marra@gmail.com',            'persona',   'Fabio',     'Rizzo'),
(2, 'sferaebbasta', 'sfera@gmail.com',            'xdvr',      'Gionata',   'Boschetti'),
(3, 'tonyboy',      'tony@gmail.com',             'goinghard', 'Antonio',   'Bozicevich'),
(4, 'vinz',         'vincebar@gmail.com',         'vinzamic',  'Vincenzo',  'Barretta'),
(5, 'wappo',        'christianbianco@gmail.com',  'wappoamic', 'Christian', 'Bianco');


INSERT INTO amicizia (codiceutente1, codiceutente2) VALUES
(4, 3),
(1, 2),
(4, 5);


INSERT INTO elementomultimediale
    (id_elemento, titolo, durata, descrizione, testo, datacreazione,
     immaginecopertina, tipomedia, bitrate, formato, codec,
     framerate, risoluzione, codiceutente) VALUES
(1, 'Crudelia', 194,
    'Canzone dell album Persona',
    'Un ragazzo incontra una ragazza' || chr(10) ||
    'Sono entrambi fuoco, incendiano la stanza',
    DATE '2026-01-10', 'persona.jpg', 'Audio', 320, 'MP3', 'MPEG-3', NULL, NULL, 1),

(2, 'Visiera A Becco', 185,
    'Canzone dell album XDVR',
    'Eh-eh, eh-eh-eh, eh' || chr(10) ||
    'E sta roba gli ha dato alla testa e non puoi piu salvarli' || chr(10) ||
    'TomTom: sempre in giro sto pensando ai soldi',
    DATE '2026-02-15', 'xdvr.jpg', 'Video', NULL, NULL, NULL, 60, '4K', 2),

(3, 'Umile', 165,
    'Canzone dell album Umile',
    'E-E-Ey' || chr(10) ||
    'Rimango umile' || chr(10) ||
    'Non ti metti mai in gioco, pero parli, sembri un giudice' || chr(10) ||
    'Mia madre mi ha educato e non montato, resto umile',
    DATE '2026-03-20', 'umile.jpg', 'Audio', 320, 'WAV', 'PCM', NULL, NULL, 3);


INSERT INTO playlist
    (id_playlist, nome, datacreazione, numerobrani, copertina,
     ispubblica, iscondivisa, isprivata, tagricerca, codiceutente) VALUES
(101, 'Old School', DATE '2026-07-01', 0, 'foto.jpg',
     TRUE,  FALSE, FALSE, 'rap italiano, classici, old school', 1),
(102, 'New Gen',    DATE '2026-07-02', 0, 'foto.jpg',
     FALSE, TRUE,  FALSE, NULL, 3),
(103, 'Ascolti miei', DATE '2026-07-05', 0, 'privata.jpg',
     FALSE, FALSE, TRUE,  NULL, 4);


INSERT INTO includeelemento (id_playlist, id_elemento) VALUES
(101, 1),
(101, 2),
(102, 3),
(103, 1),
(103, 3);


INSERT INTO condivisione
    (codiceutente, id_playlist, linkcondivisione, condivisioneamico) VALUES
(4, 102, 'https://music.app/playlist/trap-102', TRUE);

UPDATE statisticheriproduzione
   SET numeroascolti = 2500000, numerocondivisioni = 45000,  mipiace = 180000
 WHERE id_elemento = 1;
UPDATE statisticheriproduzione
   SET numeroascolti = 5000000, numerocondivisioni = 120000, mipiace = 400000
 WHERE id_elemento = 2;
UPDATE statisticheriproduzione
   SET numeroascolti = 850000,  numerocondivisioni = 15000,  mipiace = 65000
 WHERE id_elemento = 3;

SELECT setval(pg_get_serial_sequence('utente', 'codiceutente'),
              (SELECT MAX(codiceutente) FROM utente));
SELECT setval(pg_get_serial_sequence('elementomultimediale', 'id_elemento'),
              (SELECT MAX(id_elemento) FROM elementomultimediale));
SELECT setval(pg_get_serial_sequence('playlist', 'id_playlist'),
              (SELECT MAX(id_playlist) FROM playlist));

COMMIT;