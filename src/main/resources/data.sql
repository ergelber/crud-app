INSERT INTO client (
	client_id,
    company,
    website,
    phone,
    mailing
) VALUES (
	1,
    'Cool New Company',
    'coolestcompany.com',
    '123456789',
    '123 Any St. Asheville NC 28801'
), (
	2,
    'Not Cool New Company',
    'notcoolcompany.com',
    '123456789',
    '456 Any St. Asheville NC 28801'
);

INSERT INTO person (
    first_name,
    last_name,
    email_address,
    street_address,
    city,
    state,
    zip_code,
    client_id
) VALUES (
    'John',
    'Smith',
    'fake1@aquent.com',
    '123 Any St.',
    'Asheville',
    'NC',
    '28801',
    1
), (
    'Jane',
    'Smith',
    'fake2@aquent.com',
    '123 Any St.',
    'Asheville',
    'NC',
    '28801',
    1
);


