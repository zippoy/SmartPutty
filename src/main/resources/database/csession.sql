CREATE MEMORY TABLE csession (
	host VARCHAR(50) NOT NULL,
	port VARCHAR(10) DEFAULT '22' NOT NULL,
	username VARCHAR(50) NOT NULL,
	password VARCHAR(50),
	protocol VARCHAR(10) NOT NULL,
	key VARCHAR(100),
PRIMARY KEY(host,port,username,protocol));
