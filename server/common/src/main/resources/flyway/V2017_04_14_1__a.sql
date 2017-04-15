DELETE FROM controller WHERE `name`='TEST <hr><script>';
DELETE FROM controller WHERE `name`='TEST \'&\"<';
UPDATE controller SET building_id=NULL WHERE id IN (1,2);
UPDATE controller SET community_string='abc' WHERE id=3;
UPDATE controller SET community_string='1234!@#$' WHERE id=4;
UPDATE controller SET description='' WHERE description IS NULL;
UPDATE controller SET community_string='' WHERE community_string IS NULL;
ALTER TABLE controller MODIFY description VARCHAR(1000) COLLATE 'utf8_general_ci' NOT NULL;
ALTER TABLE controller MODIFY community_string VARCHAR(255) COLLATE 'utf8_bin' NOT NULL;