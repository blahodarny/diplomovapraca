-- begin FORMALNEMETODY_JAVA_FILE
alter table FORMALNEMETODY_JAVA_FILE add constraint FK_FORMALNEMETODY_JAVA_FILE_ON_PROJECT foreign key (PROJECT_ID) references FORMALNEMETODY_JAVA_PROJECT(ID)^
create index IDX_FORMALNEMETODY_JAVA_FILE_ON_PROJECT on FORMALNEMETODY_JAVA_FILE (PROJECT_ID)^
-- end FORMALNEMETODY_JAVA_FILE
