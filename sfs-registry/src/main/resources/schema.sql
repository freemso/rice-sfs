CREATE TABLE IF NOT EXISTS entity
(
    name        VARCHAR(256) PRIMARY KEY,
    description TEXT
);

CREATE TABLE IF NOT EXISTS feature
(
    name                  VARCHAR(256) PRIMARY KEY,
    description           TEXT,
    value_type            VARCHAR(20),
    delta_table_col_name  VARCHAR(256),
    dynamo_table_col_name VARCHAR(256)
);

CREATE TABLE IF NOT EXISTS feature_table
(
    name              VARCHAR(256) PRIMARY KEY,
    description       TEXT,
    entity            VARCHAR(256),
    features          VARCHAR(256)[],
    delta_table_path  VARCHAR(256),
    dynamo_table_name VARCHAR(256)
);

CREATE TABLE IF NOT EXISTS feature_table_view
(
    name               VARCHAR(256) PRIMARY KEY,
    feature_table_name VARCHAR(256),
    feature_names      VARCHAR(256)[]
);