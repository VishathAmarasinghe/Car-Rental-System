create database carRentalSystem;
use carRentalSystem;

create table employee(EmpID varchar(5),role varchar(10),firstName varchar(20),lastName varchar(25),Email varchar(30),Address1 varchar(40),
Address2 varchar(40),city varchar(20),NIC varchar(12),LicenceNo varchar(15),UserName varchar(20),Password varchar(20));

alter table employee modify EmpID varchar(5) primary key; 

create table employeePhone(EmpID varchar(5),phoneNo int, primary key(EmpID,phoneNo),foreign key(EmpID) references employee(EmpID));

insert into employee values("A001","Admin","Nimal","perera","nimalperera@gmail.com","no 13","Galawilawatta","Homagama","200033600702",null,"nimal123",
"nimal123");

insert into employee values("A002","Admin","kalum","Ranasinghe","kalumR@gmail.com","no 17","walawwa Rd","Kottawa","211133600702",null,"kalum123",
"kalum123");

insert into employee values("A003","Cashier","Nisal","Niranjaya","Nisal2001@gmail.com","no 44","uyanwaththa","Rathmalana","200033612345",null,"Nisal123",
"nisal123");

insert into employee values("A004","Cashier","Akila","Amarapala","akilaAmarapala@gmail.com","no 200","nilupul Rd","Colombo","200011112345",null,"Akila123",
"akila123");

insert into employee values("A005","VOwner","sujeewa","Amarasighe","sujeewa2008@gmail.com","no 2","malwaththa Rd","Colombo","211111112345",null,null,
null);

insert into employee values("A006","VOwner","udaya","wikramasinghe","udaya1699@gmail.com","no 13","wihara mawatha Rd","Nugegoda","217878712345",null,null,
null);

insert into employee values("A007","VOwner","chirath","Pilapitiya","Chirath2099@gmail.com","no 1","wijaya Rd","Nugegoda","217878712330",null,null,
null);

insert into employeephone values("A007",0786070411);
insert into employeephone values("A007",0714125369);

insert into employeephone values("A006",0774569362);
insert into employeephone values("A006",0778945612);

insert into employeephone values("A005",0746454545);
insert into employeephone values("A005",0714100000);

select empID, firstname,lastname,email,city,NIC from employee where role="VOwner";
select phoneNo from employeephone where empID="A005";

select max(EmpID) as maxID from employee;

describe employee;
select * from employee;

delete from employeephone where empId="A011";
delete from employee where empId="A011";

select * from employee where empID="A001";

update employee set firstName="Nimals",lastName="Perera",Email="nimalperera123@gmail.com",Address1="no 13",Address2="Galawilawatta",city="Homagama",NIC="200133600703",LicenceNo=null where empID="A001";


update employeephone set phoneNo="0786070411" where empID="A001";


alter table cars drop column OwnerID;

alter table cars modify OwnerID varchar(20) ;

alter table cars add foreign key (OwnerID) references employee(EmpID);

delete from cars where carNumber="GQ2525";

alter table customerphone add foreign key (customerID) references customer(customerID);

describe employee;

alter table customer modify firstname varchar(20);

describe cars;

select count(carNumber) from cars;

alter table cars add primary key (carNumber);

alter table reservation add foreign key (vehicalNumber) references cars(CarNumber);

select count(cars.carNumber) from cars where carNumber not in
 (select VehicalNumber from reservation 
 where pickedUpdate between "2023-03-15" and "2023-03-18" and dropOffdate between "2023-13-15" and "2023-03-18" ) and vehicalType="Car" ;
 
 
 select VehicalNumber from reservation 
 where pickedUpdate between "2023-03-15" and "2023-03-18" and dropOffdate between "2023-13-15" and "2023-03-18" ;