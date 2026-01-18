INSERT INTO product(product_id, product_name, price, quantity) VALUES
('P001','osii 舒壓按摩椅',98000,5)
ON CONFLICT (product_id) DO NOTHING;

INSERT INTO product(product_id, product_name, price, quantity) VALUES
('P002','網友最愛起司蛋糕',1200,50)
ON CONFLICT (product_id) DO NOTHING;

INSERT INTO product(product_id, product_name, price, quantity) VALUES
('P003','真愛密碼項鍊',8500,20)
ON CONFLICT (product_id) DO NOTHING;