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
  products VARCHAR(256) NOT NULL,
  dateCreated TIMESTAMP NOT NULL,
  dateShipped TIMESTAMP
)