# xlsx-sql
Simple project to create an all-purpose excel to SQL statements generator.

Coded in the form of a library to be included in any project, but will be used in a REST API Soon.

The excel that is taken as an input should have the column names in the first row/
Each excel column name will be translated into an SQL column name using the following conventions:
( COLUMN_NAME - the column name in excel, VALUE - value in the excel row/cell)

EXCEL              ----                                      DB <br>
COLUMN_NAME                              ---              COLUMN_NAME <br>
COLUMN_NAME*                             ---              WHERE COLUMN_NAME = VALUE <br>
COLUMN_NAME>V=VALUE&S=VALUE_2&?=VALUE_3  ---              Values in the excel matching V are inserted/updated as VALUES , values in the excel matching S are inserted/updated as VALUE_2 , any other value is inserted/updated as VALUE_3<br>
COLUMN_NAME<=>id-name|users              ---              insert/update value of COLUMN_NAME using Foreign Key Constraint - select id from users where name = VALUE<br>
