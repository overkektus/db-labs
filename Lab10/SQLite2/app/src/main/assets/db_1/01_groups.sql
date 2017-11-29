-- Таблица: groups
CREATE TABLE groups (idgroups INTEGER PRIMARY KEY AUTOINCREMENT, faculty TEXT UNIQUE NOT NULL, course NOT NULL CHECK (0 < course < 6), name TEXT NOT NULL UNIQUE, head UNIQUE);
