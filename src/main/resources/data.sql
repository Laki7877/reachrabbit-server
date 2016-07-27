SET FOREIGN_KEY_CHECKS = 0; 
TRUNCATE user; 
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO user (email,password,name,role,createdAt) VALUES ('laki7877@gmail.com','$2a$12$FI2mwDCMCDH76sJ0RnqYF.NA9VMIHxFslRnm8SvFAgYk29fxY75vS','Laki Sik','Admin',current_timestamp);