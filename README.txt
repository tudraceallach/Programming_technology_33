jdbc:h2:C:\Users\431fl\Downloads\Bank\database  //////ПУТЬ ДЛЯ ПОДКЛЮЧЕНИЯ К БАЗЕ

/////////////////////СОЗДАНИЕ ТАБЛИЦ
create table USER(Id int auto_increment PRIMARY KEY, Login varchar(15) unique, 
	Password varchar(20), Address varchar(50), Phone varchar(10) unique);

create table ACCOUNT(Id uuid unique, Client_Id int,
	Amount decimal(19,2) default 0.00, Acc_Code varchar(3));
	
create table OPERATION(Id int, Date_Operation date, Sum decimal(19,2), Acc_Code varchar(3),
	From_Acc uuid, To_Acc uuid, Balance_Before decimal(19,2), Balance_After decimal(19,2));
	
	
	
//////////////////ТЕСТОВЫЙ ПОЛЬЗОВАТЕЛЬ
insert into user values (default, 'Anastasia', '123', 'Радужная, 128', '9058062211');
insert into user values (default, 'Ana', '123', 'Радужная, 128', '8062211')