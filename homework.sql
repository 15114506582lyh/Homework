create database homework;
use homework;
set @@foreign_key_checks=OFF;
create table Customer(
customer_id int primary key not null comment'客户id',
customer_name varchar(50) not null comment'客户名称',
customer_type varchar(30) default'暂无' comment'客户类型',
email varchar(300) default'暂无' comment'邮箱',
status varchar(50) not null comment'客户状态'
)comment'客户信息表';
create table CustomerLocation(
location_id int primary key not null comment'地点id',
customer_id int not null comment'客户id',
address varchar(200) not null comment'客户收货地址',
phone varchar(20) not null comment'客户收货电话',
foreign key(customer_id) references Customer(customer_id),
foreign key(location_id) references Shipment(location_id)
)comment'客户地点表';
create table Item(
item_id int primary key not null comment'商品id',
item_name varchar(300) not null comment'商品名称',
uom varchar(10) not null comment'单位',
price decimal(10,4) comment'参考单价',
status varchar(50) not null comment'商品状态'
)comment'商品信息表';
create table OrderHeader(
order_id int primary key not null comment'订单头id',
customer_id int not null comment'客户id',
order_date datetime not null comment'下单日期',
status varchar(50) not null comment'订单状态',
foreign key(customer_id) references Customer(customer_id)
)comment'订单头表';
create table OrderLine(
line_id int primary key not null comment'订单行id',
order_id int not null comment'订单头id',
item_id int not null comment'商品id',
price decimal(10,4) not null comment'单价',
quality decimal(10,4) not null comment'行数量',
foreign key(order_id) references OrderHeader(order_id),
foreign key(item_id) references Item(item_id)
)comment'订单行表';
create table Shipment(
shipment_id int primary key not null comment'订单发货行id',
line_id int not null comment'订单行id',
location_id int not null comment'客户地点id',
shipment_date datetime comment'发货日期',
quality decimal(10,4) not null comment'发货行数量',
status varchar(50) not null comment'发货状态',
foreign key(line_id) references OrderLine(line_id),
foreign key(location_id) references CustomerLocation(location_id)
)comment'订单发货行表';