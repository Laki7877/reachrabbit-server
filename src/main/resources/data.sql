SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE brand;
TRUNCATE user;
TRUNCATE bank; 
TRUNCATE media;
TRUNCATE category;
TRUNCATE budget;
TRUNCATE completiontime;
SET FOREIGN_KEY_CHECKS = 1;


INSERT INTO user (email,password,name,role,createdAt) VALUES ('laki7877@gmail.com','$2a$12$FI2mwDCMCDH76sJ0RnqYF.NA9VMIHxFslRnm8SvFAgYk29fxY75vS','Laki Sik','Admin',current_timestamp);
INSERT INTO user (email,password,name,role,createdAt) VALUES ('brand@gmail.com','$2a$12$FI2mwDCMCDH76sJ0RnqYF.NA9VMIHxFslRnm8SvFAgYk29fxY75vS','Laki Sik','Brand',current_timestamp);
INSERT INTO brand (brandName, brandId) VALUES ('My Brand', 2);
INSERT INTO bank (bankId,bankName) VALUES ('002','ธนาคารกรุงเทพ จำกัด (มหาชน)');
INSERT INTO bank (bankId,bankName) VALUES ('004','ธนาคารกสิกรไทย จำกัด (มหาชน)');
INSERT INTO bank (bankId,bankName) VALUES ('005','ธนาคารเอบีเอ็น แอมโร เอ็น.วี.');
INSERT INTO bank (bankId,bankName) VALUES ('006','ธนาคารกรุงไทย จำกัด (มหาชน)');
INSERT INTO bank (bankId,bankName) VALUES ('007','ธนาคารสแตนดาร์ดชาร์เตอร์ด');
INSERT INTO bank (bankId,bankName) VALUES ('008','ธนาคารเชสแมนแฮตตัน');
INSERT INTO bank (bankId,bankName) VALUES ('010','ธนาคารแห่งโตเกียว-มิตซูบิชิ');
INSERT INTO bank (bankId,bankName) VALUES ('011','ธนาคารทหารไทย จำกัด (มหาชน)');
INSERT INTO bank (bankId,bankName) VALUES ('012','ธนาคารไทยทนุ จำกัด (มหาชน)');
INSERT INTO bank (bankId,bankName) VALUES ('013','ธนาคารมหานคร จำกัด (มหาชน)');
INSERT INTO bank (bankId,bankName) VALUES ('014','ธนาคารไทยพาณิชย์ จำกัด (มหาชน)');
INSERT INTO bank (bankId,bankName) VALUES ('015','ธนาคารนครหลวงไทย จำกัด (มหาชน)');
INSERT INTO bank (bankId,bankName) VALUES ('017','ธนาคารซิตี้แบงค์');
INSERT INTO bank (bankId,bankName) VALUES ('018','ธนาคารซากุระ');
INSERT INTO bank (bankId,bankName) VALUES ('019','ธนาคารรัตนสิน');
INSERT INTO bank (bankId,bankName) VALUES ('020','ธนาคารนครธน จำกัด (มหาชน)');
INSERT INTO bank (bankId,bankName) VALUES ('021','ธนาคารศรีนคร จำกัด (มหาชน)');
INSERT INTO bank (bankId,bankName) VALUES ('022','ธนาคารสหธนาคาร จำกัด (มหาชน)');
INSERT INTO bank (bankId,bankName) VALUES ('024','ธนาคารเอเชีย จำกัด (มหาชน)');
INSERT INTO bank (bankId,bankName) VALUES ('025','ธนาคารกรุงศรีอยุธยา จำกัด (มหาชน)');
INSERT INTO bank (bankId,bankName) VALUES ('026','ธนาคารสากลพาณิชย์จีน');
INSERT INTO bank (bankId,bankName) VALUES ('027','ธนาคารแห่งอเมริกา');
INSERT INTO bank (bankId,bankName) VALUES ('030','ธนาคารออมสิน จำกัด (มหาชน)');
INSERT INTO bank (bankId,bankName) VALUES ('031','ธนาคารฮ่องกงและเซี่ยงไฮ้แบงกิ้งคอร์ปอเรชั่น ธนาคารที่เป็นสมาชิกในเดือนพฤษภาคม 2540');
INSERT INTO bank (bankId,bankName) VALUES ('032','ธนาคารดอยซ์แบงค์');
INSERT INTO bank (bankId,bankName) VALUES ('033','ธนาคารอาคารสงเคราะห์ สาขาธนาคารพาณิชย์ในต่างประเทศ');
INSERT INTO media (mediaId,mediaName,isActive) VALUES ('facebook','Facebook',true);
INSERT INTO media (mediaId,mediaName,isActive) VALUES ('google','YouTube',true);
INSERT INTO media (mediaId,mediaName,isActive) VALUES ('instagram','Instagram',true);
INSERT INTO category (categoryName,isActive) VALUES ('Beauty & Fashion',true);
INSERT INTO category (categoryName,isActive) VALUES ('Travel',true);
INSERT INTO category (categoryName,isActive) VALUES ('Health & Fitness',true);
INSERT INTO category (categoryName,isActive) VALUES ('Lifestyle',true);
INSERT INTO category (categoryName,isActive) VALUES ('Food',true);
INSERT INTO category (categoryName,isActive) VALUES ('Cook & Bakery',true);
INSERT INTO category (categoryName,isActive) VALUES ('Music',true);
INSERT INTO category (categoryName,isActive) VALUES ('Gaming',true);
INSERT INTO category (categoryName,isActive) VALUES ('Comedy',true);
INSERT INTO category (categoryName,isActive) VALUES ('DIY',true);
INSERT INTO category (categoryName,isActive) VALUES ('Mom & Kid',true);
INSERT INTO category (categoryName,isActive) VALUES ('Gadget',true);
INSERT INTO category (categoryName,isActive) VALUES ('Educational',true);
INSERT INTO completiontime (completionTime) VALUES ('1 สัปดาห์');
INSERT INTO completiontime (completionTime) VALUES ('2 สัปดาห์');
INSERT INTO completiontime (completionTime) VALUES ('3 สัปดาห์');
INSERT INTO completiontime (completionTime) VALUES ('4 สัปดาห์');


INSERT INTO campaign (title, description, brandId, categoryId, status) VALUES ('My campaign', 'This is my campaign', 2, 1, 'Draft');


INSERT INTO budget (budgetId, fromBudget, toBudget) VALUES (1, 0, 1000);
INSERT INTO budget (budgetId, fromBudget, toBudget) VALUES (2, 1001, 2000);
INSERT INTO budget (budgetId, fromBudget, toBudget) VALUES (3, 2001, 5000);
INSERT INTO budget (budgetId, fromBudget, toBudget) VALUES (4, 5001, 10000);