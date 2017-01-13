create database test;
use test;
create table tss_data(skey varchar(50), svalue varchar(50));
create table tss_offsets(topic_name varchar(50),partition int, offset int);
insert into tss_offsets values('SensorTopic1',0,0);
insert into tss_offsets values('SensorTopic1',1,0);
insert into tss_offsets values('SensorTopic1',2,0);