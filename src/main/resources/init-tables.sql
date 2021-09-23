CREATE TABLE `event`
(
    `event_id`     BIGINT NOT NULL AUTO_INCREMENT,
    `date`         DATETIME(6),
    `ticket_price` DOUBLE,
    `title`        VARCHAR(255),
    PRIMARY KEY (`event_id`)
);

CREATE TABLE `user`
(
    `user_id` BIGINT NOT NULL AUTO_INCREMENT,
    `email`   VARCHAR(255),
    `name`    VARCHAR(255),
    PRIMARY KEY (`user_id`)
);

CREATE TABLE `ticket`
(
    `ticket_id`    BIGINT  NOT NULL AUTO_INCREMENT,
    `event_id`     BIGINT,
    `user_id`      BIGINT,
    `category`     VARCHAR(255),
    `place_number` INTEGER NOT NULL,
    PRIMARY KEY (`ticket_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
    FOREIGN KEY (`event_id`) REFERENCES `event` (`event_id`)
);

CREATE TABLE `user_account`
(
    `user_account_id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id`         BIGINT,
    `balance`         DOUBLE NOT NULL,
    PRIMARY KEY (`user_account_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
);