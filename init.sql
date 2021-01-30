create table bill
(
    id          serial primary key,
    customer    text   not null,
    gst_percent double not null,
    price       double not null,
    created     timestamp default now()
);

create table shopping_item
(
    id                  serial primary key,
    name                text   not null,
    price double not null
);

create table bill_shopping_items
(
    bill_id          int references bill (id),
    shopping_item_id int references shopping_item (id),
    Primary Key (bill_id, shopping_item_id)
);