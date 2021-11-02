CREATE TABLE post (
   id SERIAL PRIMARY KEY,
   name TEXT,
   created DATE
);

CREATE TABLE candidate (
    id SERIAL PRIMARY KEY,
    name TEXT,
    cityid int,
    registered DATE
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name TEXT,
     email TEXT,
     password TEXT
);

CREATE TABLE city (
     id SERIAL PRIMARY KEY,
      name TEXT UNIQUE
);