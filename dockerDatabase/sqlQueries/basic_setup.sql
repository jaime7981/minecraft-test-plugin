CREATE TABLE player (
    id SERIAL PRIMARY KEY,
    player_name VARCHAR(50),
    passwrd TEXT
);

CREATE OR REPLACE FUNCTION check_username(username VARCHAR(50))
RETURNS VOID AS $$
BEGIN
    IF EXISTS (SELECT 1 FROM player WHERE player_name = username) THEN
        RAISE EXCEPTION 'Username already exists: %', username;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION prevent_duplicate_usernames()
RETURNS TRIGGER AS $$
BEGIN
    PERFORM check_username(NEW.player_name);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER unique_username_trigger
BEFORE INSERT ON player
FOR EACH ROW
EXECUTE FUNCTION prevent_duplicate_usernames();


CREATE TABLE faction (
    id SERIAL PRIMARY KEY,
    faction_name VARCHAR(50)
);

CREATE OR REPLACE FUNCTION check_faction_name(factionname VARCHAR(50))
RETURNS VOID AS $$
BEGIN
    IF EXISTS (SELECT 1 FROM faction WHERE faction_name = factionname) THEN
        RAISE EXCEPTION 'faction_name already exists: %', factionname;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION prevent_duplicate_faction_name()
RETURNS TRIGGER AS $$
BEGIN
    PERFORM check_faction_name(NEW.faction_name);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER unique_faction_name_trigger
BEFORE INSERT ON faction
FOR EACH ROW
EXECUTE FUNCTION prevent_duplicate_faction_name();

CREATE TABLE faction_members (
    id SERIAL PRIMARY KEY,
    player_id INTEGER REFERENCES player(id),
    faction_id INTEGER REFERENCES faction(id)
);

CREATE TABLE faction_relationship (
    id SERIAL PRIMARY KEY,
    faction_one_id INTEGER REFERENCES faction(id),
    faction_two_id INTEGER REFERENCES faction(id),
    relationship VARCHAR(20)
);


