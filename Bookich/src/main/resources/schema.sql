DROP TABLE IF EXISTS user_reviews CASCADE;
DROP TABLE IF EXISTS exchange_requests CASCADE;
DROP TABLE IF EXISTS wishlists CASCADE;
DROP TABLE IF EXISTS book_images CASCADE;
DROP TABLE IF EXISTS books CASCADE;
DROP TABLE IF EXISTS users CASCADE;

DROP TYPE IF EXISTS district_enum CASCADE;
DROP TYPE IF EXISTS book_condition_enum CASCADE;
DROP TYPE IF EXISTS exchange_status_enum CASCADE;
DROP TYPE IF EXISTS exchange_type_enum CASCADE;
DROP TYPE IF EXISTS genre_enum CASCADE;

CREATE TYPE district_enum AS ENUM (
    'BEKTEMIR', 'CHILANZAR', 'YASHNOBOD', 'YAKKASARAY',
    'MIRZO_ULUGBEK', 'MIRABAD', 'SERGELI', 'SHAYHANTAHUR',
    'OLMAZOR', 'UCHTEPA', 'YUNUSABAD', 'YANGIHAYOT'
);

CREATE TYPE book_condition_enum AS ENUM ('NEW', 'USED_EXCELLENT', 'USED_GOOD', 'USED_POOR');

CREATE TYPE exchange_status_enum AS ENUM (
    'PENDING', 'ACCEPTED', 'REJECTED', 'CANCELLED'
);

CREATE TYPE exchange_type_enum AS ENUM (
    'SWAP', 'GIFT'
);

CREATE TYPE genre_enum AS ENUM (
    'FICTION', 'NON_FICTION', 'MYSTERY', 'THRILLER',
    'SCIENCE_FICTION', 'FANTASY', 'ROMANCE', 'HISTORICAL',
    'HORROR', 'BIOGRAPHY', 'SELF_HELP', 'BUSINESS',
    'TECHNOLOGY', 'CHILDREN', 'TEXTBOOK', 'POETRY',
    'RELIGION', 'OTHER'
);

-- 2. USERS TABLE
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       public_id VARCHAR(6) NOT NULL UNIQUE,

                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL, -- Store BCrypt hash here, NOT plain text
                       first_name VARCHAR(100),
                       last_name VARCHAR(100),
                       avatar_url TEXT,

                       phone_number VARCHAR(20),

                       district district_enum,

                       rating FLOAT DEFAULT 0.0,

                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. BOOKS TABLE
CREATE TABLE books (
                       id BIGSERIAL PRIMARY KEY,
                       owner_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,

                       title VARCHAR(255) NOT NULL,
                       author VARCHAR(255) NOT NULL,
                       genre genre_enum, -- You can make this an Enum too if you want strict genres
                       description TEXT,

                       condition book_condition_enum NOT NULL DEFAULT 'USED_GOOD',

    -- Privacy & State
                       is_18_plus BOOLEAN DEFAULT FALSE,
                       is_active BOOLEAN DEFAULT TRUE, -- Becomes FALSE if currently in an exchange process
                       is_archived BOOLEAN DEFAULT FALSE, -- If user deletes it, we just archive it

                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. BOOK IMAGES (One book can have multiple images)
CREATE TABLE book_images (
                             id BIGSERIAL PRIMARY KEY,
                             book_id BIGINT NOT NULL REFERENCES books(id) ON DELETE CASCADE,

                             image_url TEXT NOT NULL,
                             is_cover BOOLEAN DEFAULT FALSE, -- If true, this is the main thumbnail

                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 5. WISHLIST (What users want)
CREATE TABLE wishlists (
                           id BIGSERIAL PRIMARY KEY,
                           user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                           book_title VARCHAR(255) NOT NULL,
                           book_author VARCHAR(255),
                           genre genre_enum,

                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 6. REQUESTS (The Transaction System)
CREATE TABLE exchange_requests (
                                id BIGSERIAL PRIMARY KEY,

                                type exchange_type_enum NOT NULL,      -- 'SWAP' or 'GIFT'
                                status exchange_status_enum NOT NULL DEFAULT 'PENDING',

                                initiator_id BIGINT NOT NULL REFERENCES users(id),
                                receiver_id BIGINT NOT NULL REFERENCES users(id),

                                offered_book_id BIGINT NOT NULL REFERENCES books(id),
                                requested_book_id BIGINT REFERENCES books(id),

                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 7. REVIEWS (To calculate the rating)
CREATE TABLE user_reviews (
                              id BIGSERIAL PRIMARY KEY,

                              reviewer_id BIGINT NOT NULL REFERENCES users(id),
                              target_user_id BIGINT NOT NULL REFERENCES users(id),
                              transaction_id BIGINT NOT NULL REFERENCES exchange_requests(id), -- Link to the specific exchange

                              rating INT CHECK (rating >= 1 AND rating <= 5),
                              comment TEXT,

                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 8. INDEXES (For Performance)
-- Speed up searching by Title and Author
CREATE INDEX idx_wishlists_title ON wishlists(book_title);
CREATE INDEX idx_books_title ON books(title);
CREATE INDEX idx_books_author ON books(author);
-- Speed up filtering by District (for your "Books in District" requirement)
CREATE INDEX idx_users_district ON users(district);
-- Speed up finding a user by their 6-digit ID
CREATE INDEX idx_users_public_id ON users(public_id);