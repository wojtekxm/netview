ALTER TABLE device DROP FOREIGN KEY device_ibfk_1;
ALTER TABLE device MODIFY controller_id BIGINT;
ALTER TABLE device ADD FOREIGN KEY FK_device_controller (controller_id) REFERENCES controller (id) ON DELETE SET NULL ON UPDATE SET NULL;