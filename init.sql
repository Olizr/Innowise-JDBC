create table users
(
	id SERIAL PRIMARY KEY,
	username VARCHAR not null,
	password VARCHAR not null,
	mail VARCHAR not null
);

create table roles
(
	id SERIAL PRIMARY KEY,
	name VARCHAR not null,
	description VARCHAR not null
);

create table user_roles
(
	user_id INTEGER,
	role_id INTEGER,
	FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
	FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);