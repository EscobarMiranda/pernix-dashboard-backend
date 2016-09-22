insert into usertype (id, name) values (1,'AdminTest');
insert into public.user(id, email, lastname, name, password, user_type_id) values (1, 'test@email.com', 'serrano', 'luis', '123queso', 1);
insert into public.user(id, email, lastname, name, password, user_type_id) values (2, 'test@email.com', 'escobar', 'kevin', '123queso', 1);
insert into usertype (id, name) values (2,'UserTest');
insert into public.user(id, email, lastname, name, password, user_type_id) values (3, 'test@email.com', 'sanchez', 'gustavo', '123queso', 2);
