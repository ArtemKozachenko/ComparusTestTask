create table if not exists user_data (
id varchar not null,
ldap_login varchar not null,
name varchar not null,
surname varchar not null,
primary key (id)
);

INSERT INTO user_data VALUES ('4qwerty', 'user4', 'Harry', 'Potter');
INSERT INTO user_data VALUES ('5qwerty', 'user5', 'Artem', 'Sidorov');
INSERT INTO user_data VALUES ('6qwerty', 'user6', 'Richard', 'Hendricks');