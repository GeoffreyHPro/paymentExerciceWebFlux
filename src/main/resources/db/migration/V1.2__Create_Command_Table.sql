CREATE TABLE command (
    id SERIAL primary key,
    productName VARCHAR(255),
	productRef VARCHAR(255),
	quantity integer,
	price numeric(10,2),
	payment_id INTEGER,            
    FOREIGN KEY (payment_id)         
        REFERENCES payment(id)      
        ON DELETE CASCADE 
);