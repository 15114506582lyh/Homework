create database homework;
use homework;
set @@foreign_key_checks=OFF;
set sql_safe_updates=0;
drop table if exists Customer;
create table Customer(
customer_id int primary key not null auto_increment comment'客户id',
customer_number varchar(50) not null comment'客户编码',
customer_name varchar(50) not null comment'客户名称',
customer_type varchar(30) default'暂无' comment'客户类型',
email varchar(300) default'暂无' comment'邮箱',
status varchar(50) not null comment'客户状态'
)comment'客户信息表';
drop table if exists customerlocation;
create table CustomerLocation(
location_id int primary key not null auto_increment comment'地点id',
customer_id int not null comment'客户id',
address varchar(200) not null comment'客户收货地址',
phone varchar(20) not null comment'客户收货电话',
foreign key(customer_id) references Customer(customer_id)
)comment'客户地点表';
drop table if exists item;
create table Item(
item_id int primary key not null auto_increment comment'商品id',
item_name varchar(300) not null comment'商品名称',
uom varchar(10) not null comment'单位',
price decimal(10,4) comment'参考单价',
status varchar(50) not null comment'商品状态'
)comment'商品信息表';
drop table if exists orderheader;
create table OrderHeader(
order_id int primary key not null auto_increment comment'订单头id',
customer_id int not null comment'客户id',
order_date datetime not null comment'下单日期',
status varchar(50) not null comment'订单状态',
foreign key(customer_id) references Customer(customer_id)
)comment'订单头表';
drop table if exists orderline;
create table OrderLine(
line_id int primary key not null auto_increment comment'订单行id',
order_id int not null comment'订单头id',
item_id int not null comment'商品id',
price decimal(10,4) not null comment'单价',
quality decimal(10,4) not null comment'行数量',
foreign key(order_id) references OrderHeader(order_id),
foreign key(item_id) references Item(item_id)
)comment'订单行表';
drop table if exists shipment;
create table Shipment(
shipment_id int primary key not null auto_increment comment'订单发货行id',
line_id int not null comment'订单行id',
location_id int not null comment'客户地点id',
shipment_date datetime comment'发货日期',
quality decimal(10,4) not null comment'发货行数量',
status varchar(50) not null comment'发货状态',
foreign key(line_id) references OrderLine(line_id)
)comment'订单发货行表';
insert into customer values
(10000001,'c001','华为','企业','1234@huawei.com','有效'),
(customer_id,'c002','小米','企业','1234@xiaomi.com','有效'),
(customer_id,'c003','苹果','企业','1234@apple.com','有效'),
(customer_id,'c004','联想','企业','1234@lenovo.com','有效'),
(customer_id,'c005','戴尔','企业','1234@dell.com','有效'),
(customer_id,'c006','库克','个人','TimothyDonaldCook@apple.com','有效'),
(customer_id,'c007','乔布斯','个人','SteveJobs@apple.com','有效'),
(customer_id,'c008','任正非','个人','Zhengfei.Ren@huawei.com','有效'),
(customer_id,'c009','雷军',customer_type,'Jun.Lei@xiaomi.con','有效'),
(customer_id,'c0010','李宇航','个人',email,'有效');
insert into customerlocation values
(1111,10000001,'华为1','11111'),
(location_id,10000001,'华为2','11112'),
(location_id,10000002,'小米1','11113'),
(location_id,10000002,'小米2','11114'),
(location_id,10000003,'苹果1','11115'),
(location_id,10000003,'苹果2','11116'),
(location_id,10000004,'联想1','11117'),
(location_id,10000004,'联想2','11118'),
(location_id,10000005,'戴尔1','11119'),
(location_id,10000005,'戴尔2','11120'),
(location_id,10000006,'苹果3','11121'),
(location_id,10000007,'苹果4','11122'),
(location_id,10000008,'华为3','11123'),
(location_id,10000009,'小米3','11124'),
(location_id,10000010,'深圳','11125');
insert into Item values
(1001,'德芙巧克力','条',8,'有效'),
(item_id,'士力架','件',100987.1234,'有效'),
(item_id,'可口可乐','件',565435.4325,'有效'),
(item_id,'百事可乐','件',567435,'有效'),
(item_id,'农夫山泉矿泉水','箱',667.99,'有效'),
(item_id,'怡宝饮用水','箱',100000.9999,'有效'),
(item_id,'崂山汽水','瓶',null,'已下架'),
(item_id,'蒙牛纯牛奶','箱',50,'有效'),
(item_id,'特仑苏牛奶','箱',null,''),
(item_id,'好丽友薯片','件',12389.7563,'有效');