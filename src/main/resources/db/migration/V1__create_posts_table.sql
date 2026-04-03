-- Create posts table
CREATE TABLE IF NOT EXISTS posts (
    id INTEGER PRIMARY KEY,
    text TEXT,
    by VARCHAR(255),
    score INTEGER,
    time INTEGER,
    title VARCHAR(500),
    type VARCHAR(50),
    url VARCHAR(1000),
    parent INTEGER,
    descendants INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create index for faster lookups by id
CREATE INDEX IF NOT EXISTS idx_posts_id ON posts(id);
