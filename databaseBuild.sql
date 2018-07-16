-- we don't know how to generate schema SSM (class Schema) :(
create table product
(
  id int auto_increment
    primary key,
  name varchar(64) not null,
  price double not null,
  discount double default '1' not null,
  sale int default '0' not null,
  stock int default '0' not null,
  description varchar(256) not null,
  useful tinyint(1) default '1' not null,
  constraint product_id_uindex
  unique (id)
)
  engine=InnoDB
;

create table user
(
  id varchar(16) not null
    primary key,
  name varchar(128) not null,
  password varchar(256) not null,
  salt varchar(128) not null,
  is_admin tinyint(1) default '0' not null,
  phone varchar(64) null,
  address varchar(128) null,
  constraint user_id_uindex
  unique (id)
)
  engine=InnoDB
;

create table `order`
(
  id int auto_increment
    primary key,
  create_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
  status varchar(16) not null,
  price double default '0' not null,
  user_id varchar(16) not null,
  constraint order_id_uindex
  unique (id),
  constraint order_user_id_fk
  foreign key (user_id) references user (id)
)
  engine=InnoDB
;

create index order_user_id_fk
  on `order` (user_id)
;

create table orderproductrelation
(
  id int auto_increment
    primary key,
  count int not null,
  order_id int not null,
  product_id int not null,
  constraint table_name_id_uindex
  unique (id),
  constraint orderproductrelation_order_id_fk
  foreign key (order_id) references `order` (id),
  constraint orderproductrelation_product_id_fk
  foreign key (product_id) references product (id)
)
  engine=InnoDB
;

create index table_name_order_id_fk
  on orderproductrelation (order_id)
;

create index table_name_product_id_fk
  on orderproductrelation (product_id)
;

