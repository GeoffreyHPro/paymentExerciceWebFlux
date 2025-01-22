CREATE TABLE payment (
    id SERIAL,
    amount numeric,
	currency VARCHAR(255),
	paymentMeans VARCHAR(255),
	paymentStatus VARCHAR(255),
    constraint payment_pk primary key (id)
);