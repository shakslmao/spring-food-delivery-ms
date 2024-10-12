-- Insert more data into the restaurant table
INSERT INTO restaurant (name, address, contact_number, location, description, opening_hours, rating, is_open, price_range)
VALUES
('Taco Town', '202 Elm St', '123-555-6789', 'Downtown', 'Authentic Mexican tacos with fresh ingredients.', '11:00 - 21:00', 4.7, true, '$'),
('Steakhouse 101', '303 Oak Ave', '555-123-4567', 'Midtown', 'Premium steaks grilled to perfection.', '17:00 - 23:00', 4.9, true, '$$$$'),
('Vegan Delights', '404 Cedar Blvd', '444-789-1234', 'Suburb', 'Plant-based dishes full of flavor.', '10:00 - 20:00', 4.3, true, '$$'),
('Dim Sum Dynasty', '505 Pine St', '777-888-9999', 'Chinatown', 'Traditional Cantonese dim sum served fresh daily.', '09:00 - 15:00', 4.6, true, '$$$'),
('Curry Corner', '606 Spruce Ln', '222-333-4444', 'Downtown', 'Rich and flavorful Indian curries.', '12:00 - 22:00', 4.5, true, '$$'),
('Mediterranean Magic', '707 Palm St', '666-111-2222', 'Seaside', 'Healthy and delicious Mediterranean cuisine.', '11:00 - 21:00', 4.4, true, '$$$'),
('Pizza Palace', '123 Main St', '123-456-7890', 'Downtown', 'Famous for its delicious handmade pizzas.', '11:00 - 23:00', 4.5, true, '$$'),
('Sushi World', '456 Ocean Ave', '987-654-3210', 'Seaside', 'Authentic Japanese sushi experience.', '12:00 - 22:00', 4.8, true, '$$$'),
('Burger Barn', '789 Park Blvd', '456-789-0123', 'Midtown', 'Best burgers in town with locally sourced ingredients.', '10:00 - 20:00', 4.2, true, '$'),
('Pasta House', '101 Maple Dr', '321-654-9870', 'Suburb', 'Italian pasta dishes with a modern twist.', '12:00 - 21:00', 4.6, true, '$$');

-- Insert more data into the cuisine_types table
INSERT INTO cuisine_types (name, description, price, restaurant_id)
VALUES
-- Taco Town cuisines
('Chicken Tacos', 'Soft corn tortillas filled with seasoned grilled chicken.', 7.99, 5),
('Beef Tacos', 'Ground beef tacos topped with fresh salsa and cheese.', 8.99, 5),
('Fish Tacos', 'Crispy fish tacos with cabbage slaw and a tangy sauce.', 9.99, 5),

-- Steakhouse 101 cuisines
('Ribeye Steak', 'Juicy ribeye steak with a side of garlic mashed potatoes.', 29.99, 6),
('Filet Mignon', 'Tender filet mignon with a red wine reduction.', 35.99, 6),
('T-Bone Steak', 'Grilled T-bone steak served with steamed vegetables.', 32.99, 6),

-- Vegan Delights cuisines
('Vegan Burger', 'Plant-based burger with lettuce, tomato, and vegan cheese.', 11.99, 7),
('Tofu Stir-Fry', 'Stir-fried tofu with mixed vegetables and soy sauce.', 12.99, 7),
('Vegan Caesar Salad', 'Crisp romaine lettuce with vegan Caesar dressing.', 9.99, 7),

-- Dim Sum Dynasty cuisines
('Pork Dumplings', 'Steamed dumplings filled with juicy pork.', 6.99, 8),
('Shrimp Siu Mai', 'Traditional shrimp and pork dumplings.', 7.99, 8),
('BBQ Pork Buns', 'Soft buns filled with sweet and savory BBQ pork.', 5.99, 8),

-- Curry Corner cuisines
('Butter Chicken', 'Creamy tomato-based curry with tender chicken pieces.', 14.99, 9),
('Lamb Vindaloo', 'Spicy lamb curry with a tangy vinegar sauce.', 16.99, 9),
('Paneer Tikka Masala', 'Grilled paneer in a rich tomato and cream sauce.', 13.99, 9),

-- Mediterranean Magic cuisines
('Falafel Wrap', 'Crispy falafel with hummus, lettuce, and tahini sauce.', 8.99, 10),
('Chicken Shawarma', 'Marinated chicken wrapped in pita with garlic sauce.', 10.99, 10),
('Grilled Lamb Kofta', 'Spiced lamb skewers with a side of couscous.', 14.99, 10);
