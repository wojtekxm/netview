ALTER TABLE device_survey DROP column cumulative;
ALTER TABLE controller ADD COLUMN community_string VARCHAR(255) COLLATE 'utf8_bin';