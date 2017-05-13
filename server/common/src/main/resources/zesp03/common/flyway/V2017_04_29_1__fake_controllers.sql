ALTER TABLE controller ADD COLUMN is_fake BOOLEAN;
UPDATE controller SET is_fake = TRUE;
ALTER TABLE controller MODIFY COLUMN is_fake BOOLEAN NOT NULL;