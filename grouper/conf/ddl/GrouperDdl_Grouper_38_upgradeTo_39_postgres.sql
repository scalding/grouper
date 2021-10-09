ALTER TABLE GROUPER_PASSWORD_RECENTLY_USED ADD COLUMN attempt_millis BIGINT NOT NULL;
COMMENT ON COLUMN grouper_password_recently_used.attempt_millis IS 'millis since 1970 this password was attempted';

ALTER TABLE GROUPER_PASSWORD_RECENTLY_USED ADD COLUMN ip_address VARCHAR(20) NOT NULL;
COMMENT ON COLUMN grouper_password_recently_used.ip_address IS 'ip address from where the password was attempted';

ALTER TABLE GROUPER_PASSWORD_RECENTLY_USED ADD COLUMN status CHAR(1) NOT NULL;
COMMENT ON COLUMN grouper_password_recently_used.status IS 'status of the attempt. S/F/E etc';

ALTER TABLE GROUPER_PASSWORD_RECENTLY_USED ADD COLUMN hibernate_version_number BIGINT NOT NULL;
COMMENT ON COLUMN grouper_password_recently_used.hibernate_version_number IS 'hibernate version number';

ALTER TABLE GROUPER_PASSWORD_RECENTLY_USED ALTER COLUMN jwt_jti TYPE VARCHAR(100);
ALTER TABLE GROUPER_PASSWORD_RECENTLY_USED ALTER COLUMN jwt_iat TYPE INTEGER;

ALTER TABLE grouper_password DROP COLUMN recent_source_addresses;
ALTER TABLE grouper_password DROP COLUMN failed_source_addresses;
ALTER TABLE grouper_password DROP COLUMN failed_logins;

update grouper_ddl set last_updated = to_char(current_timestamp, 'YYYY/MM/DD HH12:MI:SS'), history = substring((to_char(current_timestamp, 'YYYY/MM/DD HH12:MI:SS') || ': upgrade Grouper from V' || db_version || ' to V39, ' || history) from 1 for 3500), db_version = 39 where object_name = 'Grouper';
commit;
