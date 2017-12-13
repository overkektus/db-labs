CREATE TABLE "groups" (idgroup INTEGER PRIMARY KEY AUTOINCREMENT, faculty TEXT REFERENCES faculty (faculty) ON DELETE CASCADE ON UPDATE CASCADE, course INTEGER CHECK (0 < course < 6) NOT NULL, groupname TEXT NOT NULL, head TEXT UNIQUE);