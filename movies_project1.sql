CREATE TABLE IF NOT EXISTS users(
user_id BIGSERIAL PRIMARY KEY,
first_name VARCHAR(100) NOT NULL,
second_name VARCHAR(100) NOT NULL,
phone_number VARCHAR(15),
email VARCHAR(100) UNIQUE NOT NULL,
password VARCHAR(100) NOT NULL,
created_on TIMESTAMPTZ DEFAULT NOW() NOT NULL,
last_login TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS movies (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255),
    genre VARCHAR(100),
    year INTEGER
);

 CREATE TABLE IF NOT EXISTS reviews (
    id BIGSERIAL PRIMARY KEY,
    movie_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    rating DOUBLE CHECK(rating >= 0 AND rating <= 10),
    review TEXT,
	 FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE,
	 FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);


