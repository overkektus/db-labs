SELECT g.faculty, g.course, g.name, avg(mark) FROM progress p
    INNER JOIN student s ON p.idstudent = s.idstudent
    INNER JOIN groups g ON g.idgroup = s.idgroup
    INNER JOIN subject sb ON p.idsubject = sb.idsubject
WHERE g.faculty like "ФИТ" and g.name like 7