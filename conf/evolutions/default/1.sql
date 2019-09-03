# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table measure (
  id                            integer auto_increment not null,
  sensor_id                     integer not null,
  temperatura                   float not null,
  humedad                       float not null,
  fechat                        bigint not null,
  version                       bigint not null,
  constraint pk_measure primary key (id)
);

create table sensor (
  id                            integer auto_increment not null,
  orden                         integer not null,
  nombre                        varchar(255) not null,
  version                       bigint not null,
  constraint pk_sensor primary key (id)
);

alter table measure add constraint fk_measure_sensor_id foreign key (sensor_id) references sensor (id) on delete restrict on update restrict;
create index ix_measure_sensor_id on measure (sensor_id);


# --- !Downs

alter table measure drop foreign key fk_measure_sensor_id;
drop index ix_measure_sensor_id on measure;

drop table if exists measure;

drop table if exists sensor;

