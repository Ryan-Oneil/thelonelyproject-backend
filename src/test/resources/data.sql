insert into users
values ('1', 'test@lonelyproject.org', 'ROLE_USER');
insert into users
values ('2', 'test2@lonelyproject.org', 'ROLE_USER');
insert into users
values ('3', 'test3@lonelyproject.org', 'ROLE_USER');
insert into users
values ('4', 'test4@lonelyproject.org', 'ROLE_USER');
insert into users
values ('5', 'test5@lonelyproject.org', 'ROLE_USER');

insert into user_profile(name, about, user_id, picture_id)
values ('test1', 'test', '1', null);
insert into user_profile(name, about, user_id, picture_id)
values ('test2', 'test', '2', null);
insert into user_profile(name, about, user_id, picture_id)
values ('test3', 'test', '3', null);
insert into user_profile(name, about, user_id, picture_id)
values ('test4', 'test', '4', null);
insert into user_profile(name, about, user_id, picture_id)
values ('test5', 'test', '5', null);

insert into profile_connection(status, connector_id, target_id)
values ('CONNECTED', '1', '2');
insert into profile_connection(status, connector_id, target_id)
values ('CONNECTED', '1', '3');
insert into profile_connection(status, connector_id, target_id)
values ('PENDING', '4', '1');
insert into profile_connection(status, connector_id, target_id)
values ('CONNECTED', '4', '2');
insert into profile_connection(status, connector_id, target_id)
values ('CONNECTED', '4', '3');
insert into profile_connection(status, connector_id, target_id)
values ('DENIED', '4', '5');

insert into profile_match (score, generated, profile_id, match_profile_id)
values (0, '2022-03-25 00:41:30.269000', '1', '2');
insert into profile_match (score, generated, profile_id, match_profile_id)
values (0, '2022-03-25 00:41:30.269000', '1', '3');

insert into cloud_item_details(id, external_id, name, size, container_name)
VALUES (1, '1', 'test', 0, 'test');
insert into cloud_item_details(id, external_id, name, size, container_name)
VALUES (2, '2', 'test2', 0, 'test');

insert into profile_picture(id, cloud_item_detail_id, url, user_profile_user_id)
VALUES (1, 2, 'test', '3');

insert into profile_picture(id, cloud_item_detail_id, url, user_profile_user_id)
VALUES (2, 2, 'test2', '2');

update user_profile
set picture_id = 1
where user_id = '3';

update user_profile
set picture_id = 2
where user_id = '2';

insert into profile_media(id, cloud_item_detail_id, url, type, user_profile_user_id)
values (1, '1', 'test', 'IMAGE', '1');

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
insert into interest(id, category_id, name)
VALUES (6, 2, 'test6');
insert into interest(id, category_id, name)
VALUES (7, 2, 'test7');
insert into interest(id, category_id, name)
VALUES (8, 2, 'test8');
insert into interest(id, category_id, name)
VALUES (9, 2, 'test9');

insert into user_interest(user_profile_id, trait_id)
VALUES ('2', 1);

insert into user_interest(user_profile_id, trait_id)
VALUES ('3', 1);
insert into user_interest(user_profile_id, trait_id)
VALUES ('3', 2);
insert into user_interest(user_profile_id, trait_id)
VALUES ('3', 3);
insert into user_interest(user_profile_id, trait_id)
VALUES ('3', 4);
insert into user_interest(user_profile_id, trait_id)
VALUES ('3', 5);
insert into user_interest(user_profile_id, trait_id)
VALUES ('3', 6);
insert into user_interest(user_profile_id, trait_id)
VALUES ('3', 7);
insert into user_interest(user_profile_id, trait_id)
VALUES ('3', 8);

insert into prompt(id, name)
values (1, 'test');

insert into user_prompt (user_profile_id, text, trait_id)
values ('1', 'test', 1);

insert into chat_room(id, name, type, icon)
VALUES ('95c97c8a-7fe3-4aed-9ceb-26acad52575e', 'test', 'DIRECT', '');

insert into chat_room_participant(chatroom_id, user_profile_id)
VALUES ('95c97c8a-7fe3-4aed-9ceb-26acad52575e', '1');
insert into chat_room_participant(chatroom_id, user_profile_id)
VALUES ('95c97c8a-7fe3-4aed-9ceb-26acad52575e', '2');

insert into chat_message(chat_room_id, sender_id, content, timestamp)
VALUES ('95c97c8a-7fe3-4aed-9ceb-26acad52575e', '2', 'test', '2022-03-25 00:41:30.269000');
insert into chat_message(chat_room_id, sender_id, content, timestamp)
VALUES ('95c97c8a-7fe3-4aed-9ceb-26acad52575e', '1', 'last', '2022-03-26 01:42:30.269000');

SELECT setval('cloud_item_details_id_seq', (SELECT MAX(id) FROM cloud_item_details));
SELECT setval('profile_media_id_seq', (SELECT MAX(id) FROM profile_media));
SELECT setval('chat_message_id_seq', (SELECT MAX(id) FROM chat_message));
SELECT setval('chat_shared_media_id_seq', (SELECT MAX(id) FROM chat_shared_media));
SELECT setval('profile_picture_id_seq', (SELECT MAX(id) FROM profile_picture));
