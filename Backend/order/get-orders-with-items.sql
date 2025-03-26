/*
================================================================================
Query 1: One row per menu item in each order
--------------------------------------------------------------------------------
This query retrieves all orders, where each row represents a single menu item
within an order.

Result:
- One row per order-item combination
- Useful for detailed breakdowns or tabular views
================================================================================
*/
SELECT
    o.id AS order_id,
    o.name AS customer_name,
    m.id AS menu_item_id,
    m.name AS menu_item_name,
    m.category,
    m.price
FROM bbq_order o
         JOIN order_items oi ON o.id = oi.order_id
         JOIN menu_item m ON m.id = oi.menu_item_id
ORDER BY o.id;


/*
================================================================================
Query 2: Aggregated menu items per order
--------------------------------------------------------------------------------
This query retrieves all orders and aggregates the menu items per order into
a single string column.

Result:
- One row per order
- All menu items for the order are shown in a single `items` column
- Useful for reports or compact overviews
================================================================================
*/
SELECT
    o.id AS order_id,
    o.name AS customer_name,
    STRING_AGG(m.name || ' (' || m.category || ', ' || m.price || '€)', ', ') AS items
FROM bbq_order o
         JOIN order_items oi ON o.id = oi.order_id
         JOIN menu_item m ON m.id = oi.menu_item_id
GROUP BY o.id, o.name
ORDER BY o.id;
