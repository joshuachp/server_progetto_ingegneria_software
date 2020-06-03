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
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE loyalty_cards (
	id INTEGER PRIMARY KEY,
	card_number INTEGER NOT NULL,
	emission_date INTEGER NOT NULL,
	points INTEGER NOT NULL,
	client_id INTEGER NOT NULL,
	FOREIGN KEY(client_id) REFERENCES clients(id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- TODO: Responsabile reparto
-- CREATE TABLE responsabili ();

CREATE TABLE sections (
	id INTEGER PRIMARY KEY,
	name TEXT NOT NULL
);

CREATE TABLE products (
	id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    brand TEXT NOT NULL,
    package_size INTEGER NOT NULL,
    price INTEGER NOT NULL,
    image TEXT,
    availability INTEGER NOT NULL DEFAULT 0,
    -- TODO: Check more convenient way to use characteristics, maybe another table
    characteristics TEXT,
    section_id INTEGER NOT NULL,
	FOREIGN KEY(section_id) REFERENCES sections(id) ON DELETE CASCADE ON UPDATE CASCADE
);

------------------------------------------------------------------------------------------------------------------------

INSERT INTO users (username, password, responsabile)
    VALUES('admin', '$2b$10$swPp91a8qj40VkcBEn704eIFNOQ1Tvwxc2lZlQppIq/VgyLFLfzpS', 1); -- admin:password

INSERT INTO users (username, password, responsabile)
    VALUES('guest', '$2y$12$34AOvePv2yzpQN9aN0ixD.DGmVUaBjWOLq5PImEo0wCfD3iB89HwK', 0); -- guest:guest
