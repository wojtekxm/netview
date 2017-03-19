INSERT INTO `building` (code, name, latitude, longitude) VALUES
  ('TEST <hr><script>','Budynek <hr><script>',53.01,18.57),
  ('TEST \'&\"<','Budynek \'&\"<',53.02,18.58);

INSERT INTO unit (code,description) VALUES
  ('TEST <hr><script>', 'Jednostka <hr><script>'),
  ('TEST \'&\"<', 'Jednostka \'&\"<');

INSERT INTO link_unit_building (unit_id, building_id) VALUES
  ( (SELECT id FROM unit WHERE code ='TEST <hr><script>'), (SELECT id FROM building WHERE code = 'TEST \'&\"<') ),
  ( (SELECT id FROM unit WHERE code ='TEST \'&\"<'), (SELECT id FROM building WHERE code = 'TEST <hr><script>') );

INSERT INTO controller (`name`, ipv4, description, building_id) VALUES
  ('TEST <hr><script>', '44.44.44.2', 'Kontroler <hr><script>', 10),
  ('TEST \'&\"<', '44.44.44.1', 'Kontroler \'&\"<', 9);

INSERT INTO `user` (name, secret, is_activated, is_blocked, role) VALUES
  ('TEST <hr><script>', NULL, TRUE, FALSE, 'NORMAL'),
  ('TEST \'&\"<', NULL, TRUE, TRUE, 'NORMAL');

DELETE FROM hibernate_sequences;

INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES
  ( "controller", ( SELECT MAX(id)+1000 FROM controller ) ),
  ( "building", ( SELECT MAX(id)+1000 FROM building ) ),
  ( "unit", ( SELECT MAX(id)+1000 FROM unit ) ),
  ( "link_unit_building", ( SELECT MAX(id)+1000 FROM link_unit_building ) ),
  ( "user", ( SELECT MAX(id)+1000 FROM user ) );