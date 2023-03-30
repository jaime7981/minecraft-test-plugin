SELECT f.id AS faction_id, f.faction_name, p.id AS player_id, p.player_name FROM faction f 
JOIN faction_members f_mem ON f.id = f_mem.faction_id 
JOIN player p ON f_mem.player_id = p.id;
SELECT * FROM faction;
SELECT * FROM player;
SELECT * FROM faction_members;
