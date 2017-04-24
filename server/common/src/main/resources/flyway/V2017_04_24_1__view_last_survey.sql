CREATE VIEW view_last_survey AS
  SELECT device_frequency.id AS frequency_id,
    filtered.id AS survey_id,
    filtered.`timestamp` AS `timestamp`,
    filtered.is_enabled AS is_enabled,
    filtered.clients_sum AS clients_sum,
    filtered.deleted AS deleted
  FROM device_frequency LEFT JOIN
    ( SELECT ds.id, ds.frequency_id, ds.`timestamp`, ds.is_enabled, ds.clients_sum, ds.deleted
      FROM device_survey ds INNER JOIN
        ( SELECT frequency_id, MAX(`timestamp`) AS max_timestamp FROM device_survey GROUP BY frequency_id )
        best ON ds.frequency_id = best.frequency_id AND ds.`timestamp` = best.max_timestamp )
    filtered ON device_frequency.id = filtered.frequency_id;