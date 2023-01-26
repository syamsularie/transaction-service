create table partners
(
    id            bigserial
        primary key,
    created_at    timestamp(6),
    created_by    varchar(255),
    status_record smallint,
    updated_at    timestamp(6),
    updated_by    varchar(255),
    category      varchar(255),
    description   varchar(255),
    name          varchar(255) not null,
    partner_code  varchar(255) not null
);

create table transactions
(
    id            bigserial
        primary key,
    created_at    timestamp(6),
    created_by    varchar(255),
    status_record smallint,
    updated_at    timestamp(6),
    updated_by    varchar(255),
    email         varchar(255) not null,
    phone         varchar(15)  not null,
    partner_id    bigint
        constraint fk6pg2xwl40lxodccinmiwfofwk
            references public.partners
);

create table transaction_details
(
    id                bigserial
        primary key,
    created_at        timestamp(6),
    created_by        varchar(255),
    status_record     smallint,
    updated_at        timestamp(6),
    updated_by        varchar(255),
    cashback_transaction    double precision,
    cashback_loyalty    double precision,
    status            smallint,
    total_amount      double precision,
    total_cashback    double precision,
    total_transaction integer not null,
    transaction_id    bigint
        constraint fkm5vjjt9jqvf7y69innpgqnipr
            references public.transactions
);

create table user_tiers
(
    id                bigserial
        primary key,
    created_at        timestamp(6),
    created_by        varchar(255),
    status_record     smallint,
    updated_at        timestamp(6),
    updated_by        varchar(255),
    email             varchar(255) not null,
    last_transaction  timestamp(6),
    tier_type         smallint,
    transaction_count integer      not null
);