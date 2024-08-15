create table if not exists account
(
    login_id varchar(100) primary key,
    password varchar(72) not null,
    creator varchar(100) not null,
    created_at timestamp default current_timestamp,
    updater varchar(100),
    updated_at timestamp
);

create table if not exists notice
(
    notice_no int auto_increment primary key,
    title varchar(255) not null,
    content text,
    notice_start_date datetime,
    notice_end_date datetime,
    view_count int default 0,
    creator varchar(100),
    created_at timestamp default current_timestamp,
    updater varchar(100),
    updated_at timestamp,
    foreign key (creator) references account(login_id) on delete set null
);

create table if not exists notice_attachment
(
    notice_attachment_no int auto_increment primary key,
    notice_no int not null,
    file_name varchar(255) not null,
    file_path varchar(255) not null,
    file_size bigint not null,
    creator varchar(100) not null,
    created_at timestamp default current_timestamp,
    updater varchar(100),
    updated_at timestamp,
    foreign key (notice_no) references notice(notice_no) on delete cascade
);