-- CREATE DATABASE carRentalDB;

DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS rental;
DROP TABLE IF EXISTS inventory;
DROP TABLE IF EXISTS vehicle;
DROP TABLE IF EXISTS member;

CREATE TABLE vehicle(
	id INT AUTO_INCREMENT PRIMARY KEY,
    brand VARCHAR(50),
    model VARCHAR(50),
    colour VARCHAR(50)
);

CREATE TABLE inventory(
	id INT AUTO_INCREMENT PRIMARY KEY,
    vehicleId INT,
    barcode VARCHAR(50),
    parkingStallNum VARCHAR(50),
    rateOfRental DOUBLE,
    status VARCHAR(50),
    
    FOREIGN KEY (vehicleId) REFERENCES vehicle(id)
);

CREATE TABLE member(
	id INT AUTO_INCREMENT PRIMARY KEY,
    firstName VARCHAR(50),
    lastName VARCHAR(50)
);

CREATE TABLE rental(
	id INT AUTO_INCREMENT PRIMARY KEY,
    inventoryId INT,
    memberId INT,
    startDate DATE,
    dueDate DATE,
    rentalFee DOUBLE,
    lateFee DOUBLE,
    isReturned BOOLEAN,
    
    FOREIGN KEY (inventoryId) REFERENCES inventory(id),
    FOREIGN KEY (memberId) REFERENCES member(id)
);

CREATE TABLE reservation(
	id INT AUTO_INCREMENT PRIMARY KEY,
    inventoryId INT NOT NULL,
    memberId INT NOT NULL,
    startDate DATE,
    endDate DATE,
    
    FOREIGN KEY (inventoryId) REFERENCES inventory(id),
    FOREIGN KEY (memberId) REFERENCES member(id)
);
