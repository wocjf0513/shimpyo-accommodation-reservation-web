insert into reservation (id, member_id, pay_method, total_price)
values (1, 1, 'CREDIT_CARD', 240000);

insert into reservation (id, member_id, pay_method, total_price)
values (2, 1, 'CREDIT_CARD', 240000);

insert into reservation_product (id, reservation_id, room_id, start_date, end_date, total_people, price)
values (1, 1, 1, '2023-12-10', '2023-12-13', 3, 95000);

insert into reservation_product (id, reservation_id, room_id, start_date, end_date, total_people, price)
values (2, 1, 3, '2023-12-13', '2023-12-15', 3, 125000);

insert into reservation_product (id, reservation_id, room_id, start_date, end_date, total_people, price)
values (3, 2, 2, '2023-12-24', '2023-12-27', 3, 105000);
