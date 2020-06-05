CREATE TABLE users (
	id INTEGER PRIMARY KEY,
  username TEXT UNIQUE NOT NULL,
  password TEXT NOT NULL,
  manager BOOLEAN NOT NULL
);

CREATE TABLE clients (
	id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    surname TEXT NOT NULL,
    address TEXT NOT NULL,
    cap INT NOT NULL,
    city TEXT NOT NULL,
    telephone TEXT NOT NULL,
    payment INTEGER,
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

CREATE TABLE managers (
    id INTEGER PRIMARY KEY,
    badge TEXT NOT NULL,
    name TEXT NOT NULL,
    surname TEXT NOT NULL,
    address TEXT NOT NULL,
    cap INT NOT NULL,
    city TEXT NOT NULL,
    telephone TEXT NOT NULL,
    role TEXT NOT NULL,
    user_id INTEGER NOT NULL,
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE
);

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

CREATE TABLE orders (
	id INTEGER PRIMARY KEY,
	total INTEGER NOT NULL,
	payment INTEGER NOT NULL,
	delivery_start INTEGER NOT NULL,
	delivery_end INTEGER NOT NULL,
	state INTEGER NOT NULL,
	user_id INTEGER NOT NULL,
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE order_item (
	id INTEGER PRIMARY KEY,
	name TEXT NOT NULL,
	price INTEGER NOT NULL,
	quantity INTEGER NOT NULL,
	total INTEGER NOT NULL,
	order_id INTEGER NOT NULL,
    FOREIGN KEY(order_id) REFERENCES orders(id) ON DELETE CASCADE ON UPDATE CASCADE
);


------------------------------------------------------------------------------------------------------------------------

INSERT INTO users (username, password, manager)
    VALUES('admin', '$2b$10$swPp91a8qj40VkcBEn704eIFNOQ1Tvwxc2lZlQppIq/VgyLFLfzpS', 1); -- 1:admin:password

INSERT INTO users (username, password, manager)
    VALUES('guest', '$2y$12$34AOvePv2yzpQN9aN0ixD.DGmVUaBjWOLq5PImEo0wCfD3iB89HwK', 0); -- 2:guest:guest

INSERT INTO clients (name, surname, address, cap, city, telephone, payment, user_id)
    VALUES('Name', 'Surname', 'Via Viale 1', 3333, 'Città', '3334445555', 0, 2); -- guest

INSERT INTO managers (badge, name, surname, address, cap, city, telephone, role, user_id)
    VALUES ('D34DB33F', 'Name', 'Surname', 'Via Viale 1', 3333, 'City', '3334445555', 'Admin', 1); -- admin

INSERT INTO products(name, brand, package_size, price, image, availability, characteristics, section_id)
    VALUES ('Product', 'Brand', 1, 1, NULL, 1, 'Characteristics', 1);

INSERT INTO products(name, brand, package_size, price, image, availability, characteristics, section_id)
    VALUES ('Product', 'Brand', 1, 1, NULL, 1, 'Characteristics', 1);

INSERT INTO products(name, brand, package_size, price, image, availability, characteristics, section_id)
    VALUES ('Product', 'Brand', 1, 1, 'https://yt3.ggpht.com/a-/AAuE7mADfh3UcYZrm1JiynJ5CQ3I66fjKULGGQLaIQ=s900-mo-c-c0xffffffff-rj-k-no', 1, 'Characteristics', 1);

