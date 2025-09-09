CREATE TABLE IF NOT EXISTS "users" (
  "userId" integer PRIMARY KEY,
  "email" varchar UNIQUE,
  "login" varchar UNIQUE NOT NULL,
  "name" varchar,
  "birthday" date
);

CREATE TABLE IF NOT EXISTS "friendships" (
  "friendFrom" integer NOT NULL,
  "friendTo" integer NOT NULL,
  "acceptanceStatus" bool DEFAULT false,
  PRIMARY KEY ("friendFrom", "friendTo")
);

CREATE TABLE IF NOT EXISTS "films" (
  "filmId" integer PRIMARY KEY,
  "name" varchar NOT NULL,
  "description" text,
  "releaseDate" date,
  "duration" integer
);

CREATE TABLE IF NOT EXISTS "genres" (
  "genreId" integer PRIMARY KEY,
  "genre" varchar UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS "filmsGenres" (
  "filmId" integer NOT NULL,
  "genreId" integer NOT NULL,
  PRIMARY KEY ("filmId", "genreId")
);

CREATE TABLE IF NOT EXISTS "mpaRatings" (
  "ratingId" integer PRIMARY KEY,
  "rating" varchar UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS "filmsMpaRatings" (
  "filmId" integer NOT NULL,
  "ratingId" integer NOT NULL,
  PRIMARY KEY ("filmId", "ratingId")
);

CREATE TABLE IF NOT EXISTS "likes" (
  "userId" integer NOT NULL,
  "filmId" integer NOT NULL,
  PRIMARY KEY ("userId", "filmId")
);

ALTER TABLE "friendships" ADD FOREIGN KEY ("friendFrom") REFERENCES "users" ("userId") ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE "friendships" ADD FOREIGN KEY ("friendTo") REFERENCES "users" ("userId") ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE "filmsGenres" ADD FOREIGN KEY ("filmId") REFERENCES "films" ("filmId") ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE "filmsGenres" ADD FOREIGN KEY ("genreId") REFERENCES "genres" ("genreId") ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE "filmsMpaRatings" ADD FOREIGN KEY ("filmId") REFERENCES "films" ("filmId") ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE "filmsMpaRatings" ADD FOREIGN KEY ("ratingId") REFERENCES "mpaRatings" ("ratingId") ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE "likes" ADD FOREIGN KEY ("userId") REFERENCES "users" ("userId") ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE "likes" ADD FOREIGN KEY ("filmId") REFERENCES "films" ("filmId") ON DELETE CASCADE ON UPDATE CASCADE;
