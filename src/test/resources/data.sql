insert into users
values ('1', 'test@lonelyproject.org', 'ROLE_USER');
insert into users
values ('2', 'test2@lonelyproject.org', 'ROLE_USER');
insert into users
values ('3', 'test3@lonelyproject.org', 'ROLE_USER');

insert into user_profile(name, about, user_id, picture_id)
values ('test1', 'test', '1', null);
insert into user_profile(name, about, user_id, picture_id)
values ('test2', 'test', '2', null);
insert into user_profile(name, about, user_id, picture_id)
values ('test3', 'test', '3', null);

insert into profile_connection(status, connector_id, target_id)
values ('PENDING', '1', '2');
insert into profile_connection(status, connector_id, target_id)
values ('CONNECTED', '1', '3');

insert into profile_match (score, generated, profile_id, match_profile_id)
values (0, '2022-03-25 00:41:30.269000', '1', '2');
insert into profile_match (score, generated, profile_id, match_profile_id)
values (0, '2022-03-25 00:41:30.269000', '1', '3');

insert into cloud_item_details(id, external_id, name, size, container_name)
VALUES ('1', '1', 'test', 0, 'test');

insert into profile_media(id, cloud_item_detail_id, url, type, user_profile_user_id)
values ('1', '1', 'test', 'IMAGE', '1');

insert into interest_category(id, name)
VALUES (1, 'test');
insert into interest_category(id, name)
VALUES (2, 'test2');

insert into interest(id, category_id, name)
VALUES (1, 1, 'test1');
insert into interest(id, category_id, name)
VALUES (2, 1, 'test2');
insert into interest(id, category_id, name)
VALUES (3, 2, 'test3');
insert into interest(id, category_id, name)
VALUES (4, 2, 'test4');
insert into interest(id, category_id, name)
VALUES (5, 2, 'test5');

insert into user_interest(user_profile_id, trait_id)
VALUES ('1', 1);