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
    payment INTEGER DEFAULT 0 NOT NULL,
    user_id INTEGER NOT NULL,
    loyalty_card_number INTEGER,
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY(loyalty_card_number) REFERENCES loyalty_cards(card_number) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE loyalty_cards (
	id INTEGER PRIMARY KEY,
	card_number INTEGER UNIQUE NOT NULL,
	emission_date INTEGER NOT NULL,
	points INTEGER NOT NULL
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
	name TEXT NOT NULL UNIQUE
);

CREATE TABLE products (
	id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    brand TEXT NOT NULL,
    package_size INTEGER NOT NULL,
    price REAL NOT NULL,
    image TEXT,
    availability INTEGER NOT NULL DEFAULT 0,
    characteristics TEXT,
    section INTEGER NOT NULL,
	FOREIGN KEY(section) REFERENCES sections(name) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE orders (
	id INTEGER PRIMARY KEY,
	total REAL NOT NULL,
	payment INTEGER NOT NULL,
	delivery_start INTEGER NOT NULL,
	delivery_end INTEGER NOT NULL,
	state INTEGER NOT NULL,
	address TEXT NOT NULL,
	user_id INTEGER NOT NULL,
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE order_items (
	id INTEGER PRIMARY KEY,
	name TEXT NOT NULL,
	price REAL NOT NULL,
	quantity INTEGER NOT NULL,
	product_id INTEGER NOT NULL,
	order_id INTEGER NOT NULL,
    FOREIGN KEY(product_id) REFERENCES products(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY(order_id) REFERENCES orders(id) ON DELETE CASCADE ON UPDATE CASCADE
);

------------------------------------------------------------------------------------------------------------------------

BEGIN TRANSACTION;

INSERT INTO users (username, password, manager)
    VALUES('admin', '$2b$10$swPp91a8qj40VkcBEn704eIFNOQ1Tvwxc2lZlQppIq/VgyLFLfzpS', 1); -- 1:admin:password

INSERT INTO users (username, password, manager)
    VALUES('guest', '$2y$12$34AOvePv2yzpQN9aN0ixD.DGmVUaBjWOLq5PImEo0wCfD3iB89HwK', 0); -- 2:guest:guest

INSERT INTO managers (badge, name, surname, address, cap, city, telephone, role, user_id)
    VALUES ('D34DB33F', 'Name', 'Surname', 'Via Viale 1', 33333, 'City', '3334445555', 'Admin', 1); -- admin

INSERT INTO loyalty_cards (card_number, emission_date, points)
    VALUES (1234, 0, 500);

INSERT INTO clients (name, surname, address, cap, city, telephone, payment, user_id, loyalty_card_number)
    VALUES('Name', 'Surname', 'Via Viale 1', 33333, 'City', '3334445555', 0, 2, 1234); -- guest

INSERT INTO sections(name) VALUES("Section 1");

INSERT INTO sections(name) VALUES("Section 2");

INSERT INTO products(name, brand, package_size, price, image, availability, characteristics, section)
    VALUES ('Mascara', 'Brand', 1, 1.50,  'http://localhost:8080/images/mascara.jpg', 1, 'Characteristics', 'Cosmetici');

INSERT INTO products(name, brand, package_size, price, image, availability, characteristics, section)
    VALUES ('Latte', 'Brand', 1, 2.30, NULL, 0, 'Characteristics', 'Alimentari');

INSERT INTO products(name, brand, package_size, price, image, availability, characteristics, section)
    VALUES ('Broccoli', 'Brand', 1, 5.20, 'http://localhost:8080/images/broccoli.jpg', 3, 'Characteristics', 'Alimentari');

INSERT INTO products(name, brand, package_size, price, image, availability, characteristics, section)
    VALUES ('Mele', 'Granny Smith', 4, 1.98, 'https://images.services.esselunga.it/html/img_prodotti/esselunga/medium2/761346.jpg', 10, 'Bio',
    'Frutta e verdura');

INSERT INTO products(name, brand, package_size, price, image, availability, characteristics, section)
    VALUES ('Coste confezionate', 'Naturama', 10, 1.98, 'https://images.services.esselunga.it/html/img_prodotti/esselunga/medium2/763276.jpg', 10, '',
    'Frutta e verdura');

INSERT INTO products(name, brand, package_size, price, image, availability, characteristics, section)
    VALUES ('Cavolfiore', 'Esse...lunga', 1, 1.58, 'https://images.services.esselunga.it/html/img_prodotti/esselunga/medium2/763187.jpg', 10, 'OGM',
    'Frutta e verdura');

INSERT INTO products(name, brand, package_size, price, image, availability, characteristics, section)
    VALUES ('Filetto di tonno', 'Pinne gialle', 2, 7.68, 'https://images.services.esselunga.it/html/img_prodotti/esselunga/medium2/731307.jpg', 10, 'Pesce fresco',
    'Pesce');

INSERT INTO products(name, brand, package_size, price, image, availability, characteristics, section)
    VALUES ('Orata Eviscerata', 'Esse...lunga', 4, 8.33, 'https://images.services.esselunga.it/html/img_prodotti/esselunga/medium2/731005.jpg', 10, 'Pesce fresco',
    'Pesce');

INSERT INTO products(name, brand, package_size, price, image, availability, characteristics, section)
    VALUES ('Filetto di salmone', 'Esse...lunga', 2, 8.36, 'https://images.services.esselunga.it/html/img_prodotti/esselunga/medium2/731228.jpg', 10, 'Pesce fresco',
    'Pesce');

INSERT INTO products(name, brand, package_size, price, image, availability, characteristics, section)
    VALUES ('Scottona polpa trita', 'Naturama', 1, 4.40, 'https://images.services.esselunga.it/html/img_prodotti/esselunga/medium2/707923.jpg', 10, 'halal',
    'Carne');

INSERT INTO products(name, brand, package_size, price, image, availability, characteristics, section)
    VALUES ('Fesa di Tacchino', 'Esse...lunga', 1, 4.40, 'https://images.services.esselunga.it/html/img_prodotti/esselunga/medium2/708303.jpg', 10, 'pollo',
    'Carne');

INSERT INTO orders(total, payment, delivery_start, delivery_end, state, address, user_id)
    VALUES (3.80, 0, 0, 0, 0, 'Via Viale 1, 33333, City', 2);

INSERT INTO order_items(name, price, quantity, product_id, order_id)
    VALUES ('Mascara', 1.50, 1, 1, 1);

INSERT INTO order_items(name, price, quantity, product_id, order_id)
    VALUES ('Latte', 2.30, 2, 2, 1);

END TRANSACTION;
