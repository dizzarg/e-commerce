CREATE TABLE products (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(30),
  category VARCHAR(30),
  manufacturer VARCHAR(256),
  description VARCHAR(1024),
  img VARCHAR(512),
  quantity INTEGER,
  price INTEGER
);
CREATE TABLE customers (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(30) UNIQUE,
  passwd VARCHAR(512) NOT NULL,
  email VARCHAR(128),
  firstName VARCHAR(100),
  lastName VARCHAR(100),
  address VARCHAR(100),
  phoneNumber VARCHAR(15)
);
CREATE TABLE orders (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  customerUsername VARCHAR(30) NOT NULL,
  products VARCHAR(256),
  dateCreated TIMESTAMP NOT NULL DEFAULT NOW(),
  dateShipped TIMESTAMP
);
CREATE TABLE order_items (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  order_id VARCHAR(30) NOT NULL,
  product_id VARCHAR(30) NOT NULL,
  foreign key (order_id) references orders(id),
  foreign key (product_id) references products(id)
);
CREATE TABLE payments (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  order_id VARCHAR(30) NOT NULL,
  amount INTEGER NOT NULL,
  create_dt TIMESTAMP NOT NULL DEFAULT NOW(),
  foreign key (order_id) references orders(id)
)