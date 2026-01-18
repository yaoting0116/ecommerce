-- Optional: 若要重新建立，先 DROP
DROP FUNCTION IF EXISTS sp_create_order(VARCHAR, TEXT[], INT[]);

CREATE OR REPLACE FUNCTION sp_create_order(
    _member_id VARCHAR,
    _product_ids TEXT[],
    _quantities INT[]
) RETURNS VARCHAR AS $$
DECLARE
    v_order_id VARCHAR := concat('Ms', to_char(now(), 'YYYYMMDDHH24MISS'), floor(random()*9000+1000)::int);
    idx INT;
    pid TEXT;
    qty INT;
    stock INT;
    v_price NUMERIC(12,2);        -- 改名：避免與 table.price 同名
    subtotal NUMERIC(12,2);
    total NUMERIC(12,2) := 0;
BEGIN
    IF array_length(_product_ids,1) IS NULL OR array_length(_product_ids,1) <> array_length(_quantities,1) THEN
        RAISE EXCEPTION '產品陣列與數量陣列長度不符';
    END IF;

    -- 建立 order（先以 0 當 total，之後 update）
    INSERT INTO "order"(order_id, member_id, total_amount, pay_status) VALUES (v_order_id, _member_id, 0, 0);

    FOR idx IN array_lower(_product_ids,1) .. array_upper(_product_ids,1) LOOP
        pid := _product_ids[idx];
        qty := _quantities[idx];

        -- 以表別名取得欄位，並把價格放到 v_price（避免 ambiguous）
        SELECT p.quantity, p.price
        INTO stock, v_price
        FROM product p
        WHERE p.product_id = pid
        FOR UPDATE;

        IF NOT FOUND THEN
            RAISE EXCEPTION 'Product not found: %', pid;
        END IF;

        IF qty <= 0 THEN
            RAISE EXCEPTION 'Quantity must be > 0 for product %', pid;
        END IF;

        IF stock < qty THEN
            RAISE EXCEPTION 'Insufficient stock for product % (requested %, available %)', pid, qty, stock;
        END IF;

        subtotal := v_price * qty;
        total := total + subtotal;

        INSERT INTO order_detail(order_id, product_id, quantity, stand_price, item_price)
        VALUES (v_order_id, pid, qty, v_price, subtotal);

        UPDATE product SET quantity = quantity - qty WHERE product_id = pid;
    END LOOP;

    UPDATE "order" SET total_amount = total WHERE order_id = v_order_id;

    RETURN v_order_id;
END;
$$ LANGUAGE plpgsql;
