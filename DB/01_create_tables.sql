-- 商品表
CREATE TABLE IF NOT EXISTS product (
  product_id VARCHAR(50) PRIMARY KEY,
  product_name VARCHAR(200) NOT NULL,
  price NUMERIC(12,2) NOT NULL CHECK (price >= 0),
  quantity INT NOT NULL CHECK (quantity >= 0)
);

-- 訂單表
CREATE TABLE IF NOT EXISTS "order" (
  order_id VARCHAR(50) PRIMARY KEY,
  member_id VARCHAR(50) NOT NULL,
  total_amount NUMERIC(12,2) NOT NULL,
  pay_status INT NOT NULL DEFAULT 0,
  created_at TIMESTAMPTZ DEFAULT now()
);

-- 訂單明細
CREATE TABLE IF NOT EXISTS order_detail (
  order_item_sn SERIAL PRIMARY KEY,
  order_id VARCHAR(50) NOT NULL REFERENCES "order"(order_id) ON DELETE CASCADE,
  product_id VARCHAR(50) NOT NULL REFERENCES product(product_id),
  quantity INT NOT NULL CHECK (quantity > 0),
  stand_price NUMERIC(12,2) NOT NULL,
  item_price NUMERIC(12,2) NOT NULL
);