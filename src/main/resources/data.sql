-- Insert sample Stores
INSERT INTO store (name) VALUES ('Electronics Hub');
INSERT INTO store (name) VALUES ('Book Haven');

-- Insert sample Products
-- Note: The "store_id" values here assume that the first store inserted gets ID 1 and the second gets ID 2.
INSERT INTO product (name, description, price, inventory, store_id) VALUES
                                                                        ('Laptop', 'High-performance laptop', 1200.00, 50, 1),
                                                                        ('Smartphone', 'Latest model smartphone', 800.00, 100, 1),
                                                                        ('Novel A', 'Bestselling novel', 15.99, 200, 2),
                                                                        ('Novel B', 'Critically acclaimed book', 20.99, 150, 2);

-- Insert sample Users (table name is "users" as defined by @Table(name = "users"))
INSERT INTO users (username, password, first_name, last_name, email) VALUES
                                                                       ('alice', 'password123', 'Alice', 'Johnson', 'alice@example.com'),
                                                                       ('bob', 'password123', 'Bob', 'Williams', 'bob@example.com');
