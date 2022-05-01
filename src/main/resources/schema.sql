CREATE TABLE user(
	user_id BIGINT NOT NULL AUTO_INCREMENT,
	user_name VARCHAR(255) NOT NULL,
	email VARCHAR(255) NOT NULL,
	first_name VARCHAR(255) NOT NULL,
	last_name VARCHAR(255) NOT NULL,
	password VARCHAR(255) NOT NULL,
	role VARCHAR(255) NOT NULL,
	last_updated_at date,
	created_at date,
	enabled BOOLEAN,
	PRIMARY KEY(user_id)
);

--table verification_token is auto generated. Class = VerificationToken
--foreign key 'user_id' added automatically by hibernate to table verification_token



--other entities are generated automatically by hibernate