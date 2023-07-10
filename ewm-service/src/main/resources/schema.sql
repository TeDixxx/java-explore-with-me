
CREATE TABLE IF NOT EXISTS users
(
id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
name VARCHAR(30)                           NOT NULL,
email VARCHAR(55)                          NOT NULL,
CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(55)                            NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation         VARCHAR(2000)                           NOT NULL,
    category_id        BIGINT                                  NOT NULL,
    confirmed_requests BIGINT,
    created_on            TIMESTAMP WITHOUT TIME ZONE          NOT NULL,
    description        VARCHAR(7000)                           NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    initiator_id       BIGINT                                  NOT NULL,
    lat                FLOAT                                   NOT NULL,
    lon                FLOAT                                   NOT NULL,
    paid               BOOLEAN                                 NOT NULL,
    participant_limit  BIGINT                                  NOT NULL,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN                                 NOT NULL,
    state              VARCHAR                                 NOT NULL,
    title              VARCHAR(120)                             NOT NULL,
    views              BIGINT,

   CONSTRAINT pk_events PRIMARY KEY (id),
   CONSTRAINT fk_category_id_events FOREIGN KEY (category_id) REFERENCES categories (id),
   CONSTRAINT fk_initiator_id_events FOREIGN KEY (initiator_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS request
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created      TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    event_id     BIGINT                                  NOT NULL,
    requester_id BIGINT                                  NOT NULL,
    status       VARCHAR(30)                             NOT NULL,
    CONSTRAINT pk_request PRIMARY KEY (id),
    CONSTRAINT fk_requester_id_events FOREIGN KEY (requester_id) REFERENCES users (id),
    CONSTRAINT fk_event_id_events FOREIGN KEY (event_id) REFERENCES events (id),
    UNIQUE (event_id, requester_id)
);