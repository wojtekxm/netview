ALTER TABLE controller DROP FOREIGN KEY controller_ibfk_1;
ALTER TABLE controller DROP INDEX FK_controller_building;
ALTER TABLE controller MODIFY COLUMN deleted BIGINT NOT NULL;
ALTER TABLE controller ADD UNIQUE INDEX UQ_name_deleted (`name`, deleted);
ALTER TABLE controller ADD FOREIGN KEY FK_controller_building (building_id) REFERENCES building (id) ON DELETE SET NULL ON UPDATE SET NULL;

ALTER TABLE device DROP FOREIGN KEY device_ibfk_1;
ALTER TABLE device DROP INDEX FK_device_controller;
ALTER TABLE device DROP COLUMN is_known;
UPDATE device SET description = '' WHERE description IS NULL;
ALTER TABLE device MODIFY COLUMN description VARCHAR(1000) COLLATE 'utf8_general_ci' NOT NULL;
ALTER TABLE device MODIFY controller_id BIGINT;
ALTER TABLE device MODIFY COLUMN deleted BIGINT NOT NULL;
ALTER TABLE device ADD COLUMN building_id BIGINT;
ALTER TABLE device ADD UNIQUE INDEX UQ_name_deleted (`name`, deleted);
ALTER TABLE device ADD FOREIGN KEY FK_device_controller (controller_id) REFERENCES controller (id) ON DELETE SET NULL ON UPDATE SET NULL;
ALTER TABLE device ADD FOREIGN KEY FK_device_building (building_id) REFERENCES building (id) ON DELETE SET NULL ON UPDATE SET NULL;

ALTER TABLE device_frequency DROP FOREIGN KEY device_frequency_ibfk_1;
ALTER TABLE device_frequency DROP INDEX KEY_device_id_frequency_mhz;
ALTER TABLE device_frequency MODIFY COLUMN deleted BIGINT NOT NULL;
ALTER TABLE device_frequency ADD UNIQUE INDEX UQ_device_frequency_deleted (device_id, frequency_mhz, deleted);
ALTER TABLE device_frequency ADD FOREIGN KEY FK_device_frequency_device (device_id) REFERENCES device (id);

ALTER TABLE device_survey DROP FOREIGN KEY device_survey_ibfk_1;
ALTER TABLE device_survey DROP INDEX UQ_frequency_id_timestamp;
ALTER TABLE device_survey DROP INDEX KEY_deleted;
ALTER TABLE device_survey DROP INDEX KEY_timestamp;
ALTER TABLE device_survey MODIFY COLUMN deleted BIGINT NOT NULL;
ALTER TABLE device_survey ADD UNIQUE INDEX UQ_frequency_timestamp_deleted (frequency_id, `timestamp`, deleted);
ALTER TABLE device_survey ADD INDEX KEY_frequency_timestamp (frequency_id, `timestamp`);
ALTER TABLE device_survey ADD INDEX KEY_timestamp (`timestamp`);
ALTER TABLE device_survey ADD INDEX KEY_deleted (deleted);
ALTER TABLE device_survey ADD FOREIGN KEY FK_survey_frequency (frequency_id) REFERENCES device_frequency (id);