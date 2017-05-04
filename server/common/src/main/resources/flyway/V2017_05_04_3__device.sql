ALTER TABLE device DROP COLUMN is_known;
ALTER TABLE device MODIFY COLUMN description VARCHAR(1000) COLLATE 'utf8_general_ci' NOT NULL;
ALTER TABLE device ADD COLUMN building_id BIGINT;
ALTER TABLE device ADD FOREIGN KEY FK_device_building (building_id) REFERENCES building (id) ON DELETE SET NULL ON UPDATE SET NULL;