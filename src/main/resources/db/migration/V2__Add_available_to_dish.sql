-- V2__Add_available_to_dish.sql
ALTER TABLE dish ADD COLUMN available BOOLEAN NOT NULL DEFAULT TRUE;