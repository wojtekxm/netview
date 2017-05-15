ALTER TABLE building ADD COLUMN updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
ALTER TABLE building ADD COLUMN created_at DATETIME DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE controller ADD COLUMN updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
ALTER TABLE controller ADD COLUMN created_at DATETIME DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE device ADD COLUMN updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
ALTER TABLE device ADD COLUMN created_at DATETIME DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE device_frequency ADD COLUMN updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
ALTER TABLE device_frequency ADD COLUMN created_at DATETIME DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE unit ADD COLUMN updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
ALTER TABLE unit ADD COLUMN created_at DATETIME DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE user ADD COLUMN updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
ALTER TABLE user ADD COLUMN created_at DATETIME DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE token MODIFY COLUMN action VARCHAR(255) NOT NULL COLLATE 'utf8_bin';
ALTER TABLE token ADD COLUMN created_at DATETIME DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE token ADD COLUMN expires DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;