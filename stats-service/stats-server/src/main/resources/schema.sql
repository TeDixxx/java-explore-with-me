

CREATE TABLE IF NOT EXISTS endpointhit
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    app VARCHAR(255) NOT NULL,
    uri VARCHAR(255) NOT NULL,
    ip VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_endpointhit PRIMARY KEY (id)
);