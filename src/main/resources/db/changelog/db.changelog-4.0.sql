
--liquibase formatted sql

--changeset dmatveyenka:1
ALTER TABLE users
    ADD COLUMN password VARCHAR(128) DEFAULT '{noop}123';
-- Указываем размер 128 потому что это будет закодированное значение и оно будет большое
-- Перед дефолтным паролем ставим префикс - какой именно механизм шифрации пароля будет использован
-- noop - no operation - это значит что мы не используем шифрацию паролей

--changeset dmatveyenka:2
ALTER TABLE users_aud
    ADD COLUMN password VARCHAR(128);