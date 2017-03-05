DELETE FROM current_survey;
DELETE FROM device_survey;
ALTER TABLE device_survey ADD cumulative BIGINT NOT NULL;
ALTER TABLE device_survey ADD UNIQUE UQ_device_id_timestamp (device_id, `timestamp`);