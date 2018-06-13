CREATE TABLE IF NOT EXISTS "users" (
    "username" varchar NOT NULL PRIMARY KEY CHECK (length(username::text) >= 4),
    "email" varchar NOT NULL UNIQUE CHECK (email ~* '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$'),
    "firstname" varchar NOT NULL,
    "lastname" varchar NOT NULL,
    "birthday" date NOT NULL
);
CREATE TABLE IF NOT EXISTS "tweets" (
    "id" SERIAL PRIMARY KEY,
    "username" varchar NOT NULL REFERENCES users(username),
    "date" date NOT NULL,
    "content" varchar NOT NULL
);
CREATE TABLE IF NOT EXISTS "following" (
    "follower" varchar NOT NULL REFERENCES users(username),
    "followee" varchar NOT NULL REFERENCES users(username)
);

ALTER TABLE "following" ADD CONSTRAINT following_pkey PRIMARY KEY (follower, followee);

INSERT INTO users("firstname", "lastname", "email", "username", "birthday") VALUES(
    'Tyler', 'Newman', 'tnewman@dummy.com', 'tnewman', to_date('1995-12-16', 'YYYY-MM-DD')
);
INSERT INTO users("firstname", "lastname", "email", "username", "birthday") VALUES(
    'Madison', 'Maniaci', 'maniacim@dummy.com', 'maniacim', to_date('1996-11-05', 'YYYY-MM-DD')
);
INSERT INTO users("firstname", "lastname", "email", "username", "birthday") VALUES(
    'Ben', 'Muldrow', 'muldrowb@dummy.com', 'muldrowb', to_date('1996-07-08', 'YYYY-MM-DD')
);

INSERT INTO following("follower", "followee") VALUES('tnewman', 'maniacim');
INSERT INTO following("follower", "followee") VALUES('tnewman', 'muldrowb');
INSERT INTO following("follower", "followee") VALUES('maniacim', 'tnewman');
INSERT INTO following("follower", "followee") VALUES('muldrowb', 'maniacim');

INSERT INTO tweets("username", "date", "content") VALUES(
    'tnewman',
    TO_TIMESTAMP('04/12/2016 14:42:28', 'MM/DD/YYYY HH24:MI:SS'),
    'Who else can''t wait til summer? 😎🌞'
);
INSERT INTO tweets("username", "date", "content") VALUES(
    'muldrowb',
    TO_TIMESTAMP('05/02/2016 11:27:31', 'MM/DD/YYYY HH24:MI:SS'),
    'man this twitter clone is WAY better than the original... TAGLESS FINAL FOREVER'
);
INSERT INTO tweets("username", "date", "content") VALUES(
    'tnewman',
    TO_TIMESTAMP('05/04/2016 02:14:55', 'MM/DD/YYYY HH24:MI:SS'),
    'can''t sleep, too much Game of Thrones to watch'
);
INSERT INTO tweets("username", "date", "content") VALUES(
    'maniacim',
    TO_TIMESTAMP('06/23/2016 17:01:26', 'MM/DD/YYYY HH24:MI:SS'),
    'Wondering if I should go buy a seventh puppy....'
);
