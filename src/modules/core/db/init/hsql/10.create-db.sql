-- begin FORMALNEMETODY_JAVA_FILE
create table FORMALNEMETODY_JAVA_FILE (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    TITLE varchar(255) not null,
    SOURCE longvarchar not null,
    PROJECT_ID varchar(36) not null,
    --
    primary key (ID)
)^
-- end FORMALNEMETODY_JAVA_FILE
-- begin FORMALNEMETODY_JAVA_PROJECT
create table FORMALNEMETODY_JAVA_PROJECT (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    TITLE varchar(255) not null,
    --
    primary key (ID)
)^
-- end FORMALNEMETODY_JAVA_PROJECT
