DROP ALIAS IF EXISTS SLEEP;
CREATE ALIAS SLEEP FOR "org.adbcj.tck.h2.SleepFunction.sleep";

DROP TABLE IF EXISTS simple_values;

CREATE TABLE simple_values (
  int_val int,
  str_val varchar(255)
);

INSERT INTO simple_values (int_val, str_val) values (null, null);
INSERT INTO simple_values (int_val, str_val) values (0, 'Zero');
INSERT INTO simple_values (int_val, str_val) values (1, 'One');
INSERT INTO simple_values (int_val, str_val) values (2, 'Two');
INSERT INTO simple_values (int_val, str_val) values (3, 'Three');
INSERT INTO simple_values (int_val, str_val) values (4, 'Four');

DROP TABLE IF EXISTS updates;
CREATE TABLE updates (id int);

DROP TABLE IF EXISTS locks;
CREATE TABLE locks (name varchar(255) primary key not null);
INSERT INTO locks(name) VALUES ('lock');

DROP TABLE IF EXISTS table_with_some_values;
CREATE TABLE IF NOT EXISTS table_with_some_values (
  auto_int int(11) NOT NULL AUTO_INCREMENT,
  can_be_null_int int(11) DEFAULT NULL,
  can_be_null_varchar varchar(255) DEFAULT NULL,
  PRIMARY KEY (auto_int)
) ;


INSERT INTO table_with_some_values (auto_int, can_be_null_int, can_be_null_varchar) VALUES
(1, NULL, NULL),
(2, 42, '42');

DROP TABLE IF EXISTS supporteddatatypes;
CREATE TABLE IF NOT EXISTS supporteddatatypes (
  intColumn int(11) NOT NULL,
  varCharColumn varchar(255) NOT NULL,
  bigIntColumn bigint(20) NOT NULL,
  decimalColumn decimal(10,2) NOT NULL,
  dateColumn date NOT NULL,
  dateTimeColumn datetime NOT NULL,
  timeColumn time NOT NULL,
  timeStampColumn timestamp NOT NULL,
  doubleColumn double NOT NULL,
  textColumn TEXT NOT NULL
);


INSERT INTO supportedDataTypes (
  intColumn,
  varCharColumn,
  bigIntColumn,
  decimalColumn,
  dateColumn,
  dateTimeColumn,
  timeColumn,
  timeStampColumn,
  doubleColumn,
  textColumn
)
VALUES (
'42', '4242', '42',
 '42.42', '2012-05-03',
 '2012-05-16 16:57:51', '12:05:42',
  '2012-05-16 17:10:36', '42.42',
  '42-4242-42424242-4242424242424242-42424242-4242-42'
);


DROP TABLE IF EXISTS textcontent;

CREATE TABLE IF NOT EXISTS textcontent (
  id int(11) NOT NULL AUTO_INCREMENT,
  lang varchar(16) NOT NULL,
  textData varchar(255) NOT NULL,
  PRIMARY KEY (id)
);


INSERT INTO textcontent (id, lang, textData) VALUES
(1, 'kr', '난 한국어 너무 좋아해요'),
(2, 'en', 'English is a nice language'),
(3, 'de', 'Die äüö sind toll'),
(4, 'zh', '维基百科（英语：Wikipedia）'),
(5, 'ja', 'ウィキペディア（英: Wikipedia）');

DROP TABLE IF EXISTS tablewithautoid;

CREATE TABLE IF NOT EXISTS tablewithautoid(
  id int(11) NOT NULL AUTO_INCREMENT,
  textData varchar(255) NOT NULL,
  PRIMARY KEY (id)
);
