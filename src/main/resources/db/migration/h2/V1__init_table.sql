/* 소셜로그인과 커스텀로그인 공통 계정 정보를 저장할 테이블 */
CREATE TABLE account (
    account_id VARCHAR(255) NOT NULL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    is_locked BOOLEAN NOT NULL,
    dtype VARCHAR(255) NOT NULL
);

/* 커스텀로그인 계정 정보를 저장할 테이블 */
CREATE TABLE custom (
    account_id VARCHAR(255) NOT NULL PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    FOREIGN KEY (account_id) REFERENCES account (account_id)
);

/* oauth 계정 정보를 저장할 테이블 */
CREATE TABLE oauth (
    account_id VARCHAR(255) NOT NULL PRIMARY KEY,
    auth_provider VARCHAR(255) NOT NULL,
    FOREIGN KEY (account_id) REFERENCES account (account_id)
);