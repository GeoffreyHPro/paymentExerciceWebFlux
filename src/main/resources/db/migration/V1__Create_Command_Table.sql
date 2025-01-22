CREATE TABLE command (
    id SERIAL,
    productName VARCHAR(255),
	productRef VARCHAR(255),
	quantity integer,
	price numeric,
    constraint user_pk primary key (id)
);