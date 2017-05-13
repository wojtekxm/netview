ALTER TABLE controller ADD COLUMN deleted BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE device ADD COLUMN deleted BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE device_frequency ADD COLUMN deleted BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE device_survey ADD COLUMN deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE device_frequency DROP FOREIGN KEY device_frequency_ibfk_1;
ALTER TABLE device_survey DROP FOREIGN KEY device_survey_ibfk_1;
ALTER TABLE controller DROP FOREIGN KEY controller_ibfk_1;

DROP INDEX `name` ON controller;
DROP INDEX `KEY_name` ON controller;
DROP INDEX `KEY_ipv4` ON controller;
DROP INDEX `name` ON device;
DROP INDEX `KEY_name` ON device;
DROP INDEX `UQ_device_id_frequency_mhz` ON device_frequency;

CREATE UNIQUE INDEX UQ_name_deleted ON controller (`name`, deleted);
CREATE UNIQUE INDEX UQ_name_deleted ON device (`name`, deleted);
CREATE UNIQUE INDEX UQ_device_id_frequency_mhz_deleted ON device_frequency (device_id, frequency_mhz, deleted);

ALTER TABLE device_frequency ADD FOREIGN KEY FK_device_frequency_device (device_id) REFERENCES device (id) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE device_survey ADD FOREIGN KEY FK_device_survey_device_frequency (frequency_id) REFERENCES device_frequency (id) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE controller ADD FOREIGN KEY FK_controller_building (building_id) REFERENCES building (id) ON DELETE RESTRICT ON UPDATE RESTRICT;
