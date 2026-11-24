-- Current Supabase schema and RLS policies for kRations.
-- RLS is enabled on all tables below. No updated_at trigger exists;
-- clients must update updated_at explicitly. Soft deletes use deleted_at.

create table public.categories (
  id uuid not null default gen_random_uuid(),
  owner_user_id uuid not null references auth.users(id),
  name text not null,
  icon_name text not null default 'package',
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  deleted_at timestamptz null,
  constraint categories_pkey primary key (id),
  constraint uq_categories_owner unique (id, owner_user_id),
  constraint chk_categories_icon check (length(trim(icon_name)) > 0),
  constraint chk_categories_name check (length(trim(name)) > 0)
);
create index if not exists idx_categories_owner_name on public.categories (owner_user_id, name);

create table public.products (
  id uuid not null default gen_random_uuid(),
  owner_user_id uuid not null references auth.users(id),
  category_id uuid null,
  name text not null,
  description text null,
  icon_name text null,
  unit text not null default 'unit',
  min_quantity numeric not null default 0,
  expiry_alert_days integer not null default 7,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  deleted_at timestamptz null,
  constraint products_pkey primary key (id),
  constraint uq_products_owner unique (id, owner_user_id),
  constraint fk_products_category_owner foreign key (category_id, owner_user_id)
    references public.categories (id, owner_user_id) on delete restrict,
  constraint chk_products_expiry_alert_days check (expiry_alert_days >= 0),
  constraint chk_products_unit check (length(trim(unit)) > 0),
  constraint chk_products_icon check (icon_name is null or length(trim(icon_name)) > 0),
  constraint chk_products_min_quantity check (min_quantity >= 0),
  constraint chk_products_name check (length(trim(name)) > 0)
);
create index if not exists idx_products_owner_name on public.products (owner_user_id, name);
create index if not exists idx_products_category_owner on public.products (category_id, owner_user_id);

create table public.inventory_batches (
  id uuid not null default gen_random_uuid(),
  owner_user_id uuid not null references auth.users(id),
  product_id uuid not null,
  quantity numeric not null,
  expiration_date date null,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  deleted_at timestamptz null,
  constraint inventory_batches_pkey primary key (id),
  constraint fk_batches_product_owner foreign key (product_id, owner_user_id)
    references public.products (id, owner_user_id) on delete restrict,
  constraint chk_batches_quantity check (quantity >= 0)
);
create index if not exists idx_batches_product_expiration on public.inventory_batches (product_id, owner_user_id, expiration_date);
create index if not exists idx_batches_owner_expiration on public.inventory_batches (owner_user_id, expiration_date);

create table public.shopping_list_items (
  id uuid not null default gen_random_uuid(),
  owner_user_id uuid not null references auth.users(id),
  product_id uuid null,
  name text not null,
  quantity_needed numeric not null default 1,
  unit text not null default 'unit',
  is_purchased boolean not null default false,
  purchased_quantity numeric not null default 0,
  purchased_at timestamptz null,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  deleted_at timestamptz null,
  constraint shopping_list_items_pkey primary key (id),
  constraint fk_shopping_product_owner foreign key (product_id, owner_user_id)
    references public.products (id, owner_user_id) on delete restrict,
  constraint chk_shopping_name check (length(trim(name)) > 0),
  constraint chk_shopping_unit check (length(trim(unit)) > 0),
  constraint chk_shopping_purchased_quantity check (purchased_quantity >= 0),
  constraint chk_shopping_quantity_needed check (quantity_needed > 0)
);
create index if not exists idx_shopping_owner_purchased on public.shopping_list_items (owner_user_id, is_purchased);
create index if not exists idx_shopping_product_owner on public.shopping_list_items (product_id, owner_user_id);

alter table public.categories enable row level security;
alter table public.products enable row level security;
alter table public.inventory_batches enable row level security;
alter table public.shopping_list_items enable row level security;

create policy "Users can create own categories" on public.categories for insert to authenticated
  with check ((select auth.uid()) = owner_user_id);
create policy "Users can update own categories" on public.categories for update to authenticated
  using ((select auth.uid()) = owner_user_id) with check ((select auth.uid()) = owner_user_id);
create policy "Users can view own categories" on public.categories for select to authenticated
  using ((select auth.uid()) = owner_user_id);

create policy "Users can create own products" on public.products for insert to authenticated
  with check ((select auth.uid()) = owner_user_id);
create policy "Users can update own products" on public.products for update to authenticated
  using ((select auth.uid()) = owner_user_id) with check ((select auth.uid()) = owner_user_id);
create policy "Users can view own products" on public.products for select to authenticated
  using ((select auth.uid()) = owner_user_id);

create policy "Users can create own batches" on public.inventory_batches for insert to authenticated
  with check ((select auth.uid()) = owner_user_id);
create policy "Users can update own batches" on public.inventory_batches for update to authenticated
  using ((select auth.uid()) = owner_user_id) with check ((select auth.uid()) = owner_user_id);
create policy "Users can view own batches" on public.inventory_batches for select to authenticated
  using ((select auth.uid()) = owner_user_id);

create policy "Users can create own shopping items" on public.shopping_list_items for insert to authenticated
  with check ((select auth.uid()) = owner_user_id);
create policy "Users can update own shopping items" on public.shopping_list_items for update to authenticated
  using ((select auth.uid()) = owner_user_id) with check ((select auth.uid()) = owner_user_id);
create policy "Users can view own shopping items" on public.shopping_list_items for select to authenticated
  using ((select auth.uid()) = owner_user_id);

-- There are no DELETE policies; clients should soft-delete by setting deleted_at.
