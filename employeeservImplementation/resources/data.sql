DROP TABLE IF EXISTS employee;
DROP TABLE IF EXISTS address;

CREATE TABLE employee(
  employeeId INT NOT NULL AUTO_INCREMENT,
  firstName VARCHAR(255) NOT NULL,
  lastName VARCHAR(255) NOT NULL,
  dateOfBirth DATE DEFAULT NULL,
  PRIMARY KEY(employeeId),
);

CREATE TABLE address(
  addressId INT NOT NULL AUTO_INCREMENT,
  employeeId INT NOT NULL,
  address1  VARCHAR(255) NOT NULL,
  address2 VARCHAR(255) NULL,
  city VARCHAR(100) NOT  NULL,
  country VARCHAR(100) NOT  NULL,
  state CHAR(2) NOT  NULL,
  zipCode VARCHAR(16) NOT  NULL,
  PRIMARY KEY(addressId),
  CONSTRAINT fk_con_employeeId FOREIGN KEY (employeeId)
    REFERENCES employee (employeeId)
  ON DELETE CASCADE ON UPDATE NO ACTION
);
