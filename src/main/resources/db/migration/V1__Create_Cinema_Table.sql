CREATE TABLE IF NOT EXISTS cinema(
    id int primary key auto_increment,
    uuid VARCHAR(36),
    rowNumber int,
    columnNumber int,
    price int,
    isPurchased boolean
);