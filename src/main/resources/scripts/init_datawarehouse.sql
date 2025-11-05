DROP TABLE IF EXISTS netflix_catalogo_documentario;

DROP TABLE IF EXISTS netflix_catalogo_comedia;

CREATE TABLE netflix_catalogo_documentario (
    id VARCHAR(5),
    title TEXT,
	"cast" TEXT,
	country TEXT,
	releaseYear TEXT,
	duration TEXT,
    PRIMARY KEY (id)
); 

CREATE TABLE netflix_catalogo_comedia (
    id VARCHAR(5),
    title TEXT,
	"cast" TEXT,
	country TEXT,
	releaseYear TEXT,
	duration TEXT,
    PRIMARY KEY (id)
); 