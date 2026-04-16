CREATE DATABASE movie_sys;
USE movie_sys;
CREATE TABLE users (
    user_id INT PRIMARY KEY,
    name VARCHAR(50),
    age INT
);
CREATE TABLE movies (
    movie_id INT PRIMARY KEY,
    title VARCHAR(100),
    genre VARCHAR(30)
);
CREATE TABLE ratings (
    user_id INT,
    movie_id INT,
    rating DECIMAL(2,1),
    PRIMARY KEY (user_id, movie_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (movie_id) REFERENCES movies(movie_id)
);
CREATE TABLE watch_history (
    user_id INT,
    movie_id INT,
    watch_date DATE,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (movie_id) REFERENCES movies(movie_id)
);
INSERT INTO users VALUES
(1, 'Asha', 20),
(2, 'Ravi', 22),
(3, 'Kiran', 25),
(4, 'Meena', 19),
(5, 'Arjun', 23);
INSERT INTO movies VALUES
(101, 'Leo', 'Action'),
(102, 'Jailer', 'Action'),
(103, 'Love Today', 'Romance'),
(104, '96', 'Romance'),
(105, 'Vikram', 'Thriller'),
(106, 'Don', 'Comedy'),
(107, 'Master', 'Action');
INSERT INTO ratings VALUES
(1,101,4.5),
(1,103,4.0),
(2,101,5.0),
(2,102,4.5),
(2,105,4.0),
(3,103,4.5),
(3,104,5.0),
(4,106,3.5),
(4,101,4.0),
(5,105,5.0),
(5,107,4.5);
INSERT INTO watch_history VALUES
(1,101,'2026-04-10'),
(1,103,'2026-04-12'),
(2,101,'2026-04-13'),
(2,102,'2026-04-14'),
(3,103,'2026-04-15'),
(3,104,'2026-04-15'),
(4,106,'2026-04-14'),
(5,105,'2026-04-13'),
(5,107,'2026-04-15'),
(1,105,'2026-04-15');
INSERT INTO ratings VALUES
(2,103,4.5),
(2,104,4.0),
(1,102,4.0);
SELECT 
    m.title,
    ROUND(AVG(r.rating), 2) AS avg_rating,
    COUNT(r.rating) AS total_votes
FROM Movies m
JOIN Ratings r ON m.movie_id = r.movie_id
GROUP BY m.movie_id, m.title
HAVING COUNT(r.rating) >= 2
ORDER BY avg_rating DESC, total_votes DESC
LIMIT 10;

SELECT 
    m.genre,
    COUNT(w.movie_id) AS total_watches
FROM Movies m
JOIN Watch_History w ON m.movie_id = w.movie_id
GROUP BY m.genre
ORDER BY total_watches DESC;

SELECT 
    m.title,
    COUNT(w.movie_id) AS recent_views
FROM Movies m
JOIN Watch_History w ON m.movie_id = w.movie_id
WHERE w.watch_date >= CURDATE() - INTERVAL 7 DAY
GROUP BY m.movie_id, m.title
ORDER BY recent_views DESC
LIMIT 5;

SELECT 
    u.name,
    ROUND(AVG(r.rating), 2) AS avg_rating_given,
    COUNT(r.movie_id) AS movies_rated
FROM Users u
JOIN Ratings r ON u.user_id = r.user_id
GROUP BY u.user_id, u.name
ORDER BY avg_rating_given DESC;

SELECT 
    u.name,
    COUNT(w.movie_id) AS movies_watched
FROM Users u
JOIN Watch_History w ON u.user_id = w.user_id
WHERE w.watch_date >= CURDATE() - INTERVAL 3 DAY
GROUP BY u.user_id, u.name
HAVING COUNT(w.movie_id) >= 2;

SELECT DISTINCT m.title
FROM Ratings r1
JOIN Ratings r2 
    ON r1.movie_id = r2.movie_id 
    AND r1.user_id <> r2.user_id
JOIN Movies m ON r2.movie_id = m.movie_id
WHERE r1.user_id = 1
AND r1.rating >= 4
AND r2.rating >= 4
AND r2.movie_id NOT IN (
    SELECT movie_id FROM Ratings WHERE user_id = 1
);