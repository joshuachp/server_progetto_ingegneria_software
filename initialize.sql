CREATE TABLE users (
	id INTEGER PRIMARY KEY,
  username TEXT UNIQUE NOT NULL,
  password TEXT NOT NULL,
  responsabile BOOLEAN NOT NULL
);
CREATE TABLE clients (
	id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    surname TEXT NOT NULL,
    address TEXT NOT NULL,
    cap INT NOT NULL,
    city TEXT NOT NULL,
    telephone TEXT NOT NULL,
    user_id INTEGER NOT NULL,
    FOREIGN KEY(user_id) REFERENCES users(id)
);
INSERT INTO users (username, password, responsabile)
    VALUES('admin', '$2b$10$swPp91a8qj40VkcBEn704eIFNOQ1Tvwxc2lZlQppIq/VgyLFLfzpS', 1); -- admin:password
INSERT INTO users (username, password, responsabile)
    VALUES('guest', '$2y$12$34AOvePv2yzpQN9aN0ixD.DGmVUaBjWOLq5PImEo0wCfD3iB89HwK', 0); -- guest:guest
