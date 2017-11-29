-- Таблица: students
CREATE TABLE students (idgroup INTEGER REFERENCES groups (idgroups) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL, idstudent INTEGER PRIMARY KEY, name TEXT NOT NULL);