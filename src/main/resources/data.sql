---- user 테이블에 더미 데이터 추가
--INSERT INTO User(username, email, password, point, level) VALUES ('user2', 'user2@example.com', 'password2', 200, 2);
--INSERT INTO User(username, email, password, point, level) VALUES ('user3', 'user3@example.com', 'password3', 300, 3);
--INSERT INTO User(username, email, password, point, level) VALUES ('user4', 'user4@example.com', 'password4', 400, 4);
--INSERT INTO User(username, email, password, point, level) VALUES ('user5', 'user5@example.com', 'password5', 500, 5);

-- article 테이블에 더미 데이터 추가
INSERT INTO article(title, content) VALUES ('Article 1', 'Content of article 1');
INSERT INTO article(title, content) VALUES ('Article 2', 'Content of article 2');
INSERT INTO article(title, content) VALUES ('Article 3', 'Content of article 3');
INSERT INTO article(title, content) VALUES ('What is your favorite movie?', 'Please leave a comment below 1');
INSERT INTO article(title, content) VALUES ('What is your favorite food?', 'Please leave a comment below 2');
INSERT INTO article(title, content) VALUES ('What is your hobby?', 'Please leave a comment below 3');

-- comment 테이블에 더미 데이터 추가
--INSERT INTO comment(article_id, nickname, body) VALUES (4, 'Park', 'The Shawshank Redemption');
--INSERT INTO comment(article_id, nickname, body) VALUES (4, 'Kim', 'The Godfather');
--INSERT INTO comment(article_id, nickname, body) VALUES (4, 'Choi', 'The Great Gatsby');
--INSERT INTO comment(article_id, nickname, body) VALUES (5, 'Park', 'Pizza');
--INSERT INTO comment(article_id, nickname, body) VALUES (5, 'Kim', 'Hamburger');
--INSERT INTO comment(article_id, nickname, body) VALUES (5, 'Yoon', 'Sushi');
--INSERT INTO comment(article_id, nickname, body) VALUES (6, 'Choi', 'Soccer');
--INSERT INTO comment(article_id, nickname, body) VALUES (6, 'Park', 'Basketball');
--INSERT INTO comment(article_id, nickname, body) VALUES (6, 'Joo', 'Baseball');

-- energy 테이블에 더미 데이터 추가
--INSERT INTO energy(energy_name) VALUES ('전기');

-- energy_used 테이블에 더미 데이터 추가
INSERT INTO energy_used(amount, month, price, user_id, year, energy_id) VALUES (10000, 12, 20102, 1, 2025, 1);
INSERT INTO energy_used(amount, month, price, user_id, year, energy_id) VALUES (15000, 11, 15000, 2, 2025, 1);
INSERT INTO energy_used(amount, month, price, user_id, year, energy_id) VALUES (20000, 10, 20000, 3, 2025, 1);
INSERT INTO energy_used(amount, month, price, user_id, year, energy_id) VALUES (25000, 9, 25000, 4, 2025, 1);
INSERT INTO energy_used(amount, month, price, user_id, year, energy_id) VALUES (30000, 8, 30000, 5, 2025, 1);
