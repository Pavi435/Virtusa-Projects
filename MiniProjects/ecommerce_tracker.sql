CREATE DATABASE ss;
USE ss;
CREATE TABLE pt (
    pid INT PRIMARY KEY AUTO_INCREMENT,
    pname VARCHAR(30)
);
CREATE TABLE sh (
    sid INT PRIMARY KEY AUTO_INCREMENT,
    pid INT,
    dcity VARCHAR(30),
    pdate DATE,
    adate DATE,
    stat VARCHAR(15),
    odate DATE,
    FOREIGN KEY (pid) REFERENCES pt(pid)
);
CREATE TABLE lg (
    lid INT PRIMARY KEY AUTO_INCREMENT,
    sid INT,
    msg VARCHAR(50),
    ldate DATE,
    FOREIGN KEY (sid) REFERENCES sh(sid)
);
INSERT INTO pt (pname) VALUES
('FT'), ('QM'), ('SN');
INSERT INTO sh (pid, dcity, pdate, adate, stat, odate) VALUES
(1, 'Chn', '2026-04-01', '2026-04-02', 'del', '2026-03-28'),
(2, 'Cbe', '2026-04-05', '2026-04-04', 'del', '2026-03-30'),
(1, 'Chn', '2026-04-03', '2026-04-06', 'del', '2026-04-01'),
(3, 'Mdu', '2026-04-02', '2026-04-02', 'ret', '2026-03-29'),
(2, 'Chn', '2026-04-07', '2026-04-10', 'del', '2026-04-02');
SELECT sid, pid, pdate, adate
FROM sh
WHERE adate > pdate;

SELECT 
    pid,
    SUM(stat = 'del') AS success,
    SUM(stat = 'ret') AS returned
FROM sh
GROUP BY pid;

SELECT dcity, COUNT(*) AS total
FROM sh
WHERE odate >= CURDATE() - INTERVAL 30 DAY
GROUP BY dcity
ORDER BY total DESC
LIMIT 1;

SELECT 
    pid,
    COUNT(*) AS total,
    SUM(adate > pdate) AS delays
FROM sh
GROUP BY pid
ORDER BY delays ASC;