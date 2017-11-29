SELECT g.name, s.name, avg(p.mark) FROM student s
    INNER JOIN progress p ON p.idstudent = s.idstudent
    INNER JOIN "group" g ON g.idgroup = s.idgroup
    INNER JOIN faculty f ON f.faculty = g.faculty
    WHERE f.faculty like 'ФИТ'
    LIMIT 10