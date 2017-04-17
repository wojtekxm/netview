UPDATE controller SET description='' WHERE description IS NULL;
UPDATE controller SET community_string='' WHERE community_string IS NULL;
ALTER TABLE controller MODIFY description VARCHAR(1000) COLLATE 'utf8_general_ci' NOT NULL;
ALTER TABLE controller MODIFY community_string VARCHAR(255) COLLATE 'utf8_bin' NOT NULL;