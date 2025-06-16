-- Users table
CREATE TABLE users (
    id UUID PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    birth_date DATE NOT NULL,
    avatar_url VARCHAR(500),
    chips_balance BIGINT DEFAULT 1000,
    total_chips_won BIGINT DEFAULT 0,
    level INTEGER DEFAULT 1,
    experience_points INTEGER DEFAULT 0,
    is_premium BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    last_login TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Games table
CREATE TABLE games (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(20) NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    min_bet INTEGER DEFAULT 10,
    max_bet INTEGER DEFAULT 1000,
    max_players INTEGER DEFAULT 6,
    settings JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Game sessions table
CREATE TABLE game_sessions (
    id UUID PRIMARY KEY,
    game_id UUID NOT NULL REFERENCES games(id),
    session_data JSONB,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    started_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ended_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- User game history table
CREATE TABLE user_game_history (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id),
    game_id UUID NOT NULL REFERENCES games(id),
    session_id UUID NOT NULL REFERENCES game_sessions(id),
    chips_before BIGINT NOT NULL,
    chips_after BIGINT NOT NULL,
    chips_wagered BIGINT NOT NULL,
    chips_won BIGINT DEFAULT 0,
    game_data JSONB,
    played_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Transactions table
CREATE TABLE transactions (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id),
    type VARCHAR(50) NOT NULL,
    amount BIGINT NOT NULL,
    balance_before BIGINT NOT NULL,
    balance_after BIGINT NOT NULL,
    reference_id UUID,
    description VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_games_type ON games(type);
CREATE INDEX idx_games_status ON games(status);
CREATE INDEX idx_game_sessions_game_id ON game_sessions(game_id);
CREATE INDEX idx_game_sessions_status ON game_sessions(status);
CREATE INDEX idx_user_game_history_user_id ON user_game_history(user_id);
CREATE INDEX idx_user_game_history_game_id ON user_game_history(game_id);
CREATE INDEX idx_user_game_history_session_id ON user_game_history(session_id);
CREATE INDEX idx_transactions_user_id ON transactions(user_id);
CREATE INDEX idx_transactions_type ON transactions(type);
CREATE INDEX idx_transactions_reference_id ON transactions(reference_id);

-- Insert default game types
INSERT INTO games (id, name, type, min_bet, max_bet, max_players, settings)
VALUES 
    (gen_random_uuid(), 'Texas Hold''em Poker', 'POKER', 10, 1000, 6, '{"rules": {"blinds": "10/20", "buyIn": {"min": 100, "max": 1000}}, "payouts": {}}'),
    (gen_random_uuid(), 'Blackjack', 'BLACKJACK', 10, 500, 5, '{"rules": {"dealerStandsOn": 17, "blackjackPays": 1.5}, "payouts": {"blackjack": 1.5, "win": 1.0}}'),
    (gen_random_uuid(), 'Lucky Slots', 'SLOTS', 10, 100, 1, '{"rules": {"reels": 5, "paylines": 20}, "payouts": {"jackpot": 1000, "fiveWild": 500}}'),
    (gen_random_uuid(), 'European Roulette', 'ROULETTE', 10, 500, 8, '{"rules": {"type": "european"}, "payouts": {"straight": 35, "split": 17, "street": 11}}');
