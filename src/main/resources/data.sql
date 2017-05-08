INSERT INTO client (
    company,
    website,
    phone,
    mailing
) VALUES (
    'Cool New Company',
    'coolestcompany.com',
    '123456789',
    '123 Any St. Asheville NC 28801'
), (
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
    zip_code
) VALUES (
    'John',
    'Smith',
    'fake1@aquent.com',
    '123 Any St.',
    'Asheville',
    'NC',
    '28801'
), (
    'Jane',
    'Smith',
    'fake2@aquent.com',
    '123 Any St.',
    'Asheville',
    'NC',
    '28801'
);

INSERT INTO client_persons ( 
	person_id, 
	client_id
) VALUES ( 0, 0 ), ( 0, 1 ), ( 1, 1 ), ( 1, 0 )



