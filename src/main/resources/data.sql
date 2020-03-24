DROP TABLE IF EXISTS clients;
 
CREATE TABLE clients (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  risk_profile VARCHAR(10) NOT NULL
);
 
INSERT INTO clients (risk_profile) VALUES
  ('NORMAL'),
  ('LOW'),
  ('NORMAL'),
  ('HIGH'),
  ('HIGH'),
  ('LOW'),
  ('LOW'),
  ('NORMAL'),
  ('HIGH');