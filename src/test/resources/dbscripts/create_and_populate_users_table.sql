create table if not exists users (
user_id varchar not null,
login varchar not null,
first_name varchar not null,
last_name varchar not null,
primary key (user_id)
);

INSERT INTO users VALUES ('1qwerty', 'user1', 'Artem', 'Kozachenko');
INSERT INTO users VALUES ('2qwerty', 'user2', 'John', 'Smith');
INSERT INTO users VALUES ('3qwerty', 'user3', 'James', 'Donovan');