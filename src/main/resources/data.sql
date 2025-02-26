INSERT INTO article(title, content) VALUES ('Article 1', 'Content of article 1');
INSERT INTO article(title, content) VALUES ('Article 2', 'Content of article 2');
INSERT INTO article(title, content) VALUES ('Article 3', 'Content of article 3');
INSERT INTO article(title, content) VALUES ('What is your favorite movie?', 'Please leave a comment below 1');
INSERT INTO article(title, content) VALUES ('What is your favorite food?', 'Please leave a comment below 2');
INSERT INTO article(title, content) VALUES ('What is your hobby?', 'Please leave a comment below 3');


INSERT INTO comment(id, article_id, nickname, body) VALUES (1, 4, 'Park', 'The Shawshank Redemption');
INSERT INTO comment(id, article_id, nickname, body) VALUES (2,4, 'Kim', 'The Godfather');
INSERT INTO comment(id, article_id, nickname, body) VALUES (3, 4, 'Choi', 'The Great Gatsby');
INSERT INTO comment(id, article_id, nickname, body) VALUES (4, 5, 'Park', 'Pizza');
INSERT INTO comment(id, article_id, nickname, body) VALUES (5, 5, 'Kim', 'Hamburger');
INSERT INTO comment(id, article_id, nickname, body) VALUES (6, 5, 'Yoon', 'Sushi');
INSERT INTO comment(id, article_id, nickname, body) VALUES (7, 6, 'Choi', 'Soccer');
INSERT INTO comment(id, article_id, nickname, body) VALUES (8, 6, 'Park', 'Basketball');
INSERT INTO comment(id, article_id, nickname, body) VALUES (9, 6, 'Joo', 'Baseball');