INSERT INTO events (id, name, total_seats, on_sale_at)
VALUES (1, 'Champions League Final', 5, '2025-06-01 21:00:00');

INSERT INTO seats (id, seat_number, status, event_id)
VALUES (1, 'S000001', 'AVAILABLE', 1),
       (2, 'S000002', 'AVAILABLE', 1),
       (3, 'S000003', 'AVAILABLE', 1),
       (4, 'S000004', 'AVAILABLE', 1),
       (5, 'S000005', 'AVAILABLE', 1);
