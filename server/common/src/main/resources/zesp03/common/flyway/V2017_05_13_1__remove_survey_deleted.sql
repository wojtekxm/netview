ALTER TABLE device_survey DROP FOREIGN KEY device_survey_ibfk_1;
ALTER TABLE device_survey DROP INDEX UQ_frequency_timestamp_deleted;
ALTER TABLE device_survey DROP INDEX KEY_deleted;
ALTER TABLE device_survey DROP INDEX KEY_frequency_timestamp;
ALTER TABLE device_survey DROP INDEX KEY_timestamp;
ALTER TABLE device_survey DROP COLUMN deleted;
ALTER TABLE device_survey ADD UNIQUE INDEX UQ_frequency_timestamp (frequency_id, `timestamp`);
ALTER TABLE device_survey ADD INDEX KEY_timestamp (`timestamp`);
ALTER TABLE device_survey ADD FOREIGN KEY FK_survey_frequency (frequency_id) REFERENCES device_frequency (id);

DROP VIEW view_last_survey;
CREATE VIEW view_last_survey AS
  SELECT device_frequency.id AS frequency_id,
         filtered.id AS survey_id,
         device_frequency.frequency_mhz AS frequency_mhz,
         filtered.`timestamp` AS `timestamp`,
         filtered.is_enabled AS is_enabled,
         filtered.clients_sum AS clients_sum
  FROM device_frequency LEFT JOIN
    ( SELECT ds.id, ds.frequency_id, ds.`timestamp`, ds.is_enabled, ds.clients_sum
      FROM device_survey ds INNER JOIN
        ( SELECT frequency_id, MAX(`timestamp`) AS max_timestamp FROM device_survey GROUP BY frequency_id )
        best ON ds.frequency_id = best.frequency_id AND ds.`timestamp` = best.max_timestamp )
    filtered ON device_frequency.id = filtered.frequency_id;

DROP VIEW view_token;
CREATE VIEW view_token AS SELECT id, HEX(MID(secret,5,1)) AS short_secret, action, user_id, created_at, expires FROM token;

DROP VIEW view_user;
CREATE VIEW view_user AS SELECT id, `name`, HEX(MID(secret,5,1)) AS short_secret, is_activated, is_blocked, role, updated_at, created_at, last_access FROM `user`;

CREATE VIEW view_valid_tokens AS
  SELECT
    token.id,
    HEX(MID(token.secret,5,1)) AS short_secret,
    token.action,
    token.user_id,
    token.created_at,
    token.expires,
    user.name AS user_name,
    user.last_access AS user_last_access
  FROM token LEFT JOIN user ON token.user_id=user.id WHERE token.expires > NOW();