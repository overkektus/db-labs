SELECT g.faculty, g.course, g.name, s.name "Имя студента", avg(mark), sb.subject FROM progress p
    INNER JOIN student s ON p.idstudent = s.idstudent
    INNER JOIN "group" g ON g.idgroup = s.idgroup
    INNER JOIN subject sb ON p.idsubject = sb.idsubject
WHERE g.faculty like "ФИТ" and g.name like 7 and sb.subject like 'Криптография'