CREATE SEQUENCE my_sequence
START WITH 1
INCREMENT BY 1
NOCACHE -- You can use CACHE for performance optimization
NOCYCLE;

CREATE TABLE users (
    user_id INT,
    username VARCHAR2(50) NOT NULL UNIQUE,
    password_hash VARCHAR2(255) NOT NULL,
    email VARCHAR2(100) NOT NULL UNIQUE,
    contact_number VARCHAR2(15),
    PRIMARY KEY(user_id),
    role VARCHAR2(10) DEFAULT 'user', -- DEFAULT value and CHECK constraint separated
    CHECK (role IN ('admin', 'user')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE OR REPLACE TRIGGER my_trigger
BEFORE INSERT ON users
FOR EACH ROW
WHEN (NEW.user_id IS NULL)  -- Only trigger if the user_id is not provided
BEGIN
  :NEW.user_id := my_sequence.NEXTVAL; -- Set the user_id from the sequence
END;
/
select * from users;
CREATE SEQUENCE auction_sequence
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;
CREATE TABLE auctions (
    auction_id INT,  -- Removed AUTO_INCREMENT
    user_id INT,     -- Seller
    item_title VARCHAR2(100) NOT NULL,
    description CLOB,  -- Use CLOB for larger text
    start_price DECIMAL(10, 2) NOT NULL,
    current_price DECIMAL(10, 2) DEFAULT 0,
    end_time TIMESTAMP NOT NULL,
    PRIMARY KEY(auction_id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
CREATE OR REPLACE TRIGGER auction_trigger
BEFORE INSERT ON auctions
FOR EACH ROW
WHEN (NEW.auction_id IS NULL)  -- Only trigger if the auction_id is not provided
BEGIN
  :NEW.auction_id := auction_sequence.NEXTVAL; -- Set the auction_id from the sequence
END;
/

select * from USERS;
drop table users;
drop table auctions;
