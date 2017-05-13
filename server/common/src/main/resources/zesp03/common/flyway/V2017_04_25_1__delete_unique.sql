ALTER TABLE controller DROP INDEX UQ_name_deleted;
ALTER TABLE device DROP INDEX UQ_name_deleted;
ALTER TABLE device_frequency DROP FOREIGN KEY device_frequency_ibfk_1;
ALTER TABLE device_frequency DROP INDEX UQ_device_id_frequency_mhz_deleted;
ALTER TABLE device_frequency ADD INDEX KEY_device_id_frequency_mhz (device_id, frequency_mhz);
ALTER TABLE device_frequency ADD FOREIGN KEY FK_device_frequency_device (device_id) REFERENCES device (id) ON DELETE RESTRICT ON UPDATE RESTRICT;