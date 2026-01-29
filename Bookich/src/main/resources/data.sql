INSERT INTO users (
    public_id,
    username,
    password,
    first_name,
    last_name,
    phone_number,
    district
)
VALUES (
           '100001',
           'ilnur',
            -- hash, generated using generator WebSite for password 'ilnur089'
           '{bcrypt}$2a$10$6UJu1JgbUMSrcRQ4bDV5LOoZMjcLsKKsahmTTpg8TV60HrbjTrFzy',
           'ilnur',
           'yamaletdinov',
           '+998900000000',
           'SHAYHANTAHUR'
       );

INSERT INTO users (
    public_id,
    username,
    password,
    first_name,
    last_name,
    phone_number,
    district
)
VALUES (
           '100002',
           'bobik',
           -- hash, generated using generator WebSite for password 'ilnur089'
           '{noop}bobik',
           'ilnur',
           'yamaletdinov',
           '+998900000000',
           'SHAYHANTAHUR'
       );


INSERT INTO books (
    owner_id,
    title,
    author,
    genre,
    description,
    condition,
    is_18_plus
) VALUES (
             1,
             'The Midnight Library',
             'Matt Haig',
             'FICTION', -- Assuming 'Fiction' exists in your genre_enum
             'A novel about all the choices that go into a life well lived.',
             'USED_GOOD',
             FALSE
         );

INSERT INTO books (
    owner_id,
    title,
    author,
    genre,
    description,
    condition,
    is_18_plus
) VALUES (
             2,
             'Rich Pepe, Poor Pepe',
             'Gun West',
             'FICTION', -- Assuming 'Fiction' exists in your genre_enum
             'A novel about all the choices that go into a life well lived.',
             'USED_GOOD',
             FALSE
         );


INSERT INTO exchange_requests (
    type,
    status,
    initiator_id,
    receiver_id,
    offered_book_id,
    requested_book_id
) VALUES (
          'SWAP',
          'PENDING',
          2,
          1,
          2,
          1
    );