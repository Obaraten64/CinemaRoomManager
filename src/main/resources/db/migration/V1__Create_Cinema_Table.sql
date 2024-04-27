CREATE TABLE IF NOT EXISTS cinema(
    id int primary key,
    uuid VARCHAR(36),
    rowNumber int,
    columnNumber int,
    price int,
    isPurchased boolean
);