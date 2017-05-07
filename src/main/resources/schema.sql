CREATE TABLE client (
    client_id integer IDENTITY,
    company varchar(50) NOT NULL,
    website varchar(50) NOT NULL,
    phone varchar(50) NOT NULL,
    mailing varchar(50) NOT NULL
);

CREATE TABLE person (
    person_id integer IDENTITY,
    first_name varchar(50) NOT NULL,
    last_name varchar(50) NOT NULL,
    email_address varchar(50) NOT NULL,
    street_address varchar(50) NOT NULL,
    city varchar(50) NOT NULL,
    state varchar(2) NOT NULL,
    zip_code varchar(5) NOT NULL,
    client_id integer,
    CONSTRAINT fk_client_id FOREIGN KEY (client_id) REFERENCES client (client_id)
);

CREATE TABLE client_persons (
    person_id integer not null,
    client_id integer not null,
    constraint client_persons_id PRIMARY KEY (person_id,client_id),
    constraint fk_client_persons_persons FOREIGN KEY (person_id) references person (person_id),
    constraint fk_client_persons_client FOREIGN KEY (client_id) references client (client_id)
);
