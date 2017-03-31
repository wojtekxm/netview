INSERT INTO controller (`name`, ipv4, description, building_id) VALUES
  ('TEST <hr><script>', '44.44.44.2', 'Kontroler <hr><script>', 10),
  ('TEST \'&\"<', '44.44.44.1', 'Kontroler \'&\"<', 9);

DELETE FROM hibernate_sequences;

INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES
  ( "controller", ( SELECT MAX(id)+1000 FROM controller ) );