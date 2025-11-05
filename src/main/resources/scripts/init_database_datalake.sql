DROP TABLE IF EXISTS livro;
DROP TABLE IF EXISTS netflix_catalogo;

CREATE TABLE livro (
    id TEXT,
    title TEXT,
    price TEXT,
    userId TEXT,
    profileName TEXT,
    reviewHelpfulness TEXT,
    reviewScore TEXT,
    reviewTime TEXT,
    reviewSummary TEXT,
    reviewText TEXT
);

CREATE TABLE netflix_catalogo (
    id VARCHAR(5),
    title TEXT,
	"cast" TEXT,
	country TEXT,
	releaseYear TEXT,
	duration TEXT,
	listedIn TEXT,
    PRIMARY KEY (id)
); 