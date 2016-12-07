CREATE TABLE `controller` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(85) NOT NULL COLLATE 'utf8_bin',
	`ipv4` VARCHAR(15) NOT NULL COLLATE 'ascii_general_ci',
	`description` VARCHAR(1000) COLLATE 'utf8_general_ci',
	PRIMARY KEY (`id`),
	UNIQUE INDEX `unique_name` (`name`)
);

CREATE TABLE `device` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(85) NOT NULL COLLATE 'utf8_bin',
	`is_known` TINYINT(1) NOT NULL,
	`description` VARCHAR(1000) COLLATE 'utf8_general_ci',
	`controller_id` INT(11) NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE INDEX `unique_name` (`name`),
	INDEX `key_controller` (`controller_id`),
	CONSTRAINT `key_controller` FOREIGN KEY (`controller_id`) REFERENCES `controller` (`id`) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE `device_survey` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`timestamp` INT(11) NOT NULL,
	`is_enabled` TINYINT(1) NOT NULL,
	`clients_sum` INT(11) NOT NULL,
	`device_id` INT(11) NOT NULL,
	PRIMARY KEY (`id`),
	INDEX `key_device` (`device_id`),
	CONSTRAINT `key_device` FOREIGN KEY (`device_id`) REFERENCES `device` (`id`) ON UPDATE CASCADE ON DELETE CASCADE
);